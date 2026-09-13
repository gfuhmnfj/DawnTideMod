package dawnTideMod.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class dawnTideItems {
    public static Item
            Quartz,Iron,Uranium,Steel,CeramicGlass,BoundaryBreakingAlloy,
            Barite,HighExplosive,OreCrystallization,fibrousFat;

    public static void load(){
        Quartz = new Item("Quartz", Color.valueOf("F4F4F4FF")){{//石英
            hardness = 4;
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 1.5f;
        }};

        Iron = new Item("Iron", Color.valueOf("B0BAC0FF")){{//铁
                hardness = 1;
                cost = 0.5f;
                alwaysUnlocked = false;
                healthScaling = 1.3f;
            }};

        Uranium = new Item("Uranium", Color.valueOf("6CB966FF")){{//铀
                hardness = 6;
                cost = 0.5f;
                alwaysUnlocked = false;
                radioactivity = 1.0f;
            }};

        Steel = new Item("Steel", Color.valueOf("7E7473FF")){{//钢
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 1.6f;
        }};

        CeramicGlass = new Item("CeramicGlass", Color.valueOf("646567FF")){{//陶瓷玻璃
            cost = 0.5f;
            alwaysUnlocked = false;
        }};

        BoundaryBreakingAlloy = new Item("BoundaryBreakingAlloy", Color.valueOf("E4E3C8FF")){{//临界合金
            cost = 0.5f;
            alwaysUnlocked = false;
            healthScaling = 0.6f;
        }};

        Barite = new Item("Barite", Color.valueOf("A8E1E6FF")){{//重晶石
            hardness = 5;
            cost = 0.5f;
            alwaysUnlocked = false;
        }};

        HighExplosive = new Item("HighExplosive", Color.valueOf("FF9480FF")){{//高爆炸药
            cost = 0.5f;
            alwaysUnlocked = false;
            explosiveness = 3f;
        }};

        OreCrystallization = new Item("OreCrystallization", Color.valueOf("88A4FFFF")){{//矿石结晶
            hardness = 7;
            cost = 0.5f;
            alwaysUnlocked = false;
            radioactivity = 1.5f;
            healthScaling = 1.2f;
        }};
        fibrousFat = new Item("fibrousFat", Color.valueOf("FFA763FF")){{//纤维凝脂
            hardness = 9;
            cost = 0.5f;
            alwaysUnlocked = false;
            radioactivity = 1.5f; //放射性
            healthScaling = 2f;
        }};
    }
}