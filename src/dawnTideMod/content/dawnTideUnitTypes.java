package dawnTideMod.content;

import arc.graphics.Color;
import dawnTideMod.TideClean.segment.SegmentFollowAI;
import dawnTideMod.TideClean.segment.SegmentUnitType;
import mindustry.ai.types.GroundAI;
import mindustry.content.Fx;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.entities.pattern.ShootSpread;
import mindustry.gen.Sounds;
import mindustry.type.Weapon;

public class dawnTideUnitTypes {
    public static int BODY_COUNT = 6;
    public static float SEGMENT_SPACING = 26f;
    public static SegmentUnitType dawnWormHead;
    public static SegmentUnitType dawnWormBody;
    public static SegmentUnitType dawnWormTail;
    public static void load(){
        loadBody();
        loadTail();
        loadHead();
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

    public static void setBodyCount(int count){
        BODY_COUNT = Math.max(1, count);
        if(dawnWormHead != null){
            dawnWormHead.setSegmentCount(BODY_COUNT);
        }
    }
    public static int totalSegments(){return BODY_COUNT + 1;}
    private dawnTideUnitTypes(){}
}
