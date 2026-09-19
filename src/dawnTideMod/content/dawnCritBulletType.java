package dawnTideMod.content;

import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Hitboxc;

/**
 * 「曙光潮涌」暴击：命中时按概率将伤害翻倍。
 * 原理（对照 BulletType.hitEntity 源码）：
 * 伤害结算读的是 b.damage，走 damage()/damagePierce()/damageArmorMult()，
 * 所以命中瞬间临时抬高 b.damage → 调 super 复用全部原生逻辑（护盾、护甲穿透、
 * 吸血、状态、击退、事件），再在 finally 里恢复——穿透弹多目标各自独立摇暴击。
 */
public class dawnCritBulletType extends BasicBulletType{

    public float critChance = 0.2f;
    public float critMultiplier = 2f;
    public Effect critEffect = Fx.hitLaser;

    public dawnCritBulletType(float speed, float damage){
        super(speed, damage);
    }

    @Override
    public void hitEntity(Bullet b, Hitboxc entity, float health){
        if(Mathf.chance(critChance)){
            float base = b.damage;
            b.damage = base * critMultiplier;
            try{
                super.hitEntity(b, entity, health);
            }finally{
                b.damage = base;
            }
            critEffect.at(entity.x(), entity.y(), b.rotation());
        }else{
            super.hitEntity(b, entity, health);
        }
    }
}