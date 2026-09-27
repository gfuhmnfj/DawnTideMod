package dawnTideMod.TideClean.multicrafter.world;

import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

/** 冷量机：通电后向相邻建筑输出冷量，供需要冷量输入的配方使用 */
public class ColdSourceBlock extends Block {
    /** 满效率时的冷量输出 */
    public float coldOutput = 10f;

    public ColdSourceBlock(String name){
        super(name);
        update = true;
        solid = true;
        sync = true;
        destructible = true;
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(Stat.output, coldOutput * 60f, StatUnit.perSecond);
    }

    public class ColdSourceBuild extends Building implements ColdSource {
        @Override
        public float coldOutput(){
            return efficiency > 0f ? coldOutput * efficiency : 0f;
        }
    }
}
