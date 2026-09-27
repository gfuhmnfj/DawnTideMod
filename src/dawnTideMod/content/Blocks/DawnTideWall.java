package dawnTideMod.content.Blocks;

import dawnTideMod.TideClean.Wall.RegenWall;
import dawnTideMod.content.dawnTideItems;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;

import static mindustry.type.ItemStack.with;

public class DawnTideWall {
    public static Block
            siliconWall, siliconWallLarge, giantSiliconWall, steelWall, steelWallLarge,
            giantCopperWall, giantTitaniumWall, giantThoriumWall, crystalWall, crystalWallLarge,
            erosionResistantWall, erosionResistantWallLarge;

    public static void load(){
        siliconWall = new Wall("SiliconWall") {{ //硅墙
            localizedName = "硅墙";
            requirements(Category.defense, with(Items.silicon, 6));
            health = 650;
        }};

        siliconWallLarge = new Wall("SiliconWallLarge") {{ //大型硅墙
            localizedName = "大型硅墙";
            requirements(Category.defense, with(Items.silicon, 24));
            health = 2600;
            size = 2;
        }};

        giantSiliconWall = new RegenWall("GiantSiliconWall") {{
            localizedName = "巨型硅墙";
            requirements(Category.defense, with(Items.silicon, 54));
            health = 3900;
            insulated = true;
            absorbLasers = true;
            schematicPriority = 10;
            armor = 5;
            size = 3;
            healInterval = 60f;//每秒一次
            healPercent = 0.01f;//每次回血1%
        }};

        steelWall = new Wall("ChemicalDefenseWall") {{ //钢墙
            localizedName = "钢墙";
            requirements(Category.defense, with(dawnTideItems.ceramicGlass, 6));
            health = 450;
        }};

        steelWallLarge = new Wall("ChemicalDefenseWallLarge") {{ //大型钢墙
            localizedName = "大型钢墙";
            requirements(Category.defense, with(dawnTideItems.ceramicGlass, 24));
            health = 2300;
            size = 2;
        }};

        erosionResistantWall = new Wall("ErosionResistantWall") {{ //蚀抗墙
            localizedName = "蚀抗墙";
            requirements(Category.defense, with(dawnTideItems.boundaryBreakingAlloy, 6, dawnTideItems.iron, 6));
            health = 800;
        }};

        erosionResistantWallLarge = new Wall("ErosionResistantWallLarge") {{ //大型蚀抗墙
            localizedName = "大型蚀抗墙";
            requirements(Category.defense, with(dawnTideItems.boundaryBreakingAlloy, 24, dawnTideItems.iron, 24));
            health = 3666;
            size = 2;
        }};

        giantCopperWall = new Wall("GiantCopperWall") {{ //巨型铜墙
            localizedName = "巨型铜墙";
            requirements(Category.defense, with(Items.copper, 54));
            health = 1999;
            armor = 2;
            size = 3;
        }};

        giantTitaniumWall = new Wall("GiantTitaniumWall") {{ //巨型钛墙
            localizedName = "巨型钛墙";
            requirements(Category.defense, with(Items.titanium, 54));
            health = 2600;
            armor = 3;
            size = 3;
        }};

        giantThoriumWall = new Wall("GiantThoriumWall") {{ //巨型钍墙
            localizedName = "巨型钍墙";
            requirements(Category.defense, with(Items.thorium, 54));
            health = 4555;
            armor = 4;
            size = 3;
        }};

        crystalWall = new Wall("CrystalWall") {{ //碎晶墙
            localizedName = "碎晶墙";
            requirements(Category.defense, with(dawnTideItems.quartz, 6, dawnTideItems.steel, 6));
            health = 540;
        }};

        crystalWallLarge = new Wall("CrystalWallLarge") {{ //大型碎晶墙
            localizedName = "大型碎晶墙";
            requirements(Category.defense, with(dawnTideItems.quartz, 24, dawnTideItems.steel, 24));
            health = 2900;
            size = 2;
        }};
    }
}
