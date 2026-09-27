package dawnTideMod.content.Blocks;

import dawnTideMod.TideClean.multicrafter.MultiCrafterBlock;
import dawnTideMod.TideClean.multicrafter.type.Recipe;
import dawnTideMod.TideClean.multicrafter.world.ColdSourceBlock;
import dawnTideMod.content.dawnTideItems;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.liquid.LiquidBridge;
import mindustry.world.blocks.liquid.LiquidRouter;
import mindustry.world.blocks.production.Pump;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawPumpLiquid;

import static mindustry.type.ItemStack.with;
public class DawnTideBlocks{

    public static Block
    packingMachine,UnpackingStation,coldGenerator,
    quartzConduit,quartzBridgeConduit,quartzTank,quartzPump;

    public static void load(){

        packingMachine = new MultiCrafterBlock("packingMachine"){{
            localizedName = "打包机";
            requirements(Category.crafting, with(dawnTideItems.quartz, 30, Items.lead, 20));
            health = 200;
            size = 2;
            hasRandomOutputRecipes = false;
            autoSelectRecipe = false;

            recipes.add(new Recipe("waterBottle"){{
                consumes(
                    dawnTideItems.iron, 2,
                    Liquids.water, 1f,
                    power(1f),
                    heat(10f),
                    cold(10f)
                );
                craftTime = 60f;
                outputItems(with(dawnTideItems.waterBottle, 1));
                isUnlocked();
            }});
        }};

        coldGenerator = new ColdSourceBlock("冷量机"){{
            localizedName = "冷量机";
            description = "通电产生冷量，向相邻需要冷量的工厂供冷";
            requirements(Category.crafting, with(dawnTideItems.quartz, 15, Items.silicon, 20, Items.lead, 25));
            health = 180;
            size = 2;
            consumePower(2f);
            coldOutput = 10f;
        }};

        // ===== 星域·液体运输（原 content/液体 JSON 转 Java）=====

        quartzConduit = new Conduit("石英导管"){{
            localizedName = "石英导管";
            requirements(Category.liquid, with(dawnTideItems.quartz, 2));
            health = 250;
            liquidCapacity = 70f;
            liquidPressure = 3.7f;
            buildCostMultiplier = 7.5f;
            bridgeReplacement = Blocks.phaseConduit;
        }};

        quartzBridgeConduit = new LiquidBridge("石英导管液桥"){{
            localizedName = "石英导管液桥";
            requirements(Category.liquid, with(dawnTideItems.quartz, 4, Items.silicon, 6));
            health = 300;
            armor = 3f;
            range = 8;
            liquidCapacity = 150f;
            arrowSpacing = 8f;
            arrowOffset = 4f;
            arrowTimeScl = 12f;
            bridgeWidth = 3f;
        }};

        quartzTank = new LiquidRouter("石英储液罐"){{
            localizedName = "石英储液罐";
            requirements(Category.liquid, with(dawnTideItems.quartz, 70, Items.silicon, 60, dawnTideItems.ceramicGlass, 40));
            health = 1000;
            size = 3;
            liquidCapacity = 4000f;
            absorbLasers = true;
        }};

        quartzPump = new Pump("石英汲液泵"){{
            localizedName = "石英汲液泵";
            requirements(Category.liquid, with(dawnTideItems.quartz, 60, Items.silicon, 70, Items.plastanium, 60, Items.thorium, 120));
            drawer = new DrawMulti(new DrawPumpLiquid(), new DrawDefault());
            squareSprite = false;
            health = 210;
            size = 3;
            liquidCapacity = 30f;
            hasLiquids = true;
            pumpAmount = 23f;
        }};

    }
}
