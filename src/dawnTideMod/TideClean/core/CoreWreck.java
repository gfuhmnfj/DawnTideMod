package dawnTideMod.TideClean.core;

import arc.Core;
import arc.util.Nullable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.modules.ItemModule;
public class CoreWreck extends Block{
    public float salvageFrac = 0.6f;
    public boolean decay = false;
    public float decayRate = 0.002f;
    public int deconstructWorkers = 1;
    public CoreWreck(String name){
        super(name);
        update = true;
        solid = true;
        destructible = true;
        replaceable = true;
        hasItems = false;
        hasPower = false;
        hasLiquids = false;
        breakable = true;
        allowConfigInventory = false;
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(Stat.health, health, StatUnit.none);
        stats.add(Stat.abilities, Core.bundle.format("stat.salvageable", (int)(salvageFrac * 100)));
    }

    public class WreckBuild extends Building{
        public @Nullable Block originalCore;
        protected ItemModule savedItems;
        protected float decayTimer = 0f;
        public void setSavedItems(ItemModule src){
            this.savedItems = src == null ? null : src.copy();
        }

        @Override
        public void updateTile(){
            if(decay && health > 1f){
                decayTimer += delta();
                if(decayTimer >= 60f){
                    decayTimer = 0f;
                    damage(health * decayRate * 60f);
                }
            }
        }

        @Override
        public void onDeconstructed(mindustry.gen.Unit unit){
            super.onDeconstructed(unit);
            if(mindustry.Vars.net.client()) return;
            if(savedItems != null && !savedItems.empty()){
                savedItems.each((item, amount) -> {
                    int give = (int)(amount * salvageFrac);
                    if(give > 0) offloadBatch(item, give);
                });
            }
            if(originalCore != null && originalCore.requirements != null){
                for(ItemStack stack : originalCore.requirements){
                    int give = (int)(stack.amount * salvageFrac);
                    if(give > 0) offloadBatch(stack.item, give);
                }
            }
        }
        protected void offloadBatch(Item item, int amount){
            final int maxPerFrame = 20;
            int now = Math.min(amount, maxPerFrame);
            for(int i = 0; i < now; i++){
                offload(item);
            }

            int rest = amount - now;
            if(rest > 0){
                arc.util.Time.run(rest / (float)maxPerFrame, () -> {
                    for(int i = 0; i < maxPerFrame; i++){
                        offload(item);
                    }
                });
            }
        }

        @Override
        public int getMaximumAccepted(Item item){
            return 0;
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            return false;
        }

        @Override
        public boolean canPickup(){
            return false;
        }

        @Override
        public void write(Writes write){
            super.write(write);

            write.bool(originalCore != null);
            if(originalCore != null){
                write.s(originalCore.id);
            }

            if(savedItems == null){
                write.s(0);
            }else{
                mindustry.type.ItemSeq seq = new mindustry.type.ItemSeq();
                savedItems.each((item, amount) -> {
                    if(amount > 0) seq.add(item, amount);
                });
                ItemStack[] arr = seq.toArray();
                write.s(arr.length);
                for(ItemStack stack : arr){
                    write.s(stack.item.id);
                    write.i(stack.amount);
                }
            }
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            if(read.bool()){
                originalCore = mindustry.Vars.content.block(read.s());
            }

            int n = read.s();
            if(n > 0){
                ItemModule m = new ItemModule();
                for(int i = 0; i < n; i++){
                    Item item = mindustry.Vars.content.item(read.s());
                    int amount = read.i();
                    m.set(item, amount);
                }
                savedItems = m;
            }
        }
    }
}
