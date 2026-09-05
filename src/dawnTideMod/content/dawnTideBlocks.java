package dawnTideMod.content;

import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawLiquidTile;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawRegion;
import mindustry.world.meta.BuildVisibility;
import static mindustry.type.ItemStack.with;
import static mindustry.world.meta.Attribute.water;


public class dawnTideBlocks {
    public static Block
            //crafter
    ExplosivesFactory,BariteFurnace,ThermonuclearFurnace,SulfideElectrolysisCell,SlagCooler,
    CrystalSynthesizer,CompositeSiliconPlant,ForgingFurnace,GlazeKiln,
            //ores
    oreQuartz,oreIron,oreUranium,
            //walls
    SiliconWall,SiliconWallLarge,GiantSiliconWall,ChemicalDefenseWall,ChemicalDefenseWallLarge,
    GiantChemicalDefenseWall,GiantCopperWall,GiantTitaniumWall,GiantThoriumWall,CrystalWall,
    CrystalWallLarge,ErosionResistantWall,
    //power
    tidalPowerGenerator,QuartzBattery,QuartzPowerNode,criticalReactor,
    //sandbox
    BlockSpotlight,
    //storage
    miniWarehouse,
    //defense
    BlockRepairer;

    public static void load(){
        ExplosivesFactory = new GenericCrafter("ExplosivesFactory"){{//炸药加工厂
            requirements(Category.crafting, with(Items.titanium,90,Items.silicon,40, dawnTideItems.Steel,70));
             alwaysUnlocked = false;
             hasPower = true;
             hasItems = true;
             craftEffect = Fx.pulverizeMedium;
             outputItems = with(Items.blastCompound,2, dawnTideItems.HighExplosive, 1);
             consumeItems(with(Items.pyratite,4,Items.sporePod,4));
             consumePower(1.5f);
             health = 400;
             size = 3;
             craftTime = 90f;
             buildTime = 230f;
        }};

        BariteFurnace = new GenericCrafter("BariteFurnace"){{//重晶石熔炉
            requirements(Category.crafting, ItemStack.with(Items.graphite,40,Items.titanium,70,Items.silicon,80));
            alwaysUnlocked = false;
            hasPower = true;
            hasItems = true;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.Barite,1);
            consumeItems(with(Items.titanium,1,Items.metaglass,2));
            consumePower(1.1f);
            health = 500;
            size = 2;
            craftTime = 99f;
            buildTime = 230f;
        }};

        ThermonuclearFurnace = new GenericCrafter("ThermonuclearFurnace"){{//热核熔炉
            requirements(Category.crafting, ItemStack.with(dawnTideItems.Steel,130,Items.thorium,150,Items.surgeAlloy,60));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.BoundaryBreakingAlloy,1);
            consumeItems(with(Items.copper,2,Items.titanium,3, dawnTideItems.Steel,2));
            consumePower(9f);
            hasPower = true;
            health = 450;
            size = 2;
            hasItems = true;
            craftTime = 60f;
            buildTime = 209f;
            itemCapacity = 20;
        }};

        SulfideElectrolysisCell = new GenericCrafter("SulfideElectrolysisCell"){{//硫化物电解室
            requirements(Category.crafting, ItemStack.with(Items.titanium,70,Items.silicon,85,Items.copper,120));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(Items.pyratite,3);
            consumeItems(with(Items.lead,3,Items.scrap,4));
            consumePower(2.5f);
            hasPower = true;
            health = 450;
            size = 3;
            hasItems = true;
            craftTime = 99f;
            buildTime = 250f;
        }};

        SlagCooler = new GenericCrafter("SlagCooler"){{//矿渣冷却机
            requirements(Category.crafting, ItemStack.with(Items.plastanium,40,Items.titanium,60,Items.silicon,70));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputLiquid = new LiquidStack(Liquids.cryofluid, 1f);
            consumeLiquid(Liquids.slag,0.5f);
            consumePower(7f);
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.slag), new DrawLiquidTile(Liquids.cryofluid){{drawLiquidLight = true;}}, new DrawDefault());
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

        CrystalSynthesizer = new GenericCrafter("CrystalSynthesizer"){{//结晶合成机
            requirements(Category.crafting, ItemStack.with(Items.titanium,70,Items.silicon,90,Items.surgeAlloy,120,Items.plastanium,100));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.OreCrystallization,3);
            consumeItems(with(dawnTideItems.Barite,2,Items.silicon,3));
            consumePower(6f);
            hasPower = true;
            health = 500;
            size = 2;
            hasItems = true;
            craftTime = 110f;
            buildTime = 270f;
        }};

        CompositeSiliconPlant = new GenericCrafter("CompositeSiliconPlant"){{//复合硅厂
            requirements(Category.crafting, ItemStack.with(Items.graphite,80, dawnTideItems.Steel,70,Items.silicon,60));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(Items.silicon,5);
            consumeItems(with(Items.sand,5,Items.pyratite,2,Items.graphite,3));
            consumePower(7f);
            hasPower = true;
            health = 400;
            size = 4;
            hasItems = true;
            craftTime = 78f;
            buildTime = 250f;
            itemCapacity = 20;
        }};

        ForgingFurnace = new GenericCrafter("ForgingFurnace"){{//锻钢炉
            requirements(Category.crafting, ItemStack.with(Items.graphite,70,Items.metaglass,60,Items.silicon,90));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.Steel,3);
            consumeItems(with(Items.coal,2, dawnTideItems.Iron,3));
            consumePower(5f);
            hasPower = true;
            health = 400;
            size = 2;
            hasItems = true;
            craftTime = 70f;
            buildTime = 300f;
            itemCapacity = 20;
        }};

        GlazeKiln = new GenericCrafter("GlazeKiln"){{//镀瓷凿炉
            requirements(Category.crafting, ItemStack.with(Items.titanium,70, dawnTideItems.Iron,80,Items.silicon,90));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(dawnTideItems.CeramicGlass,3);
            consumeItems(with(Items.silicon,3,Items.metaglass,4));
            consumePower(4f);
            hasPower = true;
            health = 550;
            size = 3;
            hasItems = true;
            craftTime = 84f;
            buildTime = 230f;
            itemCapacity = 20;
        }};

        oreQuartz = new OreBlock(dawnTideItems.Quartz){{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        oreIron = new OreBlock(dawnTideItems.Iron){{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        oreUranium = new OreBlock(dawnTideItems.Uranium){{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        SiliconWall = new Wall("SiliconWall"){{
            requirements(Category.defense, with(Items.silicon,6));
            health = 650;
        }};

        SiliconWallLarge = new Wall("SiliconWallLarge"){{
            requirements(Category.defense, with(Items.silicon,24));
            health = 2600;
        }};

        GiantSiliconWall = new Wall("GiantSiliconWall"){{
            requirements(Category.defense, with(Items.silicon,54));
            health = 3900;
            insulated = true;
            absorbLasers = true;
            schematicPriority = 10;
        }};

        ChemicalDefenseWall = new Wall("ChemicalDefenseWall"){{
            requirements(Category.defense, with(Items.silicon,24));
            health = 2600;
        }};

        ChemicalDefenseWallLarge = new Wall("ChemicalDefenseWallLarge"){{
            requirements(Category.defense, with(Items.silicon,24));
            health = 2600;
        }};

        GiantChemicalDefenseWall = new Wall("GiantChemicalDefenseWall"){{
            requirements(Category.defense, with(Items.silicon,24));
            health = 2600;
        }};

        GiantCopperWall = new Wall("GiantCopperWall"){{
            requirements(Category.defense, with(Items.silicon,24));
            health = 2600;
        }};

        GiantTitaniumWall = new Wall("GiantTitaniumWall"){{
            requirements(Category.defense, with(Items.silicon,24));
            health = 2600;
        }};

        GiantThoriumWall = new Wall("GiantThoriumWall"){{
            requirements(Category.defense, with(Items.silicon,24));
            health = 2600;
        }};

        CrystalWall = new Wall("CrystalWall"){{
            requirements(Category.defense, with(Items.silicon,24));
            health = 2600;
        }};

        ErosionResistantWall = new Wall("ErosionResistantWall"){{
            requirements(Category.defense, with(Items.silicon,24));
            health = 2600;
        }};

        CrystalWallLarge = new Wall("CrystalWallLarge"){{
            requirements(Category.defense, with(Items.silicon,24));
            health = 2600;
        }};

        criticalReactor = new NuclearReactor("criticalReactor"){{
            requirements(Category.power, with(Items.lead, 300, Items.silicon, 200, Items.graphite, 150, Items.thorium, 150, Items.metaglass, 50));
            ambientSound = Sounds.loopThoriumReactor;
            ambientSoundVolume = 0.11f;
            size = 3;
            health = 700;
            itemDuration = 360f;
            powerProduction = 15f;
            heating = 35f;
            outputsPower = true;
            heatOutput = 36f;
            consumeItem(dawnTideItems.HighExplosive);
            consumeLiquid(Liquids.cryofluid, heating / coolantPower).update(false);
        }};

        tidalPowerGenerator = new ThermalGenerator("tidalPowerGenerator"){{
            requirements(Category.power, with(Items.copper, 40, Items.graphite, 35, Items.lead, 50, Items.silicon, 35, Items.metaglass, 40));
            powerProduction = 1.8f;
            generateEffect = Fx.redgeneratespark;
            effectChance = 0.011f;
            size = 2;
            floating = true;
            ambientSound = Sounds.loopHum;
            ambientSoundVolume = 0.06f;
            attribute = water;
        }};

        QuartzPowerNode = new PowerNode("QuartzPowerNode"){{
            requirements(Category.power, with(dawnTideItems.Quartz,2, Items.lead, 6));
            maxNodes = 30;
            laserRange = 180;
            underBullets = true;
            crushFragile = true;
        }};

        QuartzBattery = new Battery("QuartzBattery"){{
            requirements(Category.power, with(dawnTideItems.Quartz,80,Items.lead, 50, Items.silicon, 30));
            size = 3;
            consumePowerBuffered(250000f);
            baseExplosiveness = 7f;
        }};

        BlockSpotlight = new LightBlock("BlockSpotlight"){{
            requirements(Category.effect, BuildVisibility.lightingOnly, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            brightness = 0.75f;
            radius = 140f;
            consumePower(0.05f);
        }};

        miniWarehouse = new StorageBlock("miniWarehouse"){{
            requirements(Category.effect, with(Items.titanium, 250, Items.thorium, 125));
            size = 3;
            itemCapacity = 1000;
            scaledHealth = 55;
        }};

        BlockRepairer = new MendProjector("BlockRepairer"){{
            requirements(Category.effect, with(Items.lead, 100, Items.titanium, 25, Items.silicon, 40, Items.copper, 50));
            consumePower(1.5f);
            size = 2;
            reload = 250f;
            range = 85f;
            healPercent = 11f;
            phaseBoost = 15f;
            scaledHealth = 80;
            consumeItem(Items.phaseFabric).boost();
        }};
    }
}
