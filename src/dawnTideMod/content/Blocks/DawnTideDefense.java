package dawnTideMod.content.Blocks;

import dawnTideMod.content.dawnTideItems;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.blocks.distribution.MassDriver;
import mindustry.world.blocks.power.LightBlock;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.meta.BuildVisibility;

import static mindustry.type.ItemStack.with;

public class DawnTideDefense {
    public static Block
            blockSpotlight, miniWarehouse, blockRepairer, largeMassDriver,storageRoom;

    public static void load(){
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
            requirements(Category.effect, with(dawnTideItems.refinedTitaniumSilver, 200, Items.thorium, 250, dawnTideItems.uranium, 150, dawnTideItems.steel, 100));
            size = 4;
            itemCapacity = 5000;
            scaledHealth = 55;
        }};
    }
}
