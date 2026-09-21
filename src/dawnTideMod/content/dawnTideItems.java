package dawnTideMod.content;

import arc.graphics.Color;
import mindustry.type.Item;

/** 「曙光潮涌」物品注册类。 */
public class DawnTideItems{

    public static Item quartz, iron, uranium, steel, ceramicGlass, boundaryBreakingAlloy, barite,
            highExplosive, oreCrystallization, fibrousFat, fluxAlloy, blueCrystal,
            titaniumSilver, refinedTitaniumSilver;

    public static void load(){
        quartz = new Item("Quartz", Color.valueOf("F4F4F4FF")){{ // 石英
            hardness = 4;
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 1.5f;
        }};

        iron = new Item("Iron", Color.valueOf("B0BAC0FF")){{ // 铁
            hardness = 1;
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 1.3f;
        }};

        uranium = new Item("Uranium", Color.valueOf("6CB966FF")){{ // 铀
            hardness = 6;
            cost = 0.5f;
            alwaysUnlocked = false;
            radioactivity = 1.0f;
        }};

        steel = new Item("Steel", Color.valueOf("7E7473FF")){{ // 钢
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 1.6f;
        }};

        ceramicGlass = new Item("CeramicGlass", Color.valueOf("646567FF")){{ // 陶瓷玻璃
            cost = 0.5f;
            alwaysUnlocked = false;
        }};

        boundaryBreakingAlloy = new Item("BoundaryBreakingAlloy", Color.valueOf("E4E3C8FF")){{ // 临界合金
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 0.6f;
        }};

        barite = new Item("Barite", Color.valueOf("A8E1E6FF")){{ // 重晶石
            hardness = 5;
            cost = 0.5f;
            alwaysUnlocked = false;
        }};

        highExplosive = new Item("HighExplosive", Color.valueOf("FF9480FF")){{ // 高爆炸药
            cost = 0.5f;
            alwaysUnlocked = false;
            explosiveness = 3f;
        }};

        oreCrystallization = new Item("OreCrystallization", Color.valueOf("88A4FFFF")){{ // 矿石结晶
            hardness = 7;
            cost = 0.5f;
            alwaysUnlocked = false;
            radioactivity = 1.5f;
            healthScaling = 1.2f;
        }};

        fibrousFat = new Item("fibrousFat", Color.valueOf("FFA763FF")){{ // 纤维凝脂
            hardness = 9;
            cost = 0.5f;
            alwaysUnlocked = false;
            radioactivity = 1.5f;
            healthScaling = 2f;
        }};

        fluxAlloy = new Item("fluxAlloy", Color.valueOf("FFFFFFFF")){{ // 通量合金
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 2f;
        }};

        blueCrystal = new Item("BlueCrystal", Color.valueOf("FFFFFFFF")){{ // 蓝晶
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 2f;
        }};

        titaniumSilver = new Item("TitaniumSilver", Color.valueOf("FFFFFFFF")){{ // 钛银
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 2f;
        }};

        refinedTitaniumSilver = new Item("RefinedTitaniumSilver", Color.valueOf("FFFFFFFF")){{ // 精炼钛银
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 2f;
        }};
    }
}
