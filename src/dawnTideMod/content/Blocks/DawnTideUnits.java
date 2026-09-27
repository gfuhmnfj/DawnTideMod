package dawnTideMod.content.Blocks;

import arc.struct.Seq;
import dawnTideMod.content.dawnTideItems;
import dawnTideMod.content.dawnTideLiquids;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.units.UnitFactory;

import static mindustry.type.ItemStack.with;

public class DawnTideUnits {
    public static Block
            numberUpgradeUnitGenerator, multiplierLevelUnitGenerator, multiPowerUnitGenerator,
            unboundedUnitGenerator, multiScaleUnitReconstructionFactory;

    public static void load(){
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
            requirements(Category.units, with(Items.titanium, 350, Items.lead, 650, Items.silicon, 450, dawnTideItems.oreCrystallization, 350, Items.thorium, 650));
            plans = Seq.with(
                    new UnitPlan(UnitTypes.fortress, 1800f, with(Items.silicon, 150, dawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.quasar, 1800f, with(Items.silicon, 150, dawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.spiroct, 1800f, with(Items.silicon, 150, dawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.zenith, 1800f, with(Items.silicon, 150, dawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.mega, 1800f, with(Items.silicon, 150, dawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.bryde, 1800f, with(Items.silicon, 150, dawnTideItems.barite, 50)),
                    new UnitPlan(UnitTypes.cyerce, 1800f, with(Items.silicon, 150, dawnTideItems.barite, 50)));
        }};

        multiPowerUnitGenerator = new UnitFactory("MultiPowerUnitGenerator") {{ //多幂级
            localizedName = "多幂级单位生成器";
            health = 4400;
            size = 5;
            hasPower = true;
            hasLiquids = true;
            buildTime = 600;
            consumePower(20f);
            consumeLiquid(dawnTideLiquids.microscaleFluid, 5);
            requirements(Category.units, with(Items.silicon, 450, dawnTideItems.oreCrystallization, 550, dawnTideItems.refinedTitaniumSilver, 450, Items.plastanium, 500, Items.lead, 2500, Items.phaseFabric, 400));
            plans = Seq.with(
                    new UnitFactory.UnitPlan(UnitTypes.scepter, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.vela, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.arkyid, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.antumbra, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.quad, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.sei, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.aegires, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)));
        }};

        unboundedUnitGenerator = new UnitFactory("UnboundedUnitGenerator") {{ //无量级
            localizedName = "无量级单位生成器";
            health = 5500;
            size = 5;
            hasPower = true;
            hasLiquids = true;
            buildTime = 6000;
            consumePower(34f);
            consumeLiquid(dawnTideLiquids.microscaleFluid, 5);
            requirements(Category.units, with(Items.silicon, 450, dawnTideItems.oreCrystallization, 550, dawnTideItems.refinedTitaniumSilver, 450, Items.plastanium, 500, Items.lead, 2500, Items.phaseFabric, 400));
            plans = Seq.with(
                    new UnitFactory.UnitPlan(UnitTypes.eclipse, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.toxopid, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.reign, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.omura, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.oct, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.corvus, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)),
                    new UnitFactory.UnitPlan(UnitTypes.navanax, 4800f, with(Items.silicon, 550, dawnTideItems.titaniumSilver, 500, dawnTideItems.steel, 450)));
        }};
    }
}
