package dawnTideMod.TideClean.world;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

/**
 * 液体装卸器 —— 装卸器的液体版。
 * <p>从相邻建筑取出指定液体，注入另一个相邻建筑（自动平衡：优先从存量最多处取，向存量最少处送）。
 * <p>关键字段：
 * <ul>
 *   <li>{@link #speed} —— 每次转送的间隔（tick），越小越快</li>
 *   <li>{@link #amount} —— 每次转送的液体量（1 单位 = 1/60 液体/秒换算基准）</li>
 *   <li>未配置液体时自动轮询所有液体（与原版装卸器逻辑一致）</li>
 * </ul>
 */
public class LiquidUnloader extends Block{
    /** 中心指示贴图（复用原版装卸器中心贴图）。 */
    public TextureRegion centerRegion;
    /** 每次转送间隔（tick）。转送速率 = 60 / speed * amount 液体/秒。 */
    public float speed = 15f;
    /** 每次转送的液体量。 */
    public float amount = 4f;

    public LiquidUnloader(String name){
        super(name);
        update = true;
        solid = true;
        health = 90;
        hasLiquids = true;
        liquidCapacity = 0;
        configurable = true;
        saveConfig = true;
        noUpdateDisabled = true;
        clearOnDoubleTap = true;
        unloadable = false;

        config(Liquid.class, (LiquidUnloaderBuild tile, Liquid liquid) -> tile.sortLiquid = liquid);
        configClear((LiquidUnloaderBuild tile) -> tile.sortLiquid = null);
    }

    @Override
    public void load(){
        super.load();
        centerRegion = Core.atlas.find(name + "-center", "unloader-center");
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(Stat.speed, 60f / speed * amount, StatUnit.liquidSecond);
    }

    @Override
    public void setBars(){
        super.setBars();
        removeBar("liquid");
    }

    public class LiquidUnloaderBuild extends Building{
        public float counter = 0f;
        public Liquid sortLiquid = null;

        /** 相邻建筑中是否存在该液体的「可给方」和「可收方」。 */
        private boolean hasPossibleMove(Liquid liquid){
            boolean hasProvider = false, hasReceiver = false;
            for(Building other : proximity){
                if(!other.interactable(team)) continue;
                float cap = other.block.liquidCapacity;
                float cur = other.liquids.get(liquid);
                hasProvider |= other.team == team && cur > 0.01f;
                hasReceiver |= other.team == team && cap - cur > 0.01f && other.acceptLiquid(this, liquid);
            }
            return hasProvider && hasReceiver;
        }

        @Override
        public void updateTile(){
            if((counter += delta()) < speed) return;

            Liquid move = null;
            if(sortLiquid != null){
                if(hasPossibleMove(sortLiquid)) move = sortLiquid;
            }else{
                for(Liquid liquid : Vars.content.liquids()){
                    if(hasPossibleMove(liquid)){
                        move = liquid;
                        break;
                    }
                }
            }

            if(move == null){
                counter = Math.min(counter, speed);
                return;
            }

            Building from = null, to = null;
            float bestGive = -1f, bestTake = Float.MAX_VALUE;

            for(Building other : proximity){
                if(!other.interactable(team)) continue;
                float cap = other.block.liquidCapacity;
                float cur = other.liquids.get(move);

                if(other.team == team && cur > 0.01f && cur > bestGive){
                    bestGive = cur;
                    from = other;
                }
                if(other.team == team && cap - cur > 0.01f && other.acceptLiquid(this, move) && cur < bestTake){
                    bestTake = cur;
                    to = other;
                }
            }

            if(from != null && to != null && from != to){
                float free = to.block.liquidCapacity - to.liquids.get(move);
                float moveAmt = Math.min(amount, Math.min(from.liquids.get(move), free));
                if(moveAmt > 0.01f){
                    from.liquids.remove(move, moveAmt);
                    to.handleLiquid(this, move, moveAmt);
                    counter %= speed;
                }else{
                    counter = Math.min(counter, speed);
                }
            }else{
                counter = Math.min(counter, speed);
            }
        }

        @Override
        public void draw(){
            super.draw();
            Draw.color(sortLiquid == null ? Color.clear : sortLiquid.color);
            Draw.rect(centerRegion, x, y);
            Draw.color();
        }

        @Override
        public void buildConfiguration(Table table){
            ItemSelection.buildTable(LiquidUnloader.this, table, Vars.content.liquids(), () -> sortLiquid, this::configure);
        }

        @Override
        public Liquid config(){
            return sortLiquid;
        }

        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.s(sortLiquid == null ? -1 : sortLiquid.id);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            int id = revision == 1 ? read.s() : read.b();
            sortLiquid = id == -1 ? null : Vars.content.liquid(id);
        }
    }
}
