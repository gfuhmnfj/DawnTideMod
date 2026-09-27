package dawnTideMod.content.Blocks;

import dawnTideMod.TideClean.multicrafter.MultiCrafterBlock;
import dawnTideMod.TideClean.multicrafter.type.IOEntry;
import dawnTideMod.TideClean.multicrafter.type.Recipe;
import dawnTideMod.content.dawnTideItems;
import dawnTideMod.content.dawnTideLiquids;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;

import static mindustry.type.ItemStack.with;
public class DawnTideBlocks{

    public static Block
    packingMachine,UnpackingStation;

    public static void load(){

        packingMachine = new MultiCrafterBlock("packingMachine"){{
            localizedName = "打包机";
            requirements(Category.crafting, with(dawnTideItems.quartz, 30, Items.lead, 20));
            health = 200;
            size = 2;
            hasRandomOutputRecipes = false;
            autoSelectRecipe = false;

            recipes.add(new Recipe("waterBottle",
                    new IOEntry().withItems(ItemStack.with(dawnTideItems.iron,1)).withPower(1f),
                    new IOEntry().withItems(ItemStack.with(Items.silicon, 2)), 80f).withCraftEffect(Fx.smeltsmoke).isUnlocked()
            );

            recipes.add(new Recipe("dawn-multi-water",
                    new IOEntry().withItems(ItemStack.with(Items.lead, 2)),
                    new IOEntry().withLiquids(LiquidStack.with(Liquids.water, 0.2f)), 60f)
            );
        }};

    }
}
