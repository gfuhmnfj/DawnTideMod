package dawnTideMod;

import dawnTideMod.core.CoreWreck;
import dawnTideMod.core.WreckableCore;
import dawnTideMod.thermal.ThermalCooler;
import dawnTideMod.thermal.ThermalCrafter;
import dawnTideMod.thermal.ThermalProducer;
import dawnTideMod.thermal.ThermalWall;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.meta.BuildVisibility;

import static mindustry.type.ItemStack.with;
public class DawnBlocks{
    public static ThermalWall thermalWall;
    public static ThermalWall heatProofWall;
    public static ThermalWall cryoWall;
    public static ThermalProducer heatSource;
    public static ThermalCooler cooler;
    public static ThermalCrafter cryoForge;
    public static CoreWreck coreWreck;
    public static WreckableCore coreSalvage;
    public static void load(){
        loadThermal();
        loadCrafter();
        loadCore();
    }

    private static void loadThermal(){
        thermalWall = new ThermalWall("dawn-thermal-wall"){{
            health = 800;
            size = 1;
            ambientTemp = 0.5f;
            thermalMass = 1f;
            heatResist = 0f;
            coldResist = 0f;
        }};
        thermalWall.requirements(Category.defense, with(Items.copper, 12, Items.lead, 8));

        heatProofWall = new ThermalWall("dawn-heatproof-wall"){{
            health = 900;
            size = 1;
            ambientTemp = 0.8f;
            thermalMass = 1.4f;
            heatResist = 1f;
            coldResist = 0f;
        }};
        heatProofWall.requirements(Category.defense, with(Items.titanium, 12, Items.metaglass, 8));

        cryoWall = new ThermalWall("dawn-cryo-wall"){{
            health = 900;
            size = 1;
            ambientTemp = 0.15f;
            thermalMass = 1.4f;
            heatResist = 0f;
            coldResist = 1f;
        }};
        cryoWall.requirements(Category.defense, with(Items.titanium, 12, Items.metaglass, 8));

        heatSource = new ThermalProducer("dawn-heat-source"){{
            health = 320;
            size = 2;
            targetTemp = 0.95f;
            warmupRate = 0.03f;
            heatOutput = 15f;
            thermalMass = 2f;
            heatResist = 0.6f;
        }};
        heatSource.requirements(Category.effect, with(Items.copper, 40, Items.graphite, 25, Items.silicon, 20));

        cooler = new ThermalCooler("dawn-cooler"){{
            health = 320;
            size = 2;
            targetTemp = 0.05f;
            coolRate = 0.03f;
            coolOutput = 15f;
            thermalMass = 2f;
            coldResist = 0.6f;
        }};
        cooler.requirements(Category.effect, with(Items.copper, 40, Items.metaglass, 30, Items.silicon, 20));
    }

    private static void loadCrafter(){
        cryoForge = new ThermalCrafter("dawn-cryo-forge"){{
            health = 420;
            size = 3;
            craftTime = 90f;
            craftEffect = Fx.smeltsmoke;
            updateEffect = Fx.freezing;
            outputItem = new ItemStack(Items.metaglass, 3);
            coldItems = with(Items.sand, 2, Items.lead, 1);
            maxOperatingTemp = 0.35f;
            minTempEfficiency = 0.1f;
            thermalMass = 1.5f;
            coldResist = 1f;
            ambientTemp = 0.5f;
            hasPower = true;
            consumePower(1.2f);
        }};
        cryoForge.requirements(Category.crafting,
            with(Items.copper, 60, Items.lead, 50, Items.titanium, 40, Items.silicon, 35));
    }

    private static void loadCore(){
        coreWreck = new CoreWreck("dawn-core-wreck"){{
            health = 250;
            size = 3;
            salvageFrac = 0.6f;
            decay = false;
        }};

        coreSalvage = new WreckableCore("dawn-core-salvage"){{
            health = 3000;
            size = 4;
            itemCapacity = 8000;
            wreckBlock = coreWreck;
            wreckHealthFrac = 0.25f;
            salvageFrac = 0.6f;
            transferItems = true;
            silentCollapse = true;
            alwaysUnlocked = false;
            buildVisibility = BuildVisibility.shown;
        }};
        coreSalvage.requirements(Category.effect,
            with(Items.copper, 1500, Items.lead, 1200, Items.silicon, 800, Items.titanium, 600));
    }
}
