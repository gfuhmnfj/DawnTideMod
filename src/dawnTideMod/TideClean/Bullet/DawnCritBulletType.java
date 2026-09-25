package dawnTideMod.Bullet;

import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Hitboxc;

public class DawnCritBulletType extends BasicBulletType{

    public float critChance = 0.2f;
    public float critMultiplier = 2f;
    public Effect critEffect = Fx.hitLaser;
    public DawnCritBulletType(float speed, float damage){
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
