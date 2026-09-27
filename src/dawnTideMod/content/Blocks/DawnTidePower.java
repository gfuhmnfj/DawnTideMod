package dawnTideMod.content.Blocks;

import dawnTideMod.content.dawnTideItems;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.power.Battery;
import mindustry.world.blocks.power.NuclearReactor;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.power.ThermalGenerator;

import static mindustry.type.ItemStack.with;
import static mindustry.world.meta.Attribute.water;

public class DawnTidePower {
    public static Block
            tidalPowerGenerator, quartzBattery, quartzPowerNode,criticalReactor, refinedSilverBattery;

    public static void load(){
        criticalReactor = new NuclearReactor("criticalReactor") {{ //临界反应堆
            localizedName = "临界反应堆";
            requirements(Category.power, with(dawnTideItems.steel, 250, Items.thorium, 150, Items.graphite, 270, Items.silicon, 180, Items.lead, 300));
            powerProduction = 25f;
            itemDuration = 270f;
            size = 3;
            ambientSound = Sounds.loopDifferential;
            ambientSoundVolume = 0.12f;
            fuelItem = dawnTideItems.highExplosive;
            // 接收/显示输入（NuclearReactor 类自身不注册消费器，acceptItem/acceptLiquid 全靠这里驱动）
            // 燃料：每 itemDuration 由 updateTile 的 consume() 扣 1；冷却液：只声明接收，热量吸取由 updateTile 自己扣（update(false) 防止每帧额外扣液）
            consumeItem(dawnTideItems.highExplosive);
            consumeLiquid(Liquids.cryofluid, heating / coolantPower).update(false);
        }};

        tidalPowerGenerator = new ThermalGenerator("tidalPowerGenerator") {{ //潮汐发电机
            localizedName = "潮汐发电机";
            requirements(Category.power, with(dawnTideItems.ceramicGlass, 50, Items.silicon, 40, dawnTideItems.iron, 60, Items.titanium, 70));
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
            requirements(Category.power, with(dawnTideItems.quartz, 10, Items.titanium, 10, Items.graphite, 10));
            maxNodes = 30;
            laserRange = 30;
            underBullets = true;
            crushFragile = true;
        }};

        quartzBattery = new Battery("QuartzBattery") {{ //石英电池
            localizedName = "石英电池";
            requirements(Category.power, with(dawnTideItems.quartz, 76, Items.lead, 100, Items.silicon, 120));
            size = 3;
            consumePowerBuffered(250000f);
            baseExplosiveness = 12f;
        }};

        refinedSilverBattery = new Battery("RefinedSilverBattery") {{ //石英电池
            localizedName = "钛银电池";
            requirements(Category.power, with(dawnTideItems.refinedTitaniumSilver, 100, dawnTideItems.uranium, 150, Items.plastanium, 70, dawnTideItems.steel, 135));
            size = 4;
            consumePowerBuffered(5000000f);
            baseExplosiveness = 23f;
        }};
    }
}
