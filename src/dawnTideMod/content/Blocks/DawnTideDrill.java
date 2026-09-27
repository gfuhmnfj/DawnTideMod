package dawnTideMod.content.Blocks;

import dawnTideMod.content.dawnTideItems;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.production.Drill;

import static mindustry.type.ItemStack.with;

public class DawnTideDrill {
    public static Block
            magnetoExplosionDrill, magneticEnergyDrill, crystalDrill;

    public static void load(){
        magnetoExplosionDrill = new Drill("MagnetoExplosionDrill") {{
            localizedName = "磁爆钻头";
            requirements(Category.production, with(dawnTideItems.steel, 100, dawnTideItems.boundaryBreakingAlloy, 30, Items.silicon, 80, Items.thorium, 120));
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
            requirements(Category.production, with(dawnTideItems.steel, 100, dawnTideItems.boundaryBreakingAlloy, 30, Items.silicon, 80, Items.thorium, 120));
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
            requirements(Category.production, with(dawnTideItems.steel, 100, dawnTideItems.boundaryBreakingAlloy, 30, Items.silicon, 80, Items.thorium, 120));
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
    }
}
