package dawnTideMod.content;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;

/** 「曙光潮涌」弹丸注册类 —— Mindustry 内容类的标准写法：静态字段 + load() 初始化 */
public class dawnBullets {

    public static BulletType tideCrit, tideCritHeavy;

    public static void load(){

        // 铜弹：25% 概率 ×2 暴击
        tideCrit = new dawnCritBulletType(3.2f, 18f){{
            critChance = 0.25f;
            critMultiplier = 2f;
            critEffect = Fx.hitBulletBig;
            lifetime = 55f;
            width = height = 7f;
            hitSize = 4f;
            backColor = Color.valueOf("a8d6e8");
            frontColor = Color.white;
            ammoMultiplier = 3f;       // 每个铜锭弹药换 3 发(填弹效率)
        }};

        tideCritHeavy = new dawnCritBulletType(2.8f, 32f){{
            critChance = 0.35f;//35%
            critMultiplier = 2.5f;//2.5倍伤害
            critEffect = Fx.hitLaser;
            lifetime = 65f;
            width = height = 9f;
            hitSize = 5f;
            backColor = Color.valueOf("ffd9a0");
            frontColor = Color.valueOf("fff5e0");
            knockback = 0.6f;          // 暴击流配小击退，手感更好
            ammoMultiplier = 3f;
        }};
    }
}
