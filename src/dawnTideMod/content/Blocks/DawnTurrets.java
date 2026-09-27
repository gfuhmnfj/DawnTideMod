package dawnTideMod.content.Blocks;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Scaling;
import arc.util.Strings;
import dawnTideMod.TideClean.Bullet.DawnCritBulletType;
import dawnTideMod.content.dawnBullets;
import dawnTideMod.content.dawnTideItems;
import mindustry.content.Bullets;
import mindustry.content.Items;
import mindustry.content.StatusEffects;
import mindustry.entities.UnitSorts;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.entities.bullet.PointLaserBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.Styles;
import mindustry.world.blocks.defense.turrets.ContinuousTurret;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValue;

public class DawnTurrets{
    public static ItemTurret tideCannon;
    public static ContinuousTurret prism;
    public static PowerTurret flicker;
    public static ItemTurret haze;
    public static void load(){
        tideCannon = new ItemTurret("tide-cannon"){
            {
                localizedName = "测试炮台";
                requirements(Category.turret, ItemStack.with(Items.copper, 120, Items.graphite, 80));
                health = 420;
                size = 2;
                range = 220f;
                reload = 35f;
                inaccuracy = 3f;
                rotateSpeed = 6f;
                ammo(
                        Items.copper, dawnBullets.tideCrit,
                        Items.graphite, dawnBullets.tideCritHeavy
                );
                limitRange(2f);
            }
            @Override
            public void setStats(){
                super.setStats();
                stats.replace(Stat.ammo, critAmmo(ammoTypes));
            }
        };

        prism = new ContinuousTurret("prism"){{
            localizedName = "棱镜";
            description = "向敌人发射慢速单目标激光。";
            requirements(Category.turret, ItemStack.with(
                dawnTideItems.steel, 550,
                Items.silicon, 400,
                dawnTideItems.ceramicGlass, 800,
                dawnTideItems.boundaryBreakingAlloy, 400
            ));
            squareSprite = false;
            size = 4;
            health = 2750;
            scaledHealth = 210f;
            buildTime = 1200f;
            offset = 4f;
            sizeOffset = -1;
            clipSize = 32f;
            lightClipSize = 32f;
            placeOverlapRange = 306f;
            consumePower(32f);
            range = 240f;
            rotateSpeed = 9f;
            newTargetInterval = 20f;
            shootCone = 360f;
            shootY = 0.5f;
            trackingRange = 250f;
            unitSort = UnitSorts.strongest;
            recoilTime = 10f;
            elevation = 2f;
            shootWarmupSpeed = 10f;
            aimChangeSpeed = 10f;
            scaleDamageEfficiency = true;
            shootSound = Sounds.none;
            loopSound = Sounds.beamLustre;
            loopSoundVolume = 1f;
            outlineColor = Pal.darkOutline;
            envEnabled |= Env.space;
            shootType = new PointLaserBulletType(){{
                damage = 60f;
                buildingDamageMultiplier = 0.3f;
                hitColor = Color.valueOf("fda981");
                lightningType = Bullets.damageLightning;
                lightRadius = 20f;
            }};

            drawer = new DrawTurret("reinforced-"){{
                parts.add(new RegionPart("-blade"){{
                    turretShading = true;
                    under = true;
                    mirror = true;
                    heatColor = Color.valueOf("ff6214");
                    moveX = 2f;
                    moveRot = -7f;
                    moves.add(new PartMove(PartProgress.warmup, 0f, -2f, 3f));
                }},
                new RegionPart("-inner"){{
                    turretShading = true;
                    mirror = true;
                    heatColor = Color.valueOf("ff6214");
                    moveX = 2f;
                    moveY = -8f;
                }},
                new RegionPart("-mid"){{
                    turretShading = true;
                    under = true;
                    heatColor = Color.valueOf("ff6214");
                    moveY = -8f;
                }});
            }};
        }};

        flicker = new PowerTurret("Flicker"){{
            localizedName = "闪烁";
            description = "向敌人发射一颗电球，碰撞爆炸后散射大量电弧";
            requirements(Category.turret, ItemStack.with(
                dawnTideItems.steel, 70, Items.titanium, 140, Items.silicon, 80));
            health = 1200;
            size = 3;
            buildTime = 134f;
            range = 160f;
            reload = 46.2f;
            inaccuracy = 3f;
            rotateSpeed = 6f;
            targetAir = false;
            targetGround = true;
            liquidCapacity = 20f;
            shootSound = Sounds.shootArc;
            // 冷却强化：12液体/秒，水160% / 冷冻液235%（coolantMultiplier=7.5 反推自规格值）
            coolantMultiplier = 7.5f;
            coolant = consumeCoolant(0.2f);
            shootType = new BasicBulletType(3f, 10f){{
                // 电球：3格大小，10伤害，3格爆炸范围，迟缓1秒
                width = 18f;
                height = 18f;
                shrinkX = 0f;
                shrinkY = 0f;
                hitSize = 10f;
                lifetime = 56f;
                splashDamage = 10f;
                splashDamageRadius = 24f;
                status = StatusEffects.slow;
                statusDuration = 60f;
                backColor = Color.valueOf("7d8dff");
                frontColor = Color.valueOf("c4d7ff");
                hitColor = Color.valueOf("c4d7ff");
                // 爆炸后散射 8 道电弧：20伤害 30穿透 电击3秒
                fragBullets = 8;
                fragRandomSpread = 0f;
                fragSpread = 45f;
                fragBullet = new BasicBulletType(4f, 20f){{
                    width = 7f;
                    height = 12f;
                    lifetime = 26f;
                    pierce = true;
                    pierceCap = 30;
                    status = StatusEffects.shocked;
                    statusDuration = 180f;
                    backColor = Color.valueOf("a8b6ff");
                    frontColor = Color.valueOf("e8ecff");
                }};
            }};
            limitRange(2f);
        }};

        haze = new ItemTurret("Haze"){{
            localizedName = "阴霾";
            description = "使用多类型弹药精准打击机械单位，以毁伤目标。";
            requirements(Category.turret, ItemStack.with(
                Items.copper,220,Items.graphite,90,Items.titanium,140,
                Items.silicon, 90, dawnTideItems.ceramicGlass, 45));
            health = 1420;
            size = 3;
            buildTime = 210f;
            range = 228f;
            reload = 9.4f;
            inaccuracy = 5f;
            rotateSpeed = 8f;
            targetAir = true;
            targetGround = true;
            liquidCapacity = 30f;
            shootSound = Sounds.shootSalvo;
            coolantMultiplier = 7.5f;
            coolant = consumeCoolant(0.2f);

            // 铅：30伤害 0.8击退
            BulletType hazeLead = new BasicBulletType(4f, 30f){{
                width = 8f;
                height = 10f;
                lifetime = 60f;
                knockback = 0.8f;
                backColor = Color.valueOf("6e7080");
                frontColor = Color.valueOf("dfe3ee");
            }};

            // 石墨：48伤害 x2装填 射程+3格 1.2x射速 0.2击退 x2穿透
            BulletType hazeGraphite = new BasicBulletType(4f, 48f){{
                width = 9f;
                height = 12f;
                lifetime = 60f;
                ammoMultiplier = 2f;
                reloadMultiplier = 1.2f;
                rangeChange = 24f;
                knockback = 0.2f;
                pierce = true;
                pierceCap = 2;
                backColor = Color.valueOf("4a4f5a");
                frontColor = Color.valueOf("9aa0a6");
            }};

            // 硅：42伤害 x4装填 追踪720°/s~7格 1.6x射速
            BulletType hazeSilicon = new BasicBulletType(4f, 42f){{
                width = 8f;
                height = 12f;
                lifetime = 60f;
                ammoMultiplier = 4f;
                reloadMultiplier = 1.6f;
                homingPower = 0.24f;
                homingRange = 56f;
                backColor = Color.valueOf("3f6f8f");
                frontColor = Color.valueOf("8fd3ff");
            }};

            // 钛：70伤害 x4装填 0.2击退 2x闪电~8伤害~3长度 电击1秒
            BulletType hazeTitanium = new BasicBulletType(4.5f, 70f){{
                width = 9f;
                height = 12f;
                lifetime = 60f;
                ammoMultiplier = 4f;
                knockback = 0.2f;
                lightning = 2;
                lightningDamage = 8f;
                lightningLength = 3;
                lightningType = new LightningBulletType(){{
                    status = StatusEffects.shocked;
                    statusDuration = 60f;
                }};
                backColor = Color.valueOf("5a4f8f");
                frontColor = Color.valueOf("b5a8ff");
            }};

            // 钍：90伤害 x3装填 0.5击退 x3穿透
            BulletType hazeThorium = new BasicBulletType(4.5f, 90f){{
                width = 10f;
                height = 13f;
                lifetime = 60f;
                ammoMultiplier = 3f;
                knockback = 0.5f;
                pierce = true;
                pierceCap = 3;
                backColor = Color.valueOf("3f7f5f");
                frontColor = Color.valueOf("9cffb0");
            }};

            ammo(
                Items.lead, hazeLead,
                Items.graphite, hazeGraphite,
                Items.silicon, hazeSilicon,
                Items.titanium, hazeTitanium,
                Items.thorium, hazeThorium
            );
            limitRange(5f);
        }};
    }

    private static StatValue critAmmo(ObjectMap<Item, BulletType> map){
        return table -> {
            table.row();

            Seq<Item> keys = map.keys().toSeq();
            keys.sort();

            for(Item item : keys){
                BulletType type = map.get(item);

                table.table(Styles.grayPanel, bt -> {
                    bt.left().top().defaults().padRight(3).left();

                    bt.table(title -> {
                        title.image(item.uiIcon).size(3 * 8).padRight(4).scaling(Scaling.fit).top();
                        title.add(item.localizedName).padRight(10).left().top();
                    });
                    bt.row();

                    if(type.damage > 0 && (type.collides || type.splashDamage <= 0)){
                        bt.add(Core.bundle.format("bullet.damage", type.damage));
                    }

                    if(type.statLiquidConsumed <= 0f && !Mathf.equal(type.ammoMultiplier, 1f) && type.displayAmmoMultiplier){
                        bt.row();
                        bt.add(Core.bundle.format("bullet.multiplier", (int)type.ammoMultiplier));
                    }

                    if(type.knockback > 0){
                        bt.row();
                        bt.add(Core.bundle.format("bullet.knockback", Strings.autoFixed(type.knockback, 2)));
                    }

                    if(type.status != StatusEffects.none){
                        bt.row();
                        bt.add((type.status.hasEmoji() ? type.status.emoji() : "") + "[stat]" + type.status.localizedName +
                            "[lightgray] ~ [stat]" + Strings.autoFixed(type.statusDuration / 60f, 1) +
                            "[lightgray] " + Core.bundle.get("unit.seconds"));
                    }

                    if(type instanceof DawnCritBulletType crit){
                        bt.row();
                        bt.add("[stat]" + (int)(crit.critChance * 100f) + "%[] [lightgray]暴击概率");
                        if(!Mathf.equal(crit.critMultiplier, 1f)){
                            bt.row();
                            bt.add("[stat]" + Strings.autoFixed(crit.critMultiplier, 1) + "x[] [lightgray]暴击伤害");
                        }
                    }
                }).padLeft(5).padTop(5).padBottom(5).growX().margin(10);
                table.row();
            }
        };
    }
}
