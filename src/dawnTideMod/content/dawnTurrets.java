package dawnTideMod.content;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.defense.turrets.ItemTurret;

/** 「曙光潮涌」炮塔注册类 */
public class dawnTurrets {

    public static ItemTurret tideCannon;

    public static void load(){

        // 潮涌炮：吃铜/石墨，发射暴击弹
        tideCannon = new ItemTurret("tide-cannon"){{
            requirements(Category.turret, ItemStack.with(Items.copper, 120, Items.graphite, 80));
            health = 420;
            size = 2;
            range = 220f;
            reload = 35f;              // 射击间隔(帧)——字段在 ReloadTurret 基类
            inaccuracy = 3f;
            rotateSpeed = 6f;

            ammo(
                Items.copper,   dawnBullets.tideCrit,
                Items.graphite, dawnBullets.tideCritHeavy
            );

            limitRange(2f);            // 弹丸射程自动裁剪到炮塔射程内(留2格余量)
        }};
    }
}
