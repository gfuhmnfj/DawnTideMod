package dawnTideMod.TideClean.core;

import arc.Core;
import arc.Events;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.game.EventType;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class WreckableCore extends CoreBlock{

    public mindustry.world.Block wreckBlock;
    public float wreckHealthFrac = 0.25f;
    public float salvageFrac = 0.6f;
    public boolean silentCollapse = true;
    public boolean transferItems = true;
    public WreckableCore(String name){
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(Stat.health, health, StatUnit.none);
        stats.add(Stat.abilities, Core.bundle.get("stat.wreckable"));
    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("dawntide-wreck", (CoreBuild entity) -> {
            if(!(entity instanceof WreckableCoreBuild e)) return null;
            return new Bar(
                () -> Core.bundle.get("bar.dawntide-integrity"),
                () -> Pal.accent,
                () -> e.integrityFrac()
            );
        });
    }

    @Override
    public void init(){
        super.init();
        if(wreckBlock == null){
            throw new IllegalStateException(
                "WreckableCore '" + name + "' 必须指定 wreckBlock（残骸方块）");
        }
    }

    public class WreckableCoreBuild extends CoreBuild{
        public float integrity = 1f;
        @Override
        public void updateTile(){
            super.updateTile();
            integrity = Mathf.clamp(healthf());
        }
        public float integrityFrac(){
            return integrity;
        }

        @Override
        public void onDestroyed(){
            if(!Vars.net.client() && wreckBlock != null){
                var t = tile;
                var savedItems = transferItems ? items.copy() : null;
                t.setBlock(wreckBlock, team, rotation);
                if(t.build instanceof CoreWreck.WreckBuild wreck){
                    wreck.health(wreckBlock.health * wreckHealthFrac);
                    if(savedItems != null){
                        wreck.setSavedItems(savedItems);
                    }
                    wreck.originalCore = block;
                }
            }

            if(!Vars.headless && silentCollapse){
                Fx.coreLandDust.at(x, y, Pal.accent);
                Sounds.explosionCore.at(x, y, 0.4f);
            }
            EventType.CoreChangeEvent ev =
                new EventType.CoreChangeEvent(this);
            Events.fire(ev);
        }
        @Override
        public void afterDestroyed(){
        }
    }
}
