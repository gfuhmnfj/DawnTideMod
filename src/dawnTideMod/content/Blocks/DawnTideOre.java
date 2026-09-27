package dawnTideMod.content.Blocks;

import dawnTideMod.content.dawnTideItems;
import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;

public class DawnTideOre {
    public static Block
            quartzOre, ironOre, uraniumOre, titaniumSilverOre, oreCrystallizationOre, bariteOre;

    public static void load(){
        quartzOre = new OreBlock(dawnTideItems.quartz) {{
            localizedName = "石英矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        ironOre = new OreBlock(dawnTideItems.iron) {{
            localizedName = "铁矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        uraniumOre = new OreBlock(dawnTideItems.uranium) {{
            localizedName = "铀矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        titaniumSilverOre = new OreBlock(dawnTideItems.titaniumSilver) {{
            localizedName = "钛银矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        oreCrystallizationOre = new OreBlock(dawnTideItems.oreCrystallization) {{
            localizedName = "矿石结晶矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        bariteOre = new OreBlock(dawnTideItems.barite) {{
            localizedName = "重晶石矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};
    }
}
