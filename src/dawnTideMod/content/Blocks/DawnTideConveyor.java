package dawnTideMod.content.Blocks;

import dawnTideMod.TideClean.world.LiquidUnloader;
import dawnTideMod.content.dawnTideItems;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.BufferedItemBridge;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.StackConveyor;
import mindustry.world.blocks.storage.Unloader;
import mindustry.world.meta.BlockGroup;

import static mindustry.type.ItemStack.with;

public class DawnTideConveyor {
    public static Block
            quartzConveyor,quartzBridge,refinedSilverConveyor,refinedSilverBridge,unRefinedSilver,liquidUnloader;

    public static void load(){
        quartzConveyor = new Conveyor("QuartzConveyor") {{
            localizedName = "石英传送带";
            requirements(Category.distribution, with(dawnTideItems.quartz, 1));
            health = 250;
            speed = 0.25f;
            displayedSpeed = 25f;
            researchCost = with(dawnTideItems.quartz, 500);
        }};

        quartzBridge = new BufferedItemBridge("QuartzBridge") {{
            localizedName = "石英传送带桥";
            requirements(Category.distribution, with(dawnTideItems.quartz, 3, Items.lead, 6));
            fadeIn = moveArrows = false;
            range = 6;
            speed = 74f;
            arrowSpacing = 8f;
            bufferCapacity = 14;
            crushFragile = true;
        }};

        refinedSilverBridge = new BufferedItemBridge("RefinedSilverBridge") {{
            localizedName = "钛银传送带桥";
            requirements(Category.distribution, with(dawnTideItems.quartz, 3, Items.lead, 6));
            fadeIn = moveArrows = false;
            range = 15;
            speed = 74f;
            arrowSpacing = 17f;
            bufferCapacity = 25;
            crushFragile = true;
        }};

        refinedSilverConveyor = new StackConveyor("RefinedSilverConveyor"){{ //钛银带
            localizedName = "钛银传送带";
            requirements(Category.distribution, with(dawnTideItems.titaniumSilver, 1, Items.silicon, 1));
            health = 150;
            speed = 4f / 60f;
            itemCapacity = 30;
        }};

        unRefinedSilver = new Unloader("UnRefinedSilver"){{
            localizedName = "钛银装卸器";
            requirements(Category.distribution, with(dawnTideItems.titaniumSilver, 20, Items.thorium, 25));
            speed = 300f / 11f;
            group = BlockGroup.transportation;
        }};

        liquidUnloader = new LiquidUnloader("LiquidUnloader"){{
            localizedName = "液体装卸器";
            description = "从相邻建筑取出液体并注入另一个相邻建筑，点击可指定液体，不指定则自动轮询。";
            requirements(Category.liquid, with(
                    dawnTideItems.steel, 40, Items.metaglass, 30, Items.silicon, 25));
            alwaysUnlocked = true;
        }};
    }
}
