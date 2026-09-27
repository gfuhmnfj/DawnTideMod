package dawnTideMod.content;

import arc.graphics.Color;
import dawnTideMod.TideClean.Bullet.DawnCritBulletType;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;

public class dawnBullets {

    public static BulletType tideCrit, tideCritHeavy;

    public static void load(){

        tideCrit = new DawnCritBulletType(3.2f, 18f){{
            critChance = 0.25f;
            critMultiplier = 2f;
            critEffect = Fx.hitBulletBig;
            lifetime = 55f;
            width = height = 7f;
            hitSize = 4f;
            backColor = Color.valueOf("a8d6e8");
            frontColor = Color.white;
            ammoMultiplier = 3f;
        }};

        tideCritHeavy = new DawnCritBulletType(2.8f, 32f){{
            critChance = 0.35f;
            critMultiplier = 2.5f;
            critEffect = Fx.hitLaser;
            lifetime = 65f;
            width = height = 9f;
            hitSize = 5f;
            backColor = Color.valueOf("ffd9a0");
            frontColor = Color.valueOf("fff5e0");
            knockback = 0.6f;
            ammoMultiplier = 3f;
        }};
    }
}
