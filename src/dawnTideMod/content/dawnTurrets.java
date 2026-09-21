package dawnTideMod.content;

import arc.graphics.Color;
import mindustry.content.Bullets;
import mindustry.content.Items;
import mindustry.entities.UnitSorts;
import mindustry.entities.bullet.PointLaserBulletType;
import mindustry.entities.part.DrawPart.PartMove;
import mindustry.entities.part.DrawPart.PartProgress;
import mindustry.entities.part.RegionPart;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.defense.turrets.ContinuousTurret;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.Env;

public class DawnTurrets{

    public static ItemTurret tideCannon;

    public static ContinuousTurret prism;

    public static void load(){
        tideCannon = new ItemTurret("tide-cannon"){{
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
        }};

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
}
