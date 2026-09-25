package dawnTideMod.content;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Scaling;
import arc.util.Strings;
import dawnTideMod.TideClean.Bullet.DawnCritBulletType;
import mindustry.content.Bullets;
import mindustry.content.Items;
import mindustry.content.StatusEffects;
import mindustry.entities.UnitSorts;
import mindustry.entities.bullet.BulletType;
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
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValue;

public class DawnTurrets{
    public static ItemTurret tideCannon;
    public static ContinuousTurret prism;
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
                        Items.copper, DawnBullets.tideCrit,
                        Items.graphite, DawnBullets.tideCritHeavy
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
                DawnTideItems.steel, 550,
                Items.silicon, 400,
                DawnTideItems.ceramicGlass, 800,
                DawnTideItems.boundaryBreakingAlloy, 400
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
