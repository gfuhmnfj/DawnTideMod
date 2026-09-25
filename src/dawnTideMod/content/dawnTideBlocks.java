package dawnTideMod.content;

import arc.struct.Seq;
import dawnTideMod.TideClean.Wall.RegenWall;
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


/** 「曙光潮涌」方块注册类（工厂 / 矿物 / 墙 / 电力 / 辅助 / 运输 / 钻头 / 单位工厂）。 */
public class DawnTideBlocks{

    public static Block
        // 工厂
        explosivesFactory, bariteFurnace, thermonuclearFurnace, sulfideElectrolysisCell, slagCooler,
        crystalSynthesizer, compositeSiliconPlant, forgingFurnace, glazeKiln, titaniumSilverMeltingFurnace,
        blueCrystalCompressor, fluxReactor,
        // 矿物
        quartzOre, ironOre, uraniumOre, titaniumSilverOre, oreCrystallizationOre, bariteOre,
        // 墙
        siliconWall, siliconWallLarge, giantSiliconWall, steelWall, steelWallLarge,
        giantCopperWall, giantTitaniumWall, giantThoriumWall, crystalWall, crystalWallLarge,
        erosionResistantWall, erosionResistantWallLarge,
        // 电力
        tidalPowerGenerator, quartzBattery, quartzPowerNode, criticalReactor, refinedSilverBattery,
        // 辅助
        blockSpotlight, miniWarehouse, blockRepairer, largeMassDriver,
        // 运输
        quartzConveyor, quartzBridge, refinedSilverConveyor, refinedSilverBridge, unRefinedSilver,
        // 钻头
        magnetoExplosionDrill, magneticEnergyDrill, crystalDrill, storageRoom,
        // 单位工厂
        numberUpgradeUnitGenerator, multiplierLevelUnitGenerator, multiPowerUnitGenerator,
        unboundedUnitGenerator, multiScaleUnitReconstructionFactory;

    public static void load(){
        //工厂
        explosivesFactory = new GenericCrafter("ExplosivesFactory") {{
            localizedName = "炸药加工厂";
            requirements(Category.crafting, with(Items.titanium, 90, Items.silicon, 40, DawnTideItems.steel, 70));
            alwaysUnlocked = false;
            hasPower = true;
            hasItems = true;
            craftEffect = Fx.pulverizeMedium;
            outputItems = with(Items.blastCompound, 2, DawnTideItems.highExplosive, 1);
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
            outputItem = new ItemStack(DawnTideItems.barite, 1);
            consumeItems(with(Items.titanium, 1, Items.metaglass, 2));
            consumePower(1.1f);
            health = 500;
            size = 2;
            craftTime = 99f;
            buildTime = 230f;
        }};

        thermonuclearFurnace = new GenericCrafter("ThermonuclearFurnace") {{ //热核熔炉
            localizedName = "热河熔炉";
            requirements(Category.crafting, ItemStack.with(DawnTideItems.steel, 130, Items.thorium, 150, Items.surgeAlloy, 60));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(DawnTideItems.boundaryBreakingAlloy, 1);
            consumeItems(with(Items.copper, 2, Items.titanium, 3, DawnTideItems.steel, 2));
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
            outputItem = new ItemStack(DawnTideItems.oreCrystallization, 3);
            consumeItems(with(DawnTideItems.barite, 2, Items.silicon, 3));
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
            requirements(Category.crafting, ItemStack.with(Items.graphite, 80, DawnTideItems.steel, 70, Items.silicon, 60));
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
            outputItem = new ItemStack(DawnTideItems.steel, 3);
            consumeItems(with(Items.coal, 2, DawnTideItems.iron, 3));
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
            requirements(Category.crafting, ItemStack.with(Items.titanium, 70, DawnTideItems.iron, 80, Items.silicon, 90));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(DawnTideItems.ceramicGlass, 3);
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
            requirements(Category.crafting, ItemStack.with(DawnTideItems.quartz, 90, DawnTideItems.ceramicGlass, 80, Items.silicon, 90, DawnTideItems.oreCrystallization, 50, Items.plastanium, 85));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(DawnTideItems.titaniumSilver, 2);
            consumeItems(with(DawnTideItems.titaniumSilver, 1, Items.graphite, 2));
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
            requirements(Category.crafting, ItemStack.with(DawnTideItems.quartz, 90, DawnTideItems.ceramicGlass, 80, Items.silicon, 90, DawnTideItems.oreCrystallization, 50, Items.plastanium, 85));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(DawnTideItems.blueCrystal, 1);
            consumeItems(with(DawnTideItems.barite, 1, Items.graphite, 2));
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
            requirements(Category.crafting, ItemStack.with(DawnTideItems.quartz, 90, DawnTideItems.ceramicGlass, 80, Items.silicon, 90, DawnTideItems.oreCrystallization, 50, Items.plastanium, 85));
            alwaysUnlocked = false;
            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(DawnTideItems.fluxAlloy, 2);
            consumeItems(with(DawnTideItems.titaniumSilver, 1, Items.graphite, 2));
            consumePower(5f);
            hasPower = true;
            health = 550;
            size = 4;
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
                    new IOEntry().withItems(ItemStack.with(Items.copper, 3, Items.silicon, 3)).withPower(1.5f),
                    new IOEntry().withItems(ItemStack.with(Items.silicon, 2)), 80f).withCraftEffect(Fx.smeltsmoke).isUnlocked());

            recipes.add(new Recipe("dawn-multi-water",
                    new IOEntry().withItems(ItemStack.with(Items.lead, 2)),
                    new IOEntry().withLiquids(LiquidStack.with(Liquids.water, 0.2f)), 60f
            ));
        }};*/


        //矿物

        quartzOre = new OreBlock(DawnTideItems.quartz) {{
            localizedName = "石英矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        ironOre = new OreBlock(DawnTideItems.iron) {{
            localizedName = "铁矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        uraniumOre = new OreBlock(DawnTideItems.uranium) {{
            localizedName = "铀矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        titaniumSilverOre = new OreBlock(DawnTideItems.titaniumSilver) {{
            localizedName = "钛银矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        oreCrystallizationOre = new OreBlock(DawnTideItems.oreCrystallization) {{
            localizedName = "矿石结晶矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};

        bariteOre = new OreBlock(DawnTideItems.barite) {{
            localizedName = "重晶石矿";
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};


        //墙
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
            requirements(Category.defense, with(DawnTideItems.ceramicGlass, 6));
            health = 450;
        }};

        steelWallLarge = new Wall("ChemicalDefenseWallLarge") {{ //大型钢墙
            localizedName = "大型钢墙";
            requirements(Category.defense, with(DawnTideItems.ceramicGlass, 24));
            health = 2300;
            size = 2;
        }};

        erosionResistantWall = new Wall("ErosionResistantWall") {{ //蚀抗墙
            localizedName = "蚀抗墙";
            requirements(Category.defense, with(DawnTideItems.boundaryBreakingAlloy, 6, DawnTideItems.iron, 6));
            health = 800;
        }};

        erosionResistantWallLarge = new Wall("ErosionResistantWallLarge") {{ //大型蚀抗墙
            localizedName = "大型蚀抗墙";
            requirements(Category.defense, with(DawnTideItems.boundaryBreakingAlloy, 24, DawnTideItems.iron, 24));
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
            requirements(Category.defense, with(DawnTideItems.quartz, 6, DawnTideItems.steel, 6));
            health = 540;
        }};

        crystalWallLarge = new Wall("CrystalWallLarge") {{ //大型碎晶墙
            localizedName = "大型碎晶墙";
            requirements(Category.defense, with(DawnTideItems.quartz, 24, DawnTideItems.steel, 24));
            health = 2900;
            size = 2;
        }};


        //电力
        criticalReactor = new NuclearReactor("criticalReactor") {{ //临界反应堆
            localizedName = "临界反应堆";
            requirements(Category.power, with(DawnTideItems.steel, 250, Items.thorium, 150, Items.graphite, 270, Items.silicon, 180, Items.lead, 300));
            powerProduction = 25f;
            itemDuration = 270f;
            size = 3;
            ambientSound = Sounds.loopDifferential;
            ambientSoundVolume = 0.12f;
            fuelItem = DawnTideItems.highExplosive;
            // 接收/显示输入（NuclearReactor 类自身不注册消费器，acceptItem/acceptLiquid 全靠这里驱动）
            // 燃料：每 itemDuration 由 updateTile 的 consume() 扣 1；冷却液：只声明接收，热量吸取由 updateTile 自己扣（update(false) 防止每帧额外扣液）
            consumeItem(DawnTideItems.highExplosive);
            consumeLiquid(Liquids.cryofluid, heating / coolantPower).update(false);
        }};

        tidalPowerGenerator = new ThermalGenerator("tidalPowerGenerator") {{ //潮汐发电机
            localizedName = "潮汐发电机";
            requirements(Category.power, with(DawnTideItems.ceramicGlass, 50, Items.silicon, 40, DawnTideItems.iron, 60, Items.titanium, 70));
            powerProduction = 1.5f;
            generateEffect = Fx.redgeneratespark;
            effectChance = 0.011f;
            size = 2;
            floating = true;
            ambientSound = Sounds.loopHum;
            ambientSoundVolume = 0.06f;
            attribute = water;
        }};

        quartzPowerNode = new PowerNode("QuartzPowerNode") {{ //石英电力节点
            size = 3;
            localizedName = "石英电力节点";
            requirements(Category.power, with(DawnTideItems.quartz, 10, Items.titanium, 10, Items.graphite, 10));
            maxNodes = 30;
            laserRange = 30;
            underBullets = true;
            crushFragile = true;
        }};

        quartzBattery = new Battery("QuartzBattery") {{ //石英电池
            localizedName = "石英电池";
            requirements(Category.power, with(DawnTideItems.quartz, 76, Items.lead, 100, Items.silicon, 120));
            size = 3;
            consumePowerBuffered(250000f);
            baseExplosiveness = 12f;
        }};

        refinedSilverBattery = new Battery("RefinedSilverBattery") {{ //石英电池
            localizedName = "钛银电池";
            requirements(Category.power, with(DawnTideItems.refinedTitaniumSilver, 100, DawnTideItems.uranium, 150, Items.plastanium, 70, DawnTideItems.steel, 135));
            size = 4;
            consumePowerBuffered(5000000f);
            baseExplosiveness = 23f;
        }};


        //辅助
        blockSpotlight = new LightBlock("BlockSpotlight") {{ //区块探照灯
            localizedName = "区块探照灯";
            requirements(Category.effect, BuildVisibility.lightingOnly, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            brightness = 0.75f;
            radius = 140f;
            consumePower(0.05f);
            size = 3;
        }};

        largeMassDriver = new MassDriver("LargemassDriver"){{
            localizedName = "大型质量驱动器";
            requirements(Category.distribution, with(Items.titanium, 125, Items.silicon, 75, Items.lead, 125, Items.thorium, 50));
            size = 4;
            itemCapacity = 240;
            reload = 150f;
            range = 600f;
            consumePower(2.5f);
        }};

        blockRepairer = new MendProjector("BlockRepairer") {{ //区块修复器
            localizedName = "区块修复器";
            requirements(Category.effect, with(Items.lead, 100, Items.titanium, 25, Items.silicon, 40, Items.copper, 50));
            consumePower(1.5f);
            size = 3;
            reload = 210f;
            range = 240f;
            healPercent = 8f;
            phaseBoost = 11f;
            scaledHealth = 80;
            consumeItem(Items.phaseFabric).boost();
        }};

        miniWarehouse = new StorageBlock("miniWarehouse") {{ //微型仓库
            localizedName = "微型仓库";
            requirements(Category.effect, with(Items.titanium, 250, Items.thorium, 125));
            size = 1;
            itemCapacity = 100;
            scaledHealth = 55;
        }};

        storageRoom = new StorageBlock("StorageRoom") {{ //储藏室
            localizedName = "储藏室";
            requirements(Category.effect, with(DawnTideItems.refinedTitaniumSilver, 200, Items.thorium, 250, DawnTideItems.uranium, 150, DawnTideItems.steel, 100));
            size = 4;
            itemCapacity = 5000;
            scaledHealth = 55;
        }};


        //运输
        quartzConveyor = new Conveyor("QuartzConveyor") {{
            localizedName = "石英传送带";
            requirements(Category.distribution, with(DawnTideItems.quartz, 1));
            health = 250;
            speed = 0.25f;
            displayedSpeed = 25f;
            researchCost = with(DawnTideItems.quartz, 500);
        }};

        quartzBridge = new BufferedItemBridge("QuartzBridge") {{
            localizedName = "石英传送带桥";
            requirements(Category.distribution, with(DawnTideItems.quartz, 3, Items.lead, 6));
            fadeIn = moveArrows = false;
            range = 6;
            speed = 74f;
            arrowSpacing = 8f;
            bufferCapacity = 14;
            crushFragile = true;
        }};

        refinedSilverBridge = new BufferedItemBridge("RefinedSilverBridge") {{
            localizedName = "钛银传送带桥";
            requirements(Category.distribution, with(DawnTideItems.quartz, 3, Items.lead, 6));
            fadeIn = moveArrows = false;
            range = 15;
            speed = 74f;
            arrowSpacing = 17f;
            bufferCapacity = 25;
            crushFragile = true;
        }};

        refinedSilverConveyor = new StackConveyor("RefinedSilverConveyor"){{ //钛银带
            localizedName = "钛银传送带";
            requirements(Category.distribution, with(DawnTideItems.titaniumSilver, 1, Items.silicon, 1));
            health = 150;
            speed = 4f / 60f;
            itemCapacity = 30;
        }};

        unRefinedSilver = new Unloader("UnRefinedSilver"){{
            localizedName = "钛银装卸器";
            requirements(Category.distribution, with(DawnTideItems.titaniumSilver, 20, Items.thorium, 25));
            speed = 300f / 11f;
            group = BlockGroup.transportation;
        }};


        //Production
        magnetoExplosionDrill = new Drill("MagnetoExplosionDrill") {{
            localizedName = "磁爆钻头";
            requirements(Category.production, with(DawnTideItems.steel, 100, DawnTideItems.boundaryBreakingAlloy, 30, Items.silicon, 80, Items.thorium, 120));
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

        magneticEnergyDrill = new Drill("MagneticEnergyDrill") {{
            localizedName = "磁能钻头";
            requirements(Category.production, with(DawnTideItems.steel, 100, DawnTideItems.boundaryBreakingAlloy, 30, Items.silicon, 80, Items.thorium, 120));
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

        crystalDrill = new Drill("CrystalDrill") {{
            localizedName = "晶石钻头";
            requirements(Category.production, with(DawnTideItems.steel, 100, DawnTideItems.boundaryBreakingAlloy, 30, Items.silicon, 80, Items.thorium, 120));
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
        numberUpgradeUnitGenerator = new UnitFactory("NumberUpgradeUnitGenerator") {{ //数增级单位生成器
            localizedName = "数增级单位生成器";
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

        multiplierLevelUnitGenerator = new UnitFactory("MultiplierLevelUnitGenerator") {{ //倍乘级
            localizedName = "倍乘级单位生成器";
            health = 3130;
            size = 5;
            hasPower = true;
            hasLiquids = true;
            buildTime = 600;
            consumePower(7f);
            consumeLiquid(Liquids.water, 1);
            requirements(Category.units, with(Items.titanium, 350, Items.lead, 650, Items.silicon, 450, DawnTideItems.oreCrystallization, 350, Items.thorium, 650));
            plans = Seq.with(
                    new UnitPlan(UnitTypes.fortress, 1800f, with(Items.silicon, 150, DawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.quasar, 1800f, with(Items.silicon, 150, DawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.spiroct, 1800f, with(Items.silicon, 150, DawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.zenith, 1800f, with(Items.silicon, 150, DawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.mega, 1800f, with(Items.silicon, 150, DawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.bryde, 1800f, with(Items.silicon, 150, DawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.cyerce, 1800f, with(Items.silicon, 150, DawnTideItems.barite, 50)));
        }};

        multiPowerUnitGenerator = new UnitFactory("MultiPowerUnitGenerator") {{ //多幂级
            localizedName = "多幂级单位生成器";
            health = 4400;
            size = 5;
            hasPower = true;
            hasLiquids = true;
            buildTime = 600;
            consumePower(20f);
            consumeLiquid(DawnTideLiquids.microscaleFluid, 5);
            requirements(Category.units, with(Items.silicon, 450, DawnTideItems.oreCrystallization, 550, DawnTideItems.refinedTitaniumSilver, 450, Items.plastanium, 500, Items.lead, 2500, Items.phaseFabric, 400));
            plans = Seq.with(
                    new UnitFactory.UnitPlan(UnitTypes.scepter, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.vela, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.arkyid, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.antumbra, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.quad, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.sei, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.aegires, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)));
        }};

        unboundedUnitGenerator = new UnitFactory("UnboundedUnitGenerator") {{ //无量级
            localizedName = "无量级单位生成器";
            health = 5500;
            size = 5;
            hasPower = true;
            hasLiquids = true;
            buildTime = 6000;
            consumePower(34f);
            consumeLiquid(DawnTideLiquids.microscaleFluid, 5);
            requirements(Category.units, with(Items.silicon, 450, DawnTideItems.oreCrystallization, 550, DawnTideItems.refinedTitaniumSilver, 450, Items.plastanium, 500, Items.lead, 2500, Items.phaseFabric, 400));
            plans = Seq.with(
                    new UnitFactory.UnitPlan(UnitTypes.eclipse, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.toxopid, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.reign, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.omura, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.oct, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.corvus, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.navanax, 4800f, with(Items.silicon, 550, DawnTideItems.titaniumSilver, 500, DawnTideItems.steel, 450)));
        }};
    }
}
