package dawnTideMod.content;

import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.distribution.BufferedItemBridge;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.MassDriver;
import mindustry.world.blocks.distribution.StackConveyor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.blocks.storage.Unloader;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.draw.*;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;
import static mindustry.type.ItemStack.with;
import static mindustry.world.meta.Attribute.water;


public class dawnTideBlocks {
    public static Block
            //工厂
    ExplosivesFactory,BariteFurnace,ThermonuclearFurnace,SulfideElectrolysisCell,SlagCooler,
    CrystalSynthesizer,CompositeSiliconPlant,ForgingFurnace,GlazeKiln,TitaniumSilverMeltingFurnace,
    BlueCrystalCompressor,fluxReactor,
            //矿物
    QuartzOre,IronOre,UraniumOre,TitaniumSilverOre,OreCrystallizationOre,BariteOre,
            //墙
    SiliconWall,SiliconWallLarge,GiantSiliconWall,SteelWall,SteelWallLarge,
    GiantCopperWall,GiantTitaniumWall,GiantThoriumWall,CrystalWall, CrystalWallLarge,
    ErosionResistantWall,ErosionResistantWallLarge,
    //电力
    tidalPowerGenerator,QuartzBattery,QuartzPowerNode,criticalReactor,RefinedSilverBattery,
    //辅助
    BlockSpotlight,miniWarehouse,BlockRepairer,LargemassDriver,
    //运输
    QuartzConveyor,QuartzBridge,RefinedSilverConveyor,RefinedSilverBridge,UnRefinedSilver,
    //液体运输
    //钻头
    MagnetoExplosionDrill,MagneticEnergyDrill,CrystalDrill,StorageRoom,
    //单位工厂
    NumberUpgradeUnitGenerator,MultiplierLevelUnitGenerator,MultiPowerUnitGenerator,UnboundedUnitGenerator,MultiScaleUnitReconstructionFactory;


    public static void load() {
        //工厂
        ExplosivesFactory = new GenericCrafter("ExplosivesFactory") {{//炸药加工厂
            requirements(Category.crafting, with(Items.titanium, 90, Items.silicon, 40, dawnTideItems.Steel, 70));
            alwaysUnlocked = false;
            hasPower = true;
            hasItems = true;
            craftEffect = Fx.pulverizeMedium;
            outputItems = with(Items.blastCompound, 2, dawnTideItems.HighExplosive, 1);
            consumeItems(with(Items.pyratite, 4, Items.sporePod, 4));
            consumePower(1.5f);
            health = 400;
            size = 3;
            craftTime = 90f;
            buildTime = 230f;
        }};

        BariteFurnace = new GenericCrafter("BariteFurnace") {{//重晶石熔炉
            requirements(Category.crafting, ItemStack.with(Items.graphite, 40, Items.titanium, 70, Items.silicon, 80));
            alwaysUnlocked = false;
            hasPower = true;
            hasItems = true;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.Barite, 1);
            consumeItems(with(Items.titanium, 1, Items.metaglass, 2));
            consumePower(1.1f);
            health = 500;
            size = 2;
            craftTime = 99f;
            buildTime = 230f;
        }};

        ThermonuclearFurnace = new GenericCrafter("ThermonuclearFurnace") {{//热核熔炉
            requirements(Category.crafting, ItemStack.with(dawnTideItems.Steel, 130, Items.thorium, 150, Items.surgeAlloy, 60));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.BoundaryBreakingAlloy, 1);
            consumeItems(with(Items.copper, 2, Items.titanium, 3, dawnTideItems.Steel, 2));
            consumePower(9f);
            hasPower = true;
            health = 450;
            size = 2;
            hasItems = true;
            craftTime = 60f;
            buildTime = 209f;
            itemCapacity = 20;
        }};

        SulfideElectrolysisCell = new GenericCrafter("SulfideElectrolysisCell") {{//硫化物电解室
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

        SlagCooler = new GenericCrafter("SlagCooler") {{//矿渣冷却机
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

        CrystalSynthesizer = new GenericCrafter("CrystalSynthesizer") {{//结晶合成机
            requirements(Category.crafting, ItemStack.with(Items.titanium, 70, Items.silicon, 90, Items.surgeAlloy, 120, Items.plastanium, 100));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.OreCrystallization, 3);
            consumeItems(with(dawnTideItems.Barite, 2, Items.silicon, 3));
            consumePower(6f);
            hasPower = true;
            health = 500;
            size = 2;
            hasItems = true;
            craftTime = 110f;
            buildTime = 270f;
        }};

        CompositeSiliconPlant = new GenericCrafter("CompositeSiliconPlant") {{//复合硅厂
            requirements(Category.crafting, ItemStack.with(Items.graphite, 80, dawnTideItems.Steel, 70, Items.silicon, 60));
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

        ForgingFurnace = new GenericCrafter("ForgingFurnace") {{//锻钢炉
            requirements(Category.crafting, ItemStack.with(Items.graphite, 70, Items.metaglass, 60, Items.silicon, 90));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.Steel, 3);
            consumeItems(with(Items.coal, 2, dawnTideItems.Iron, 3));
            consumePower(5f);
            hasPower = true;
            health = 400;
            size = 2;
            hasItems = true;
            craftTime = 70f;
            buildTime = 300f;
            itemCapacity = 20;
        }};

        GlazeKiln = new GenericCrafter("GlazeKiln") {{//镀瓷凿炉
            requirements(Category.crafting, ItemStack.with(Items.titanium, 70, dawnTideItems.Iron, 80, Items.silicon, 90));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.CeramicGlass, 3);
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

        TitaniumSilverMeltingFurnace = new GenericCrafter("TitaniumSilverMeltingFurnace") {{//钛银熔炉
            requirements(Category.crafting, ItemStack.with(dawnTideItems.Quartz, 90, dawnTideItems.CeramicGlass, 80, Items.silicon, 90, dawnTideItems.OreCrystallization, 50, Items.plastanium, 85));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.TitaniumSilver, 2);
            consumeItems(with(dawnTideItems.TitaniumSilver, 1, Items.graphite, 2));
            consumePower(5f);
            hasPower = true;
            health = 550;
            size = 3;
            hasItems = true;
            craftTime = 99f;
            buildTime = 270f;
            itemCapacity = 10;
        }};

        BlueCrystalCompressor = new GenericCrafter("BlueCrystalCompressor") {{//蓝晶压缩机
            requirements(Category.crafting, ItemStack.with(dawnTideItems.Quartz, 90, dawnTideItems.CeramicGlass, 80, Items.silicon, 90, dawnTideItems.OreCrystallization, 50, Items.plastanium, 85));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.BlueCrystal, 1);
            consumeItems(with(dawnTideItems.Barite, 1, Items.graphite, 2));
            consumePower(5f);
            hasPower = true;
            health = 650;
            size = 2;
            hasItems = true;
            craftTime = 55f;
            buildTime = 87f;
            itemCapacity = 10;
        }};

        fluxReactor = new GenericCrafter("fluxReactor") {{//通量反应炉
            requirements(Category.crafting, ItemStack.with(dawnTideItems.Quartz, 90, dawnTideItems.CeramicGlass, 80, Items.silicon, 90, dawnTideItems.OreCrystallization, 50, Items.plastanium, 85));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.fluxAlloy, 2);
            consumeItems(with(dawnTideItems.TitaniumSilver, 1, Items.graphite, 2));
            consumePower(5f);
            hasPower = true;
            health = 550;
            size = 3;
            hasItems = true;
            craftTime = 99f;
            buildTime = 270f;
            itemCapacity = 10;
        }};

        /*abc = new MultiCrafterBlock("multi-crafter"){{
            requirements(Category.crafting, with(Items.copper, 30, Items.lead, 20));
            health = 200;
            size = 2;
            hasRandomOutputRecipes = false;
            autoSelectRecipe = false;

            recipes.add(new Recipe("dawn-multi-silicon",
                    new IOEntry().withItems(ItemStack.with(Items.copper,3,Items.silicon,3)).withPower(1.5f),
                    new IOEntry().withItems(ItemStack.with(Items.silicon, 2)),80f).withCraftEffect(Fx.smeltsmoke).isUnlocked());

            recipes.add(new Recipe("dawn-multi-water",
                    new IOEntry().withItems(ItemStack.with(Items.lead, 2)),
                    new IOEntry().withLiquids(LiquidStack.with(Liquids.water, 0.2f)),60f
            ));
        }};*/


        //矿物

        QuartzOre = new OreBlock(dawnTideItems.Quartz) {{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        IronOre = new OreBlock(dawnTideItems.Iron) {{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        UraniumOre = new OreBlock(dawnTideItems.Uranium) {{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        TitaniumSilverOre = new OreBlock(dawnTideItems.TitaniumSilver) {{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        OreCrystallizationOre = new OreBlock(dawnTideItems.OreCrystallization) {{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        BariteOre = new OreBlock(dawnTideItems.Barite) {{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};


        //墙
        SiliconWall = new Wall("SiliconWall") {{//硅墙
            requirements(Category.defense, with(Items.silicon, 6));
            health = 650;
        }};

        SiliconWallLarge = new Wall("SiliconWallLarge") {{//大型硅墙
            requirements(Category.defense, with(Items.silicon, 24));
            health = 2600;
            size = 2;
        }};

        GiantSiliconWall = new RegenWall("GiantSiliconWall") {{//巨型硅墙（再生墙：每秒回复0.6%血量）
            requirements(Category.defense, with(Items.silicon, 54));
            health = 3900;
            insulated = true;
            absorbLasers = true;
            schematicPriority = 10;
            armor = 5;
            size = 3;
            healInterval = 60f;    // 每秒结算一次
            healPercent = 0.01f;  // 每次回复最大血量的 0.6%
        }};

        SteelWall = new Wall("ChemicalDefenseWall") {{//钢墙
            requirements(Category.defense, with(dawnTideItems.CeramicGlass, 6));
            health = 450;
        }};

        SteelWallLarge = new Wall("ChemicalDefenseWallLarge") {{//大型钢墙
            requirements(Category.defense, with(dawnTideItems.CeramicGlass, 24));
            health = 2300;
            size = 2;
        }};

        ErosionResistantWall = new Wall("ErosionResistantWall") {{//蚀抗墙
            requirements(Category.defense, with(dawnTideItems.BoundaryBreakingAlloy, 6, dawnTideItems.Iron, 6));
            health = 800;
        }};

        ErosionResistantWallLarge = new Wall("ErosionResistantWallLarge") {{//大型蚀抗墙
            requirements(Category.defense, with(dawnTideItems.BoundaryBreakingAlloy, 24, dawnTideItems.Iron, 24));
            health = 3666;
            size = 2;
        }};

        GiantCopperWall = new Wall("GiantCopperWall") {{//巨型铜墙
            requirements(Category.defense, with(Items.copper, 54));
            health = 1999;
            armor = 2;
            size = 3;
        }};

        GiantTitaniumWall = new Wall("GiantTitaniumWall") {{//巨型钛墙
            requirements(Category.defense, with(Items.titanium, 54));
            health = 2600;
            armor = 3;
            size = 3;
        }};

        GiantThoriumWall = new Wall("GiantThoriumWall") {{//巨型钍墙
            requirements(Category.defense, with(Items.thorium, 54));
            health = 4555;
            armor = 4;
            size = 3;
        }};

        CrystalWall = new Wall("CrystalWall") {{//碎晶墙
            requirements(Category.defense, with(dawnTideItems.Quartz, 6, dawnTideItems.Steel, 6));
            health = 540;
        }};

        CrystalWallLarge = new Wall("CrystalWallLarge") {{//大型碎晶墙
            requirements(Category.defense, with(dawnTideItems.Quartz, 24, dawnTideItems.Steel, 24));
            health = 2900;
            size = 2;
        }};


        //电力
        criticalReactor = new ConsumeGenerator("criticalReactor") {{//临界反应堆
            requirements(Category.power, with(dawnTideItems.Steel, 250, Items.thorium, 150, Items.graphite, 270, Items.silicon, 180, Items.lead, 300));
            powerProduction = 25f;
            itemDuration = 270f;
            hasLiquids = true;
            hasItems = true;
            size = 3;
            ambientSound = Sounds.loopDifferential;
            generateEffect = Fx.generatespark;
            ambientSoundVolume = 0.12f;
            consumeItem(dawnTideItems.HighExplosive);
            consumeLiquid(Liquids.cryofluid, 0.1f);
            //drawer = new DrawMulti(new DrawDefault(), new DrawWarmupRegion(), new DrawLiquidRegion());
        }};

        tidalPowerGenerator = new ThermalGenerator("tidalPowerGenerator") {{//潮汐发电机
            requirements(Category.power, with(dawnTideItems.CeramicGlass, 50, Items.silicon, 40, dawnTideItems.Iron, 60, Items.titanium, 70));
            powerProduction = 1.5f;
            generateEffect = Fx.redgeneratespark;
            effectChance = 0.011f;
            size = 2;
            floating = true;
            ambientSound = Sounds.loopHum;
            ambientSoundVolume = 0.06f;
            attribute = water;
        }};

        QuartzPowerNode = new PowerNode("QuartzPowerNode") {{//石英电力节点
            requirements(Category.power, with(dawnTideItems.Quartz, 10, Items.titanium, 10, Items.graphite, 10));
            maxNodes = 30;
            laserRange = 30;
            underBullets = true;
            crushFragile = true;
        }};

        QuartzBattery = new Battery("QuartzBattery") {{//石英电池
            requirements(Category.power, with(dawnTideItems.Quartz, 76, Items.lead, 100, Items.silicon, 120));
            size = 3;
            consumePowerBuffered(250000f);
            baseExplosiveness = 12f;
        }};

        RefinedSilverBattery = new Battery("RefinedSilverBattery") {{//石英电池
            requirements(Category.power, with(dawnTideItems.RefinedTitaniumSilver,100,dawnTideItems.Uranium,150,Items.plastanium,70,dawnTideItems.Steel,135));
            size = 4;
            consumePowerBuffered(5000000f);
            baseExplosiveness = 23f;
        }};


        //辅助
        BlockSpotlight = new LightBlock("BlockSpotlight") {{//区块探照灯
            requirements(Category.effect, BuildVisibility.lightingOnly, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            brightness = 0.75f;
            radius = 140f;
            consumePower(0.05f);
            size = 3;
        }};

        LargemassDriver = new MassDriver("LargemassDriver"){{
            requirements(Category.distribution, with(Items.titanium, 125, Items.silicon, 75, Items.lead, 125, Items.thorium, 50));
            size = 4;
            itemCapacity = 240;
            reload = 150f;
            range = 600f;
            consumePower(2.5f);
        }};

        BlockRepairer = new MendProjector("BlockRepairer") {{//区块修复器
            requirements(Category.effect, with(Items.lead, 100, Items.titanium, 25, Items.silicon, 40, Items.copper, 50));
            consumePower(1.5f);
            size = 3;
            reload = 210f;
            range = 210f;
            healPercent = 8f;
            phaseBoost = 11f;
            scaledHealth = 80;
            consumeItem(Items.phaseFabric).boost();
            size = 3;
        }};

        miniWarehouse = new StorageBlock("miniWarehouse") {{//微型仓库
            requirements(Category.effect, with(Items.titanium, 250, Items.thorium, 125));
            size = 1;
            itemCapacity = 100;
            scaledHealth = 55;
        }};

        StorageRoom = new StorageBlock("StorageRoom") {{//储藏室
            requirements(Category.effect, with(dawnTideItems.RefinedTitaniumSilver,200, Items.thorium,250,dawnTideItems.Uranium,150,dawnTideItems.Steel,100));
            size = 4;
            itemCapacity = 5000;
            scaledHealth = 55;
        }};


        //运输
        QuartzConveyor = new Conveyor("QuartzConveyor") {{
            requirements(Category.distribution, with(dawnTideItems.Quartz, 1));
            health = 250;
            speed = 0.25f;
            displayedSpeed = 25f;
            researchCost = with(dawnTideItems.Quartz, 500);
        }};

        QuartzBridge = new BufferedItemBridge("QuartzBridge") {{
            requirements(Category.distribution, with(dawnTideItems.Quartz, 3, Items.lead, 6));
            fadeIn = moveArrows = false;
            range = 6;
            speed = 74f;
            arrowSpacing = 8f;
            bufferCapacity = 14;
            crushFragile = true;
        }};

        RefinedSilverBridge = new BufferedItemBridge("RefinedSilverBridge") {{
            requirements(Category.distribution, with(dawnTideItems.Quartz, 3, Items.lead, 6));
            fadeIn = moveArrows = false;
            range = 15;
            speed = 74f;
            arrowSpacing = 17f;
            bufferCapacity = 25;
            crushFragile = true;
        }};

        RefinedSilverConveyor = new StackConveyor("RefinedSilverConveyor"){{//钛银带
            requirements(Category.distribution, with(dawnTideItems.TitaniumSilver,1,Items.silicon,1));
            health = 150;
            speed = 4f / 60f;
            itemCapacity = 30;
        }};

        UnRefinedSilver = new Unloader("UnRefinedSilver"){{
            requirements(Category.distribution, with(dawnTideItems.TitaniumSilver,20,Items.thorium,25));
            speed = 300f / 11f;
            group = BlockGroup.transportation;
        }};


        //Production
        MagnetoExplosionDrill = new Drill("MagnetoExplosionDrill") {{
            requirements(Category.production, with(dawnTideItems.Steel, 100, dawnTideItems.BoundaryBreakingAlloy, 30, Items.silicon, 80, Items.thorium, 120));
            drillTime = 99;
            size = 4;
            drawRim = true;
            hasPower = true;
            tier = 5;
            updateEffect = Fx.pulverizeRed;
            updateEffectChance = 0.03f;
            drillEffect = Fx.mineHuge;
            health = 300;
            rotateSpeed = 9f;
            warmupSpeed = 0.01f;
            itemCapacity = 20;
            liquidBoostIntensity = 1.8f;
            consumePower(5f);
            consumeLiquid(Liquids.cryofluid, 0.1f).boost();
        }};

        MagneticEnergyDrill = new Drill("MagneticEnergyDrill") {{
            requirements(Category.production, with(dawnTideItems.Steel, 100, dawnTideItems.BoundaryBreakingAlloy, 30, Items.silicon, 80, Items.thorium, 120));
            drillTime = 115;
            health = 450;
            size = 3;
            drawRim = true;
            hasPower = true;
            tier = 4;
            updateEffect = Fx.pulverizeRed;
            updateEffectChance = 0.03f;
            drillEffect = Fx.mineHuge;
            rotateSpeed = 4f;
            warmupSpeed = 0.01f;
            itemCapacity = 10;
            liquidBoostIntensity = 1.8f;
            consumePower(1.5f);
            consumeLiquid(Liquids.slag, 0.1f).boost();
        }};

        CrystalDrill = new Drill("CrystalDrill") {{
            requirements(Category.production, with(dawnTideItems.Steel, 100, dawnTideItems.BoundaryBreakingAlloy, 30, Items.silicon, 80, Items.thorium, 120));
            drillTime = 140;
            health = 320;
            size = 2;
            drawRim = true;
            hasPower = true;
            tier = 3;
            updateEffect = Fx.pulverizeRed;
            updateEffectChance = 0.03f;
            drillEffect = Fx.mineHuge;
            rotateSpeed = 2f;
            warmupSpeed = 0.01f;
            itemCapacity = 10;
            liquidBoostIntensity = 1.8f;
            consumePower(1.5f);
            consumeLiquid(Liquids.water, 0.1f).boost();
        }};


        //Units
        NumberUpgradeUnitGenerator = new UnitFactory("NumberUpgradeUnitGenerator") {{//数增级单位生成器
            hasPower = true;
            requirements(Category.units, with(Items.copper, 200, Items.lead, 150, Items.silicon, 100));
            plans = Seq.with(
                    new UnitPlan(UnitTypes.mace, 1500f, with(Items.silicon, 50, Items.lead, 10, Items.graphite, 40)),
                    new UnitPlan(UnitTypes.poly, 2700f, with(Items.silicon, 70, Items.lead, 15, Items.graphite, 40)),
                    new UnitPlan(UnitTypes.pulsar, 3000f, with(Items.silicon, 70, Items.lead, 20, Items.graphite, 40, Items.titanium, 20)),
                    new UnitPlan(UnitTypes.atrax, 1200f, with(Items.silicon, 50, Items.coal, 10, Items.graphite, 40)),
                    new UnitPlan(UnitTypes.horizon, 1500f, with(Items.silicon, 55, Items.graphite, 40)),
                    new UnitPlan(UnitTypes.minke, 3300f, with(Items.silicon, 60, Items.graphite, 40, Items.metaglass, 35)),
                    new UnitPlan(UnitTypes.oxynoe, 2700f, with(Items.silicon, 55, Items.graphite, 40, Items.titanium, 20)));
            size = 3;
            consumePower(3f);
            researchCostMultiplier = 3f;
        }};

        MultiplierLevelUnitGenerator = new UnitFactory("MultiplierLevelUnitGenerator") {{//倍乘级
            health = 3130;
            size = 5;
            hasPower = true;
            hasLiquids = true;
            buildTime = 600;
            consumePower(7f);
            consumeLiquid(Liquids.water, 1);
            requirements(Category.units, with(Items.titanium, 350, Items.lead, 650, Items.silicon, 450, dawnTideItems.OreCrystallization, 350, Items.thorium, 650));
            plans = Seq.with(
                    new UnitPlan(UnitTypes.fortress, 1800f, with(Items.silicon, 150, dawnTideItems.Barite, 50)),
                    new UnitPlan(UnitTypes.quasar, 1800f, with(Items.silicon, 150, dawnTideItems.Barite, 50)),
                    new UnitPlan(UnitTypes.spiroct, 1800f, with(Items.silicon, 150, dawnTideItems.Barite, 50)),
                    new UnitPlan(UnitTypes.zenith, 1800f, with(Items.silicon, 150, dawnTideItems.Barite, 50)),
                    new UnitPlan(UnitTypes.mega, 1800f, with(Items.silicon, 150, dawnTideItems.Barite, 50)),
                    new UnitPlan(UnitTypes.bryde, 1800f, with(Items.silicon, 150, dawnTideItems.Barite, 50)),
                    new UnitPlan(UnitTypes.cyerce, 1800f, with(Items.silicon, 150, dawnTideItems.Barite, 50)));
        }};

        MultiPowerUnitGenerator = new UnitFactory("MultiPowerUnitGenerator") {{//多幂级
            health = 4400;
            size = 5;
            hasPower = true;
            hasLiquids = true;
            buildTime = 600;
            consumePower(20f);
            consumeLiquid(dawnTideLiquids.MicroscaleFluid, 5);
            requirements(Category.units, with(Items.silicon,450,dawnTideItems.OreCrystallization,550,dawnTideItems.RefinedTitaniumSilver,450,Items.plastanium,500,Items.lead,2500,Items.phaseFabric,400));
            plans = Seq.with(
                    new UnitFactory.UnitPlan(UnitTypes.scepter, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.vela, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.arkyid, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.antumbra, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.quad, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.sei, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.aegires, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)));
        }};

        UnboundedUnitGenerator = new UnitFactory("UnboundedUnitGenerator") {{//无量级
            health = 5500;
            size = 5;
            hasPower = true;
            hasLiquids = true;
            buildTime = 6000;
            consumePower(34f);
            consumeLiquid(dawnTideLiquids.MicroscaleFluid, 5);
            requirements(Category.units, with(Items.silicon,450,dawnTideItems.OreCrystallization,550,dawnTideItems.RefinedTitaniumSilver,450,Items.plastanium,500,Items.lead,2500,Items.phaseFabric,400));
            plans = Seq.with(
                    new UnitFactory.UnitPlan(UnitTypes.eclipse, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.toxopid, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.reign, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.omura, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.oct, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.corvus, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)),
                    new UnitFactory.UnitPlan(UnitTypes.navanax, 4800f, with(Items.silicon,550, dawnTideItems.TitaniumSilver,500,dawnTideItems.Steel,450)));
        }};

        /*heatCrafter = new AttributeMultiCrafterBlock("heat-crafter"){{
            requirements(Category.crafting, with(Items.titanium, 40));
            size = 2;
            attribute = Attribute.heat;   // 吃什么属性（Attribute.heat/oil/water...）
            baseEfficiency = 1f;          // 底效率
            boostScale = 1f;              // 每点属性加多少倍率
            maxBoost = 3f;                // 加成上限

            recipes.add(new Recipe("dawn-heat-smelt",
                    new IOEntry().withItems(ItemStack.with(Items.scrap, 2)),
                    new IOEntry().withItems(ItemStack.with(Items.titanium, 1)),
                    90f
            ));
        }};*/
    }
}