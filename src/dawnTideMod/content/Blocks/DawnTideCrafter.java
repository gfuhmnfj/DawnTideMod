package dawnTideMod.content.Blocks;

import dawnTideMod.content.dawnTideItems;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawLiquidTile;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawRegion;

import static mindustry.type.ItemStack.with;

public class DawnTideCrafter {
    public static Block
            explosivesFactory, bariteFurnace, thermonuclearFurnace, sulfideElectrolysisCell, slagCooler,
            crystalSynthesizer, compositeSiliconPlant, forgingFurnace, glazeKiln, titaniumSilverMeltingFurnace,
            blueCrystalCompressor, fluxReactor;

    public static void load(){
        explosivesFactory = new GenericCrafter("ExplosivesFactory") {{
            localizedName = "炸药加工厂";
            requirements(Category.crafting, with(Items.titanium, 90, Items.silicon, 40, dawnTideItems.steel, 70));
            alwaysUnlocked = false;
            hasPower = true;
            hasItems = true;
            craftEffect = Fx.pulverizeMedium;
            outputItems = with(Items.blastCompound, 2, dawnTideItems.highExplosive, 1);
            consumeItems(with(Items.pyratite, 4, Items.sporePod, 4));
            consumePower(1.5f);
            health = 400;
            size = 3;
            craftTime = 90f;
            buildTime = 230f;
        }};

        bariteFurnace = new GenericCrafter("BariteFurnace") {{ //重晶石熔炉
            localizedName = "重晶石熔炉";
            requirements(Category.crafting, ItemStack.with(Items.graphite, 40, Items.titanium, 70, Items.silicon, 80));
            alwaysUnlocked = false;
            hasPower = true;
            hasItems = true;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.barite, 1);
            consumeItems(with(Items.titanium, 1, Items.metaglass, 2));
            consumePower(1.1f);
            health = 500;
            size = 2;
            craftTime = 99f;
            buildTime = 230f;
        }};

        thermonuclearFurnace = new GenericCrafter("ThermonuclearFurnace") {{ //热核熔炉
            localizedName = "热河熔炉";
            requirements(Category.crafting, ItemStack.with(dawnTideItems.steel, 130, Items.thorium, 150, Items.surgeAlloy, 60));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.boundaryBreakingAlloy, 1);
            consumeItems(with(Items.copper, 2, Items.titanium, 3, dawnTideItems.steel, 2));
            consumePower(9f);
            hasPower = true;
            health = 450;
            size = 2;
            hasItems = true;
            craftTime = 60f;
            buildTime = 209f;
            itemCapacity = 20;
        }};

        sulfideElectrolysisCell = new GenericCrafter("SulfideElectrolysisCell") {{ //硫化物电解室
            localizedName = "硫化物电解室";
            requirements(Category.crafting, ItemStack.with(Items.titanium, 70, Items.silicon, 85, Items.copper, 120));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(Items.pyratite, 3);
            consumeItems(with(Items.lead, 3, Items.scrap, 4));
            consumePower(2.5f);
            hasPower = true;
            health = 450;
            size = 3;
            hasItems = true;
            craftTime = 99f;
            buildTime = 250f;
        }};

        slagCooler = new GenericCrafter("SlagCooler") {{ //矿渣冷却机
            localizedName = "矿渣冷却机";
            requirements(Category.crafting, ItemStack.with(Items.plastanium, 40, Items.titanium, 60, Items.silicon, 70));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputLiquid = new LiquidStack(Liquids.cryofluid, 1f);
            consumeLiquid(Liquids.slag, 0.5f);
            consumePower(7f);
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.slag), new DrawLiquidTile(Liquids.cryofluid) {{
                drawLiquidLight = true;
            }}, new DrawDefault());
            liquidCapacity = 60f;
            hasPower = true;
            outputsLiquid = true;
            health = 340;
            size = 3;
            hasLiquids = true;
            craftTime = 60f;
            buildTime = 230f;
            lightLiquid = Liquids.cryofluid;
        }};

        crystalSynthesizer = new GenericCrafter("CrystalSynthesizer") {{ //结晶合成机
            localizedName = "结晶合成机";
            requirements(Category.crafting, ItemStack.with(Items.titanium, 70, Items.silicon, 90, Items.surgeAlloy, 120, Items.plastanium, 100));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.oreCrystallization, 3);
            consumeItems(with(dawnTideItems.barite, 2, Items.silicon, 3));
            consumePower(6f);
            hasPower = true;
            health = 500;
            size = 2;
            hasItems = true;
            craftTime = 110f;
            buildTime = 270f;
        }};

        compositeSiliconPlant = new GenericCrafter("CompositeSiliconPlant") {{ //复合硅厂
            localizedName = "复合硅厂";
            requirements(Category.crafting, ItemStack.with(Items.graphite, 80, dawnTideItems.steel, 70, Items.silicon, 60));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(Items.silicon, 5);
            consumeItems(with(Items.sand, 5, Items.pyratite, 2, Items.graphite, 3));
            consumePower(7f);
            hasPower = true;
            health = 400;
            size = 4;
            hasItems = true;
            craftTime = 78f;
            buildTime = 250f;
            itemCapacity = 20;
        }};

        forgingFurnace = new GenericCrafter("ForgingFurnace") {{ //锻钢炉
            localizedName = "锻钢炉";
            requirements(Category.crafting, ItemStack.with(Items.graphite, 70, Items.metaglass, 60, Items.silicon, 90));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.steel, 3);
            consumeItems(with(Items.coal, 2, dawnTideItems.iron, 3));
            consumePower(5f);
            hasPower = true;
            health = 400;
            size = 2;
            hasItems = true;
            craftTime = 70f;
            buildTime = 300f;
            itemCapacity = 20;
        }};

        glazeKiln = new GenericCrafter("GlazeKiln") {{ //镀瓷凿炉
            localizedName = "镀瓷凿炉";
            requirements(Category.crafting, ItemStack.with(Items.titanium, 70, dawnTideItems.iron, 80, Items.silicon, 90));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.ceramicGlass, 3);
            consumeItems(with(Items.silicon, 3, Items.metaglass, 4));
            consumePower(4f);
            hasPower = true;
            health = 550;
            size = 3;
            hasItems = true;
            craftTime = 84f;
            buildTime = 230f;
            itemCapacity = 20;
        }};

        titaniumSilverMeltingFurnace = new GenericCrafter("TitaniumSilverMeltingFurnace") {{ //钛银熔炉
            localizedName = "钛银熔炉";
            requirements(Category.crafting, ItemStack.with(dawnTideItems.quartz, 90, dawnTideItems.ceramicGlass, 80, Items.silicon, 90, dawnTideItems.oreCrystallization, 50, Items.plastanium, 85));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.titaniumSilver, 2);
            consumeItems(with(dawnTideItems.titaniumSilver, 1, Items.graphite, 2));
            consumePower(5f);
            hasPower = true;
            health = 550;
            size = 3;
            hasItems = true;
            craftTime = 99f;
            buildTime = 270f;
            itemCapacity = 10;
        }};

        blueCrystalCompressor = new GenericCrafter("BlueCrystalCompressor") {{ //蓝晶压缩机
            localizedName = "蓝晶压缩机";
            requirements(Category.crafting, ItemStack.with(dawnTideItems.quartz, 90, dawnTideItems.ceramicGlass, 80, Items.silicon, 90, dawnTideItems.oreCrystallization, 50, Items.plastanium, 85));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.blueCrystal, 1);
            consumeItems(with(dawnTideItems.barite, 1, Items.graphite, 2));
            consumePower(5f);
            hasPower = true;
            health = 650;
            size = 2;
            hasItems = true;
            craftTime = 55f;
            buildTime = 87f;
            itemCapacity = 10;
        }};

        fluxReactor = new GenericCrafter("fluxReactor") {{ //通量反应炉
            localizedName = "通量反应炉";
            requirements(Category.crafting, ItemStack.with(dawnTideItems.quartz, 90, dawnTideItems.ceramicGlass, 80, Items.silicon, 90, dawnTideItems.oreCrystallization, 50, Items.plastanium, 85));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.fluxAlloy, 2);
            consumeItems(with(dawnTideItems.titaniumSilver, 1, Items.graphite, 2));
            consumePower(5f);
            hasPower = true;
            health = 550;
            size = 4;
            hasItems = true;
            craftTime = 99f;
            buildTime = 270f;
            itemCapacity = 10;
        }};
    }
}