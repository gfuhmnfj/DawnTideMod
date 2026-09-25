package dawnTideMod.content;

import arc.graphics.Color;
import mindustry.type.Item;
public class DawnTideItems{

    public static Item quartz, iron, uranium, steel, ceramicGlass, boundaryBreakingAlloy, barite,
            highExplosive, oreCrystallization, fibrousFat, fluxAlloy, blueCrystal,
            titaniumSilver, refinedTitaniumSilver;

    public static void load(){
        quartz = new Item("Quartz", Color.valueOf("F4F4F4FF")){{
            localizedName = "石英";
            hardness = 4;
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 1.5f;
        }};

        iron = new Item("Iron", Color.valueOf("B0BAC0FF")){{
            localizedName = "铁";
            hardness = 1;
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 1.3f;
        }};

        uranium = new Item("Uranium", Color.valueOf("6CB966FF")){{
            localizedName = "铀";
            hardness = 6;
            cost = 0.5f;
            alwaysUnlocked = false;
            radioactivity = 1.0f;
        }};

        steel = new Item("Steel", Color.valueOf("7E7473FF")){{
            localizedName = "钢";
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 1.6f;
        }};

        ceramicGlass = new Item("CeramicGlass", Color.valueOf("646567FF")){{
            localizedName = "陶瓷玻璃";
            cost = 0.5f;
            alwaysUnlocked = false;
        }};

        boundaryBreakingAlloy = new Item("BoundaryBreakingAlloy", Color.valueOf("E4E3C8FF")){{
            localizedName = "临界合金";
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 0.6f;
        }};

        barite = new Item("Barite", Color.valueOf("A8E1E6FF")){{
            localizedName = "重晶石";
            hardness = 5;
            cost = 0.5f;
            alwaysUnlocked = false;
        }};

        highExplosive = new Item("HighExplosive", Color.valueOf("FF9480FF")){{
            localizedName = "高爆炸药";
            cost = 0.5f;
            alwaysUnlocked = false;
            explosiveness = 3f;
        }};

        oreCrystallization = new Item("OreCrystallization", Color.valueOf("88A4FFFF")){{
            localizedName = "矿石结晶";
            hardness = 7;
            cost = 0.5f;
            alwaysUnlocked = false;
            radioactivity = 1.5f;
            healthScaling = 1.2f;
        }};

        fibrousFat = new Item("fibrousFat", Color.valueOf("FFA763FF")){{
            localizedName = "纤维凝脂";
            hardness = 9;
            cost = 0.5f;
            alwaysUnlocked = false;
            radioactivity = 1.5f;
            healthScaling = 2f;
        }};

        fluxAlloy = new Item("fluxAlloy", Color.valueOf("FFFFFFFF")){{
            localizedName = "通量合金";
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 2f;
        }};

        blueCrystal = new Item("BlueCrystal", Color.valueOf("FFFFFFFF")){{
            localizedName = "蓝晶";
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 2f;
        }};

        titaniumSilver = new Item("TitaniumSilver", Color.valueOf("FFFFFFFF")){{
            localizedName = "钛银";
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 2f;
        }};

        refinedTitaniumSilver = new Item("RefinedTitaniumSilver", Color.valueOf("FFFFFFFF")){{
            localizedName = "精炼钛银";
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 2f;
        }};
    }
}
