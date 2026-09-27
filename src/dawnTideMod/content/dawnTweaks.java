package dawnTideMod.content;

import arc.graphics.Color;
import arc.struct.ObjectSet;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BombBulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.bullet.MissileBulletType;
import mindustry.entities.pattern.ShootSpread;
import mindustry.gen.Sounds;
import mindustry.type.Weapon;
import mindustry.world.blocks.distribution.ItemBridge;

/** 改原版内容的数值/字段，全部写在这里 */
public class dawnTweaks {
    public static void load(){
        Blocks.coreShard.itemCapacity = 5000;
        ((ItemBridge)Blocks.bridgeConduit).range = 5;
        loadVanillaUnits();
        // 放在 loadVanillaUnits() 之后，保证这里的覆盖不被五单位改写冲掉
        UnitTypes.antumbra.health = 50;
    }

    /** 星域「原版主空」五单位的字段改写（原 content/units/原版主空 JSON 转 Java，只改列出的字段，其余保留原版） */
    public static void loadVanillaUnits(){
        // 星辉（flare）
        UnitTypes.flare.immunities = ObjectSet.with(StatusEffects.unmoving, StatusEffects.freezing);
        UnitTypes.flare.rotateSpeed = 3.5f;
        UnitTypes.flare.speed = 2.8f;
        UnitTypes.flare.accel = 0.1f;
        UnitTypes.flare.drag = 0.09f;
        UnitTypes.flare.trailLength = 3; // JSON 3.23，字段为 int
        UnitTypes.flare.targetAir = true;
        UnitTypes.flare.circleTarget = true;
        UnitTypes.flare.health = 200f;
        UnitTypes.flare.weapons.clear();
        UnitTypes.flare.weapons.add(new Weapon("堡垒1"){{
            reload = 48f;
            x = 2f;
            y = 1f;
            rotate = false;
            inaccuracy = 0f;
            shake = 2f;
            recoil = 4f;
            shootY = 7f;
            top = false;
            alternate = true;
            ejectEffect = Fx.casing3;
            shootSound = Sounds.shootArtillery;
            bullet = new ArtilleryBulletType(3.2f, 12f){{
                recoil = 0.45f;
                homingRange = 40f;
                homingPower = 0.1f;
                homingDelay = 27f;
                collides = true;
                collidesTiles = false;
                collidesAir = false;
                status = StatusEffects.blasted;
                lifetime = 30f;
                hitSound = Sounds.explosion;
                hitEffect = Fx.flakExplosionBig;
                despawnEffect = Fx.blastExplosion;
                shootEffect = Fx.shootBig2;
                smokeEffect = Fx.shootBigSmoke;
                splashDamage = 8f;
                splashDamageRadius = 45f;
                width = 12f;
                height = 16f;
                trailLength = 10;
                trailWidth = 2f;
                trailColor = Color.valueOf("eab676");
            }};
        }});

        // 天垠（horizon）
        UnitTypes.horizon.details = "专业反建筑攻顶燃烧弹";
        UnitTypes.horizon.speed = 2.75f;
        UnitTypes.horizon.targetAir = false;
        UnitTypes.horizon.hitSize = 12f;
        UnitTypes.horizon.health = 800f;
        // v8 已删除单位弹药系统，JSON 的 ammoType/ammoCapacity 无对应字段，跳过
        UnitTypes.horizon.weapons.clear();
        UnitTypes.horizon.weapons.add(new Weapon("天垠1"){{
            minShootVelocity = 1.5f;
            reload = 85f;
            x = 0f;
            y = 0f;
            ignoreRotation = true;
            rotate = true;
            shootCone = 120f;
            inaccuracy = 15f;
            mirror = false;
            shootSound = Sounds.none;
            shoot.shots = 7;
            shoot.shotDelay = 4f;
            bullet = new BombBulletType(){{
                splashDamage = 30f;
                splashDamageRadius = 10f;
                status = StatusEffects.blasted;
                buildingDamageMultiplier = 2f;
                incendAmount = 1;
                incendSpread = 16f;
                incendChance = 0.005f;
                lifetime = 30f;
                width = 9f;
                height = 15f;
                trailLength = 10;
                trailWidth = 2.2f;
                trailColor = Color.valueOf("ff9c4a");
                shootEffect = Fx.none;
                smokeEffect = Fx.none;
                hitEffect = Fx.flakExplosion;
                despawnEffect = Fx.flakExplosion;
            }};
        }});

        // 苍穹（zenith）
        UnitTypes.zenith.details = "扩增导弹发射巢大小";
        UnitTypes.zenith.speed = 2.6f;
        UnitTypes.zenith.targetAir = true;
        UnitTypes.zenith.health = 2200f;
        UnitTypes.zenith.armor = 3f;
        UnitTypes.zenith.range = 240f;
        UnitTypes.zenith.weapons.clear();
        UnitTypes.zenith.weapons.add(new Weapon("苍穹1"){{
            reload = 50f;
            x = 7.5f;
            y = 0f;
            shootY = 8f;
            shoot = new ShootSpread(3, 1.77f);
            rotate = true;
            inaccuracy = 6f;
            alternate = true;
            ejectEffect = Fx.casing2;
            shootSound = Sounds.shootMissile;
            bullet = new MissileBulletType(5f, 35f){{
                splashDamageRadius = 25f;
                splashDamage = 25f;
                status = StatusEffects.blasted;
                backColor = Color.valueOf("ff7f24");
                hitEffect = Fx.explosion;
                despawnEffect = Fx.explosion;
                smokeEffect = Fx.none;
                lifetime = 50f;
                width = 8f;
                height = 9f;
                trailLength = 12;
                trailWidth = 2.2f;
                trailColor = Color.valueOf("ff7f24");
            }};
        }});

        // 月影（antumbra）——注意：若上面把 health 改为 50 的测试值仍生效，此处覆盖为正式值
        UnitTypes.antumbra.health = 12000f;
        UnitTypes.antumbra.weapons.clear();
        UnitTypes.antumbra.weapons.add(
            new Weapon("巫妖1"){{
                reload = 7f;
                shootY = 7f;
                x = 10f;
                y = 0f;
                rotate = true;
                rotateSpeed = 1f;
                shootSound = Sounds.shootSpectre; // 原 JSON shootBig，159.7 无此音效，取重型炮声替代
                shake = 1f;
                ejectEffect = Fx.casing3;
                bullet = new BasicBulletType(8.5f, 70f){{
                    lifetime = 28f;
                    smokeEffect = Fx.shootBig;
                    width = 14f;
                    height = 20f;
                    trailLength = 12;
                    trailWidth = 2.4f;
                    trailColor = Color.valueOf("ffd9a0");
                }};
            }},
            new Weapon("巫妖2"){{
                reload = 50f;
                x = 18f;
                y = 8f;
                shootY = 6f;
                shoot.shots = 5;
                shoot.shotDelay = 1f;
                rotate = true;
                inaccuracy = 10f;
                alternate = true;
                shootSound = Sounds.shootMissile;
                ejectEffect = Fx.casing1;
                bullet = new MissileBulletType(5f, 20f){{
                    splashDamageRadius = 30f;
                    splashDamage = 40f;
                    status = StatusEffects.blasted;
                    hitEffect = Fx.flakExplosionBig;
                    despawnEffect = Fx.flakExplosionBig;
                    smokeEffect = Fx.none;
                    lifetime = 40f;
                    width = 7f;
                    height = 8f;
                    trailLength = 12;
                    trailWidth = 2f;
                    trailColor = Color.valueOf("ff8a5c");
                }};
            }},
            new Weapon("巫妖2"){{
                reload = 75f;
                x = -18f;
                y = -9f;
                shootY = 6f;
                shoot.shots = 5;
                shoot.shotDelay = 1f;
                rotate = true;
                inaccuracy = 10f;
                alternate = true;
                shootSound = Sounds.shootMissile;
                ejectEffect = Fx.casing1;
                bullet = new MissileBulletType(5f, 10f){{
                    splashDamageRadius = 20f;
                    splashDamage = 60f;
                    status = StatusEffects.blasted;
                    statusDuration = 60f;
                    hitEffect = Fx.flakExplosionBig;
                    despawnEffect = Fx.flakExplosionBig;
                    smokeEffect = Fx.none;
                    lifetime = 40f;
                    width = 7f;
                    height = 8f;
                    trailLength = 12;
                    trailWidth = 2f;
                    trailColor = Color.valueOf("ff8a5c");
                }};
            }}
        );

        // 日蚀（eclipse）
        UnitTypes.eclipse.health = 25000f;
        UnitTypes.eclipse.armor = 20f;
        UnitTypes.eclipse.weapons.clear();
        UnitTypes.eclipse.weapons.add(
            new Weapon("死神1"){{
                reload = 30f;
                shoot.shots = 3;
                shoot.shotDelay = 4f;
                shootY = 9f;
                x = 11f;
                y = 27f;
                rotate = true;
                rotateSpeed = 3f;
                shootSound = Sounds.shoot;
                shake = 1f;
                ejectEffect = Fx.casing3;
                bullet = new BasicBulletType(6f, 80f){{
                    lifetime = 60f;
                    width = 10f;
                    height = 12f;
                    status = StatusEffects.blasted;
                    statusDuration = 120f;
                    shootEffect = Fx.shootBig;
                    splashDamageRadius = 40f;
                    splashDamage = 50f;
                    hitEffect = Fx.flakExplosionBig;
                    despawnEffect = Fx.flakExplosionBig;
                    ammoMultiplier = 6f;
                    lightningDamage = 35f;
                    lightning = 6;
                    lightningLength = 19;
                    trailLength = 12;
                    trailWidth = 2.2f;
                    trailColor = Color.valueOf("ffd7a0");
                }};
            }},
            new Weapon("死神1"){{
                reload = 36f;
                shoot.shots = 3;
                shoot.shotDelay = 4f;
                shootY = 9f;
                x = 20f;
                y = -13f;
                rotate = true;
                rotateSpeed = 3f;
                shootSound = Sounds.shoot;
                shake = 1f;
                ejectEffect = Fx.casing3;
                bullet = new BasicBulletType(6f, 60f){{
                    lifetime = 60f;
                    width = 10f;
                    height = 12f;
                    status = StatusEffects.blasted;
                    statusDuration = 120f;
                    shootEffect = Fx.shootBig;
                    splashDamageRadius = 40f;
                    splashDamage = 90f;
                    hitEffect = Fx.flakExplosionBig;
                    despawnEffect = Fx.flakExplosionBig;
                    ammoMultiplier = 6f;
                    lightningDamage = 14f;
                    lightning = 2;
                    lightningLength = 7;
                    trailLength = 12;
                    trailWidth = 2.2f;
                    trailColor = Color.valueOf("ffd7a0");
                }};
            }},
            new Weapon("死神2"){{
                reload = 40f;
                x = 18f;
                y = 5f;
                shootY = 9f;
                rotate = true;
                rotateSpeed = 1.5f;
                inaccuracy = 0f;
                alternate = true;
                shake = 3f;
                shootSound = Sounds.shootLaser;
                bullet = new LaserBulletType(350f){{
                    sideAngle = 20f;
                    sideWidth = 1f;
                    sideLength = 88f;
                    smokeEffect = Fx.bigShockwave;
                    colors = new Color[]{
                        Color.valueOf("d86e56ff"),
                        Color.valueOf("ffa05cff"),
                        Color.valueOf("ffffff")
                    };
                    width = 25f;
                    length = 360f;
                    status = StatusEffects.melting;
                    statusDuration = 90f;
                }};
            }}
        );
    }
}
