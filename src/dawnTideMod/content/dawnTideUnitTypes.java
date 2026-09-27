package dawnTideMod.content;

import arc.graphics.Color;
import arc.struct.ObjectSet;
import dawnTideMod.TideClean.abilities.ArcShieldAbility;
import dawnTideMod.TideClean.abilities.SpawnUnitBehindAbility;
import dawnTideMod.TideClean.segment.SegmentFollowAI;
import dawnTideMod.TideClean.segment.SegmentUnitType;
import mindustry.ai.types.GroundAI;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BombBulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.bullet.MissileBulletType;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.entities.pattern.ShootSpread;
import mindustry.gen.Sounds;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class dawnTideUnitTypes {
    public static int BODY_COUNT = 6;
    public static float SEGMENT_SPACING = 26f;
    public static SegmentUnitType dawnWormHead;
    public static SegmentUnitType dawnWormBody;
    public static SegmentUnitType dawnWormTail;
    public static UnitType jianYu;
    public static UnitType tianLin, shiXin, muMing, lieKong;
    public static void load(){
        loadBody();
        loadTail();
        loadHead();
        loadJianYu();
        loadTianlin();
        loadShixin();
        loadMuming();
        loadLiekong();
        loadVanillaAir();
    }

    private static void loadBody(){

        BasicBulletType grenade = new BasicBulletType(4.2f, 34f, "shell"){{
            width = 11f;
            height = 15f;
            lifetime = 58f;
            splashDamage = 22f;
            splashDamageRadius = 30f;
            hitSize = 7f;
            collidesTiles = false;
            frontColor = Color.valueOf("ffd59e");
            backColor   = Color.valueOf("d4813a");
            lightColor  = Color.valueOf("ffaa55");
            lightRadius = 22f;
            lightOpacity = 0.6f;
            hitEffect   = Fx.flakExplosion;
            despawnEffect = Fx.none;
            shootEffect = Fx.shootBig;
            smokeEffect = Fx.shootBigSmoke2;
            trailLength = 12;
            trailWidth  = 2.4f;
            trailColor  = Color.valueOf("ffca8a");
        }};

        dawnWormBody = new SegmentUnitType("dawn-worm-body"){{
            health = 2600f;
            armor  = 9f;
            hitSize = 19f;
            speed = 0f;
            flying = false;
            targetAir   = true;
            targetGround = true;
            rotateSpeed = 0f;
            omniMovement = false;
            drawBody = true;
            aiController = SegmentFollowAI::new;
            weapons.add(new Weapon("dawn-worm-body-w"){{
                x = 0f;
                y = 5f;
                mirror = true;
                alternate = false;
                reload = 46f;
                xRand = 5f;
                shoot.shots = 1;
                shoot.shotDelay = 4f;
                inaccuracy = 6f;
                velocityRnd = 0.12f;
                shootCone = 12f;
                bullet = grenade;
                shootSound = Sounds.shootArtillerySmall;
                rotate = false;
                ejectEffect = Fx.casing2;
            }});
            weapons.get(0).baseRotation = 90f;
        }};
    }

    private static void loadTail(){
        BasicBulletType pellet = new BasicBulletType(7.5f, 11f, "bullet"){{
            width = 7f;
            height = 11f;
            lifetime = 30f;
            hitSize = 5f;
            collidesTiles = false;
            pierce = false;
            knockback = 2.5f;
            frontColor = Color.valueOf("c9f0ff");
            backColor  = Color.valueOf("5fa8d6");
            lightColor = Color.valueOf("9fd8ff");
            lightRadius = 14f;
            lightOpacity = 0.5f;
            hitEffect  = Fx.hitBulletColor;
            trailLength = 8;
            trailWidth = 1.4f;
            trailColor = Color.valueOf("a8dcff");
        }};

        dawnWormTail = new SegmentUnitType("dawn-worm-tail"){{
            health = 1800f;
            armor  = 7f;
            hitSize = 17f;
            speed = 0f;
            targetAir = true;
            targetGround = true;
            rotateSpeed = 0f;
            omniMovement = false;

            aiController = SegmentFollowAI::new;

            weapons.add(new Weapon("dawn-worm-tail-w"){{
                x = 0f;
                y = 0f;
                mirror = false;
                reload = 62f;
                inaccuracy = 26f;
                shootCone = 40f;
                bullet = pellet;
                shootSound = Sounds.shootMalign;
                rotate = false;
                baseRotation = 180f;

                shoot = new ShootSpread(7, 11f);
            }});
        }};
    }

    private static void loadHead(){

        BasicBulletType mainShell = new BasicBulletType(9f, 78f, "shell"){{
            width = 13f;
            height = 20f;
            lifetime = 42f;
            hitSize = 9f;
            collidesTiles = false;
            pierce = true;
            pierceCap = 2;
            pierceBuilding = true;
            splashDamage = 30f;
            splashDamageRadius = 34f;
            knockback = 5f;
            frontColor = Color.valueOf("fff0c4");
            backColor  = Color.valueOf("ff8a3d");
            lightColor = Color.valueOf("ffd35a");
            lightRadius = 30f;
            lightOpacity = 0.7f;
            hitEffect  = Fx.flakExplosion;
            trailLength = 14;
            trailWidth = 3.2f;
            trailColor = Color.valueOf("ffbe72");
            shootEffect = Fx.shootTitan;
            smokeEffect = Fx.shootSmokeTitan;
        }};

        LaserBulletType twinLaser = new LaserBulletType(46f){{
            length = 130f;
            width = 11f;
            lengthFalloff = 0.55f;
            sideLength = 22f;
            sideWidth = 0.8f;
            sideAngle = 25f;
            colors = new Color[]{
                    Color.valueOf("d8f4ff"),
                    Color.valueOf("8fd0ff"),
                    Color.valueOf("5a9de0").a(0.55f)
            };
            laserEffect = Fx.hitLaserBlast;
            hitEffect = Fx.hitLaserBlast;
            hitColor = Color.valueOf("b9e6ff");
            collidesTiles = false;
            pierce = true;
            pierceCap = 3;
            pierceBuilding = true;
            knockback = -2f;
            shootEffect = Fx.shootSmokeSmite;
        }};

        SegmentUnitType head = new SegmentUnitType("dawn-worm-head"){{
            health = 4200f;
            armor  = 12f;
            hitSize = 21f;
            speed = 1.15f;
            rotateSpeed = 2.4f;
            omniMovement = true;
            flying = false;
            targetAir = true;
            targetGround = true;
            canBoost = false;
            drawBody = true;
            useEngineElevation = false;
            aiController = GroundAI::new;
            canDrown = false;
            naval = false;
            weapons.add(
                    new Weapon("dawn-worm-head-laser"){{
                        x = 8f;
                        y = 9f;
                        mirror = true;
                        alternate = true;
                        rotate = true;
                        reload = 78f;
                        rotateSpeed = 3.2f;
                        shootCone = 12f;
                        bullet = twinLaser;
                        shootSound = Sounds.shootLaser;
                        recoil = 1.6f;
                        cooldownTime = 26f;
                        shoot = new ShootAlternate(3.5f);
                    }},

                    new Weapon("dawn-worm-head-main"){{
                        x = 0f;
                        y = 13f;
                        mirror = false;
                        rotate = true;
                        reload = 150f;
                        rotateSpeed = 2.2f;
                        shootCone = 6f;
                        bullet = mainShell;
                        shootSound = Sounds.shootArtillery;
                        recoil = 3.6f;
                        recoils = 1;
                        cooldownTime = 42f;
                        shake = 2.4f;
                        inaccuracy = 1.5f;
                    }}
            );
        }};
        head.segmentCount = BODY_COUNT;
        head.segmentUnit  = dawnWormBody;
        head.segmentEndUnit = dawnWormTail;
        head.segmentSpacing = SEGMENT_SPACING;
        head.segmentRotSpeed = 6.5f;
        head.segmentMaxRot   = 26f;
        head.segmentLayerOrder = true;
        dawnWormHead = head;
    }

    // ===== 星域·歼雨（原 歼雨.json，主空t1）=====
    private static void loadJianYu(){
        jianYu = new UnitType("歼雨"){{
            localizedName = "歼雨";
            details = "主空t1";
            speed = 6f;
            accel = 0.1f;
            drag = 0.09f;
            flying = true;
            rotateSpeed = 15f;
            targetAir = true;
            circleTarget = true;
            health = 70f;
            trailLength = 15;
            trailColor = Color.valueOf("ffa763ff");

            abilities.add(new ForceFieldAbility(40f, 0.2f, 50f, 180f, 6, 0f));
            abilities.add(new RepairFieldAbility(10f, 180f, 6f));

            weapons.add(new Weapon("单击"){{
                reload = 100f;
                x = 4f;
                y = 0f;
                rotate = true;
                inaccuracy = 0f;
                alternate = true;
                top = false;
                ejectEffect = Fx.casing1;
                shoot.shots = 3;
                shoot.shotDelay = 3f;
                bullet = new BasicBulletType(5f, 5f){{
                    lifetime = 15f;
                    width = 12f;
                    height = 14f;
                    trailLength = 8;
                    trailWidth = 1.6f;
                    trailColor = Color.valueOf("ffa763");
                }};
            }});
        }};
    }

    // ===== 单位.txt 四单位（按用途+前几行描述实现，所有子弹带拖尾）=====

    /** T2 天麟：散射激光（炮口生成电磁球，球体命中/消失后散射激光电附近建筑） */
    private static void loadTianlin(){
        // 电磁球：width/height=球体大小，backColor=球体颜色（可调）
        BasicBulletType orb = new BasicBulletType(4.5f, 0f, "bullet"){{
            width = 11f;
            height = 11f;
            hitSize = 10f;
            lifetime = 34f;
            collidesTiles = false;
            frontColor = Color.valueOf("dff6ff");
            backColor = Color.valueOf("7fd0f0");
            lightColor = Color.valueOf("a9e5ff");
            lightRadius = 26f;
            lightOpacity = 0.6f;
            hitEffect = Fx.hitLaserBlast;
            despawnEffect = Fx.none;
            trailLength = 12;
            trailWidth = 2.6f;
            trailColor = Color.valueOf("9fe0ff");
            // 散射激光：一道只电一个（pierceCap=1），数量=fragBullets（可调）
            fragBullet = new LaserBulletType(13f){{
                length = 26f;
                width = 8f;
                colors = new Color[]{
                    Color.valueOf("eafaff"),
                    Color.valueOf("a9e5ff"),
                    Color.valueOf("5aa0e0").a(0.55f)
                };
                pierce = true;
                pierceCap = 1;
                pierceBuilding = true;
                collidesTiles = false;
                status = StatusEffects.corroded; // 毒雾
                statusDuration = 20f * 60f;
                hitEffect = Fx.hitLaserBlast;
            }};
            fragBullets = 2;
            fragRandomSpread = 360f;
            fragLifeMin = 1f;
            fragLifeMax = 1f;
        }};

        tianLin = new UnitType("天麟"){{
            localizedName = "天麟";
            details = "T2 空中单位，散射激光";
            health = 750f;
            armor = 3f;
            hitSize = 13f;
            speed = 2.9f; // 22格/秒
            accel = 0.1f;
            drag = 0.09f;
            flying = true;
            canBoost = false; // 不可助推
            rotateSpeed = 12f;
            itemCapacity = 20;
            range = 72f; // 9格
            targetAir = false;
            targetGround = true;
            trailLength = 10;
            trailColor = Color.valueOf("9fe0ff");
            // 能力：自身产生一个盾（盾值200，范围3格）
            abilities.add(new ForceFieldAbility(24f, 0.5f, 200f, 180f));
            // 武器：散射激光，4x 0.4/秒
            weapons.add(new Weapon("tianlin-orb"){{
                reload = 150f;
                x = 0f;
                y = 3f;
                shootY = 6f;
                rotate = true;
                shoot.shots = 4;
                shoot.shotDelay = 8f;
                inaccuracy = 5f;
                velocityRnd = 0.1f;
                bullet = orb;
                shootSound = Sounds.shootLaser;
                ejectEffect = Fx.none;
            }});
        }};
    }

    /** T3 蚀心：电磁炸弹（拖尾+闪电+麻痹），爆炸后弹射含麻痹的激光 */
    private static void loadShixin(){
        BombBulletType bomb = new BombBulletType(){{
            speed = 3.2f;
            splashDamage = 50f;
            splashDamageRadius = 16f; // ~2格
            lightning = 6;
            lightningDamage = 15f;
            status = StatusEffects.unmoving; // 麻痹
            statusDuration = 60f;
            lifetime = 34f;
            width = 10f;
            height = 16f;
            backColor = Color.valueOf("7b4fd0");
            frontColor = Color.valueOf("e6d5ff");
            hitEffect = Fx.flakExplosionBig;
            despawnEffect = Fx.flakExplosion;
            trailLength = 14;
            trailWidth = 3f;
            trailColor = Color.valueOf("c8a2ff");
            // 爆炸后发射含麻痹buff的激光
            fragBullet = new LaserBulletType(18f){{
                length = 60f;
                width = 10f;
                colors = new Color[]{
                    Color.valueOf("f2e8ff"),
                    Color.valueOf("c8a2ff"),
                    Color.valueOf("8a5ce0").a(0.55f)
                };
                pierce = true;
                pierceCap = 2;
                pierceBuilding = true;
                collidesTiles = false;
                status = StatusEffects.unmoving;
                statusDuration = 90f;
                hitEffect = Fx.hitLaserBlast;
            }};
            fragBullets = 1;
            fragLifeMin = 1f;
            fragLifeMax = 1f;
        }};

        shiXin = new UnitType("蚀心"){{
            localizedName = "蚀心";
            details = "让敌方单位原地渡劫";
            health = 2300f;
            armor = 2f;
            hitSize = 20f;
            speed = 1.85f; // 14格/秒
            accel = 0.1f;
            drag = 0.09f;
            flying = true;
            canBoost = false;
            rotateSpeed = 10f;
            itemCapacity = 80;
            range = 200f; // 25格
            targetAir = true;
            targetGround = true;
            trailLength = 12;
            trailColor = Color.valueOf("c8a2ff");
            // 能力：自御型护盾（108°弧形盾 500盾 60/秒修复 14秒冷却）
            abilities.add(new ArcShieldAbility(30f, 108f, 500f, 60f, 14f));
            // 武器：电磁炸弹，两侧各三发，0.48次/秒
            weapons.add(new Weapon("shixin-bomb"){{
                reload = 125f;
                x = 6f;
                y = -2f;
                mirror = true;
                rotate = true;
                shoot.shots = 3;
                shoot.shotDelay = 6f;
                inaccuracy = 4f;
                bullet = bomb;
                shootSound = Sounds.shootArtillery;
                ejectEffect = Fx.none;
            }});
        }};
    }

    /** T4 暮鸣：跟踪导弹 + 扇形热浪（只推敌人+燃烧）+ 一串普通子弹 */
    private static void loadMuming(){
        // 跟踪导弹
        MissileBulletType missile = new MissileBulletType(4.2f, 30f){{
            homingPower = 0.25f;
            homingRange = 60f;
            pierce = true;
            pierceCap = 3; // 3x穿透
            status = StatusEffects.wet; // 潮湿~2秒
            statusDuration = 120f;
            lifetime = 55f;
            width = 8f;
            height = 9f;
            frontColor = Color.valueOf("ffe8c4");
            backColor = Color.valueOf("ff8a3d");
            hitEffect = Fx.explosion;
            despawnEffect = Fx.explosion;
            trailLength = 12;
            trailWidth = 2.2f;
            trailColor = Color.valueOf("ffb066");
        }};

        // 热浪：无伤害，只击退敌人并点燃（子弹本身不与同队碰撞，友军天然不受影响）
        BasicBulletType heatWave = new BasicBulletType(3.2f, 0f, "bullet"){{
            width = 16f;
            height = 14f;
            knockback = 30f;
            pierce = true; // 无限穿透
            collidesTiles = false;
            status = StatusEffects.burning; // 燃烧
            statusDuration = 5f * 60f;
            frontColor = Color.valueOf("ffd39e");
            backColor = Color.valueOf("ff8a3d");
            lightColor = Color.valueOf("ff9e4d");
            lightRadius = 20f;
            hitEffect = Fx.none;
            despawnEffect = Fx.none;
            trailLength = 10;
            trailWidth = 4f;
            trailColor = Color.valueOf("ff9e4d");
            lifetime = 26f;
        }};

        // 一串普通子弹
        BasicBulletType volley = new BasicBulletType(7f, 50f){{
            width = 9f;
            height = 13f;
            lifetime = 30f;
            hitSize = 6f;
            frontColor = Color.valueOf("ffe8c4");
            backColor = Color.valueOf("e8a05a");
            hitEffect = Fx.hitBulletColor;
            trailLength = 10;
            trailWidth = 1.8f;
            trailColor = Color.valueOf("ffd7a8");
        }};

        muMing = new UnitType("暮鸣"){{
            localizedName = "暮鸣";
            details = "肃清蛮夷，威慑四方";
            health = 11000f;
            armor = 10f;
            hitSize = 28f;
            speed = 1.6f; // 12格/秒
            accel = 0.1f;
            drag = 0.09f;
            flying = true;
            canBoost = false;
            rotateSpeed = 8f;
            itemCapacity = 110;
            range = 168f; // 21格
            targetAir = true;
            targetGround = true;
            trailLength = 14;
            trailColor = Color.valueOf("ff9e4d");
            // 能力：单面力场 → 以全向力场近似（2300盾 72/秒修复）
            abilities.add(new ForceFieldAbility(34f, 1.2f, 2300f, 180f));
            weapons.add(
                // 导弹：两侧跟踪导弹
                new Weapon("muming-missile"){{
                    reload = 45f;
                    x = 9f;
                    y = 2f;
                    mirror = true;
                    rotate = true;
                    rotateSpeed = 4f;
                    shootCone = 30f;
                    shoot.shots = 3;
                    shoot.shotDelay = 3f;
                    bullet = missile;
                    shootSound = Sounds.shootMissile;
                    ejectEffect = Fx.none;
                }},
                // 热浪：扇形
                new Weapon("muming-heatwave"){{
                    reload = 45f;
                    x = 5f;
                    y = -6f;
                    mirror = true;
                    rotate = true;
                    shoot = new ShootSpread(5, 22f);
                    bullet = heatWave;
                    shootSound = Sounds.shootMalign;
                    ejectEffect = Fx.none;
                }},
                // 一串普通子弹
                new Weapon("muming-volley"){{
                    reload = 26f;
                    x = 11f;
                    y = -8f;
                    mirror = true;
                    rotate = true;
                    inaccuracy = 5f;
                    xRand = 4f;
                    shoot.shots = 3;
                    shoot.shotDelay = 2f;
                    bullet = volley;
                    shootSound = Sounds.shoot;
                    ejectEffect = Fx.casing1;
                }}
            );
        }};
    }

    /** T5 猎空：电弹（碰撞分裂8发60°，分裂弹带破甲+强化链）+ 扇形热浪 + 追踪子弹 + 泰坦式炸弹 + 四向边缘子弹 + 尾部制造歼雨 */
    private static void loadLiekong(){
        // 分裂弹：40伤害，破甲buff；命中后再弹出一发强化弹（近似40%暴击→强化链）
        BasicBulletType splitShot = new BasicBulletType(5.5f, 40f){{
            width = 7f;
            height = 10f;
            lifetime = 26f;
            hitSize = 5f;
            status = dawnTideStatuses.armorBreak; // 破甲buff
            statusDuration = 240f;
            frontColor = Color.valueOf("d8b4ff");
            backColor = Color.valueOf("9a5cff");
            hitEffect = Fx.hitBulletColor;
            trailLength = 8;
            trailWidth = 1.6f;
            trailColor = Color.valueOf("b98aff");
            fragBullet = new BasicBulletType(6f, 80f){{ // 强化暴击分裂弹：2x伤害
                width = 9f;
                height = 13f;
                lifetime = 30f;
                hitSize = 6f;
                status = dawnTideStatuses.armorBreak;
                statusDuration = 240f;
                frontColor = Color.valueOf("ffffff");
                backColor = Color.valueOf("ffd94d");
                hitEffect = Fx.hitBulletColor;
                trailLength = 10;
                trailWidth = 2f;
                trailColor = Color.valueOf("ffe27a");
            }};
            fragBullets = 1;
            fragRandomSpread = 30f;
            fragLifeMin = 1f;
            fragLifeMax = 1f;
        }};

        // 电弹（跟踪）：150伤害 2x穿透，碰撞后分裂8发60°
        MissileBulletType zapMissile = new MissileBulletType(6f, 150f){{
            homingPower = 0.3f;
            homingRange = 80f;
            pierce = true;
            pierceCap = 2;
            lifetime = 60f;
            width = 9f;
            height = 11f;
            frontColor = Color.valueOf("eaffff");
            backColor = Color.valueOf("35d0ff");
            hitEffect = Fx.hitLaserBlast;
            despawnEffect = Fx.hitLaserBlast;
            trailLength = 14;
            trailWidth = 2.6f;
            trailColor = Color.valueOf("7fe3ff");
            fragBullet = splitShot;
            fragBullets = 8;
            fragRandomSpread = 60f; // 60°角内
            fragLifeMin = 1f;
            fragLifeMax = 1f;
        }};

        // 追踪普通子弹
        BasicBulletType tracker = new BasicBulletType(7.5f, 100f){{
            homingPower = 0.2f;
            homingRange = 60f;
            width = 10f;
            height = 14f;
            lifetime = 40f;
            hitSize = 6f;
            frontColor = Color.valueOf("e0f7ff");
            backColor = Color.valueOf("4db8ff");
            hitEffect = Fx.hitBulletColor;
            trailLength = 12;
            trailWidth = 2f;
            trailColor = Color.valueOf("8fd4ff");
        }};

        // 泰坦式炸弹
        BombBulletType titanBomb = new BombBulletType(){{
            speed = 2.5f;
            splashDamage = 300f;
            splashDamageRadius = 30f;
            lifetime = 40f;
            width = 12f;
            height = 18f;
            backColor = Color.valueOf("d04f4f");
            frontColor = Color.valueOf("ffd5d5");
            hitEffect = Fx.flakExplosionBig;
            despawnEffect = Fx.flakExplosionBig;
            trailLength = 12;
            trailWidth = 3f;
            trailColor = Color.valueOf("ff6a6a");
        }};

        // 四方向边缘普通子弹
        BasicBulletType sideBullet = new BasicBulletType(7f, 60f){{
            width = 8f;
            height = 12f;
            lifetime = 26f;
            hitSize = 5f;
            frontColor = Color.valueOf("eaffff");
            backColor = Color.valueOf("5cd0d0");
            hitEffect = Fx.hitBulletColor;
            trailLength = 8;
            trailWidth = 1.5f;
            trailColor = Color.valueOf("9fe0e0");
        }};

        // 复用暮鸣的热浪子弹
        BasicBulletType heatWave = (BasicBulletType)muMing.weapons.get(1).bullet;

        lieKong = new UnitType("猎空"){{
            localizedName = "猎空";
            details = "T5 空中堡垒";
            health = 26000f;
            armor = 10f;
            hitSize = 32f;
            speed = 1.6f; // 12格/秒
            accel = 0.1f;
            drag = 0.09f;
            flying = true;
            canBoost = false;
            rotateSpeed = 8f;
            itemCapacity = 110;
            range = 168f; // 21格
            targetAir = true;
            targetGround = true;
            trailLength = 16;
            trailColor = Color.valueOf("7fe3ff");
            // 能力：弧形盾60° 4000盾 72/秒修复 + 修复力场30格600/秒 + 尾部制造歼雨
            abilities.add(
                new ArcShieldAbility(40f, 60f, 4000f, 72f, 10f),
                new RepairFieldAbility(600f, 60f, 240f),
                new SpawnUnitBehindAbility(jianYu, 12f)
            );
            weapons.addAll(
                // 电弹：2x 4/秒 两侧
                new Weapon("liekong-zap"){{
                    reload = 30f;
                    x = 10f;
                    y = 4f;
                    mirror = true;
                    rotate = true;
                    rotateSpeed = 6f;
                    shoot.shots = 2;
                    shoot.shotDelay = 4f;
                    bullet = zapMissile;
                    shootSound = Sounds.shootLaser;
                    ejectEffect = Fx.none;
                }},
                // 扇形热浪
                new Weapon("liekong-heatwave"){{
                    reload = 50f;
                    x = 6f;
                    y = -8f;
                    mirror = true;
                    rotate = true;
                    shoot = new ShootSpread(5, 24f);
                    bullet = heatWave;
                    shootSound = Sounds.shootMalign;
                    ejectEffect = Fx.none;
                }},
                // 追踪普通子弹 两侧
                new Weapon("liekong-tracker"){{
                    reload = 28f;
                    x = 13f;
                    y = -2f;
                    mirror = true;
                    rotate = true;
                    bullet = tracker;
                    shootSound = Sounds.shoot;
                    ejectEffect = Fx.casing1;
                }},
                // 泰坦式炸弹
                new Weapon("liekong-bomb"){{
                    reload = 100f;
                    x = 0f;
                    y = -6f;
                    rotate = true;
                    inaccuracy = 8f;
                    bullet = titanBomb;
                    shootSound = Sounds.shootArtillery;
                    ejectEffect = Fx.none;
                }},
                // 四方向边缘普通子弹（rotate=false，baseRotation 定方向）
                fourWayWeapon(sideBullet, 0f, 0f, 14f),
                fourWayWeapon(sideBullet, 90f, 14f, 0f),
                fourWayWeapon(sideBullet, 180f, 0f, -14f),
                fourWayWeapon(sideBullet, 270f, -14f, 0f)
            );
        }};
    }

    private static Weapon fourWayWeapon(BasicBulletType bullet, float baseRotation, float x, float y){
        return new Weapon("liekong-side"){{
            this.baseRotation = baseRotation;
            this.x = x;
            this.y = y;
            rotate = false;
            mirror = false;
            top = false;
            shootCone = 190f;
            reload = 40f;
            inaccuracy = 5f;
            this.bullet = bullet;
            shootSound = Sounds.shoot;
            ejectEffect = Fx.none;
        }};
    }

    // ===== 星域·原版主空改版（原 content/units/原版主空 JSON 转 Java，直接修改原版单位字段）=====
    private static void loadVanillaAir(){
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

        // 月影（antumbra）
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

    public static void setBodyCount(int count){
        BODY_COUNT = Math.max(1, count);
        if(dawnWormHead != null){
            dawnWormHead.setSegmentCount(BODY_COUNT);
        }
    }
    public static int totalSegments(){return BODY_COUNT + 1;}
    private dawnTideUnitTypes(){}
}
