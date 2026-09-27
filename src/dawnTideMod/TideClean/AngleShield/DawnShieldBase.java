package dawnTideMod.TideClean.AngleShield;

import arc.audio.Sound;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.gen.Bullet;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.ExplosionShield;
import mindustry.world.blocks.defense.BaseShield;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

import static mindustry.Vars.tilesize;

import static arc.util.Time.delta;

/**
 * 定向护盾基类（继承原版 BaseShield）。
 * <p>与原版圆形护盾不同：护盾只覆盖以建筑朝向为中心的 {@link #arcAngle} 度扇区。
 * <p>核心字段：
 * <ul>
 *   <li>{@link #radius} —— 盾范围（世界坐标，1 格 = 8）</li>
 *   <li>{@link #arcAngle} —— 控制角度：护盾覆盖的总角度（度），旋转摆放决定朝向</li>
 *   <li>{@link #shieldHealth} —— 盾耐久，被打满后破碎，随时间回充</li>
 * </ul>
 * 子类只需覆写 {@link #checkAbsorb} / {@link #drawShieldShape} 即可实现不同形状（弧形/扇形）。
 */
public abstract class DawnShieldBase extends BaseShield{
    /** 控制角度：护盾覆盖的总角度（度）。180 = 半圆，360 = 退化为整圆。 */
    public float arcAngle = 120f;
    /** 朝向微调（度），叠加在摆放旋转之上，用于精细对准。 */
    public float rotationOffset = 0f;
    /** 盾耐久：累计吸收伤害达到该值后护盾破碎。 */
    public float shieldHealth = 900f;
    /** 完好时每秒回充的耐久。 */
    public float cooldownNormal = 1.75f;
    /** 破碎后每秒回充的耐久（回满后护盾重新展开）。 */
    public float cooldownBrokenBase = 0.35f;
    /** 是否推开盾区内的敌方单位（扇形盾开启，弧形盾关闭）。 */
    public boolean repelUnits = false;
    public Effect absorbEffect = Fx.absorb;
    public Effect shieldBreakEffect = Fx.shieldBreak;
    public Sound breakSound = Sounds.shieldBreak;
    public Sound hitSound = Sounds.shieldHit;
    public float hitSoundVolume = 0.12f;

    // 供静态 lambda 使用的临时引用（原版 BaseShield 同款模式，避免每帧分配）
    protected static DawnShieldBase paramBase;
    protected static DawnShieldBuild paramBuild;

    protected static final Cons<Bullet> bulletConsumer = bullet -> {
        if(bullet.team != paramBuild.team && bullet.type.absorbable && !bullet.absorbed){
            float dx = bullet.x - paramBuild.x, dy = bullet.y - paramBuild.y;
            float rad = paramBuild.radius();
            if(dx * dx + dy * dy <= rad * rad + 64f && paramBase.checkAbsorb(dx, dy, Mathf.dst(dx, dy), rad, paramBuild.rotation)){
                bullet.absorb();
                paramBuild.hit = 1f;
                paramBuild.buildup += bullet.type.shieldDamage(bullet);
                paramBase.absorbEffect.at(bullet);
                if(Mathf.chance(0.06f)){
                    paramBase.hitSound.at(bullet.x, bullet.y, 1f + Mathf.range(0.1f), paramBase.hitSoundVolume);
                }
            }
        }
    };

    protected static final Cons<Unit> unitConsumer = unit -> {
        float rad = paramBuild.radius();
        float dist = unit.dst(paramBuild);
        float overlapDst = (unit.hitSize / 2f + rad) - dist;
        if(overlapDst > 0 && paramBase.checkRepel(unit.x - paramBuild.x, unit.y - paramBuild.y, dist, rad, paramBuild.rotation)){
            if(overlapDst > unit.hitSize * 1.5f){
                unit.kill();
            }else{
                unit.vel.setZero();
                unit.move(Tmp.v1.set(unit).sub(paramBuild).setLength(overlapDst + 0.01f));
                if(Mathf.chanceDelta(0.12f * Time.delta)){
                    Fx.circleColorSpark.at(unit.x, unit.y, paramBuild.team.color);
                }
            }
        }
    };

    public DawnShieldBase(String name){
        super(name);
        rotate = true;
    }

    /** 护盾朝向（度）：摆放旋转(0-3) * 90 + 微调。 */
    public float facingDeg(int rotation){
        return rotation * 90f + rotationOffset;
    }

    /** 点是否落在朝向扇区角度内。 */
    public boolean inArcAngle(float dx, float dy, int rotation){
        return Angles.angleDist(Mathf.angle(dx, dy), facingDeg(rotation)) <= arcAngle / 2f;
    }

    /** 子弹吸收判定：相对坐标 dx/dy、到圆心距离 dist、当前半径 rad、建筑朝向 rotation。 */
    public abstract boolean checkAbsorb(float dx, float dy, float dist, float rad, int rotation);

    /** 单位推开判定：默认仅当 repelUnits 且在扇区内。 */
    public boolean checkRepel(float dx, float dy, float dist, float rad, int rotation){
        return repelUnits && dist <= rad && inArcAngle(dx, dy, rotation);
    }

    /** 绘制护盾形状（子类实现：弧形 = 双弧线，扇形 = 扇面填充）。 */
    public abstract void drawShieldShape(Building build, float facing, float rad, float hit);

    @Override
    public void setStats(){
        super.setStats();
        stats.add(Stat.shieldHealth, shieldHealth, StatUnit.none);
        stats.add(Stat.regenerationRate, cooldownNormal * 60f, StatUnit.perSecond);
        stats.add(Stat.booster, table -> {
            table.add("[lightgray]覆盖角度：[stat]" + (int)arcAngle + "°[]，朝向随摆放旋转");
        });
    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("shield", (DawnShieldBuild entity) ->
            new Bar("stat.shieldhealth", Pal.accent,
                () -> entity.broken ? 0f : 1f - entity.buildup / shieldHealth).blink(Color.white));
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
        float wx = x * tilesize + offset, wy = y * tilesize + offset;
        float facing = facingDeg(rotation);
        float start = facing - arcAngle / 2f;
        Draw.color(Vars.player.team().color);
        Lines.stroke(1.5f);
        Lines.arc(wx, wy, radius, arcAngle / 360f, start);
        Lines.line(wx, wy, wx + Angles.trnsx(start, radius), wy + Angles.trnsy(start, radius));
        Lines.line(wx, wy, wx + Angles.trnsx(start + arcAngle, radius), wy + Angles.trnsy(start + arcAngle, radius));
        Draw.reset();
    }

    public class DawnShieldBuild extends Building implements ExplosionShield{
        public boolean broken = false;
        public float buildup = 0f;
        public float hit = 0f;
        public float smoothRadius = 0f;

        public float radius(){
            return smoothRadius;
        }

        @Override
        public void updateTile(){
            DawnShieldBase b = (DawnShieldBase)block;
            smoothRadius = Mathf.lerpDelta(smoothRadius, b.radius * efficiency, 0.05f);

            if(buildup > 0){
                buildup -= delta() * (broken ? b.cooldownBrokenBase : b.cooldownNormal);
            }

            if(broken && buildup <= 0){
                broken = false;
            }

            if(!broken && b.shieldHealth > 0 && buildup >= b.shieldHealth){
                broken = true;
                buildup = b.shieldHealth;
                b.shieldBreakEffect.at(x, y, radius(), team.color);
                b.breakSound.at(x, y);
            }

            if(hit > 0f){
                hit -= 1f / 5f * Time.delta;
            }

            float rad = radius();
            if(rad > 1f && !broken){
                paramBase = b;
                paramBuild = this;
                Groups.bullet.intersect(x - rad, y - rad, rad * 2f, rad * 2f, bulletConsumer);
                if(b.repelUnits){
                    Units.nearbyEnemies(team, x, y, rad + 10f, unitConsumer);
                }
            }
        }

        @Override
        public boolean absorbExplosion(float ex, float ey, float damage){
            DawnShieldBase b = (DawnShieldBase)block;
            float rad = radius();
            float dx = ex - x, dy = ey - y;
            boolean absorb = !broken && rad > 1f && b.checkAbsorb(dx, dy, Mathf.dst(dx, dy), rad, rotation);
            if(absorb){
                b.absorbEffect.at(ex, ey);
                hit = 1f;
                buildup += damage;
            }
            return absorb;
        }

        @Override
        public void draw(){
            super.draw();
            if(!broken){
                float rad = radius();
                if(rad > 0.001f){
                    Draw.z(Layer.shields);
                    Draw.color(shieldColor == null ? team.color : shieldColor, Color.white, Mathf.clamp(hit));
                    ((DawnShieldBase)block).drawShieldShape(this, ((DawnShieldBase)block).facingDeg(rotation), rad, hit);
                    Draw.reset();
                }
            }
        }

        @Override
        public void drawSelect(){
            super.drawSelect();
            DawnShieldBase b = (DawnShieldBase)block;
            float facing = b.facingDeg(rotation);
            float start = facing - b.arcAngle / 2f;
            Draw.color(team.color);
            Lines.stroke(1f);
            Lines.arc(x, y, radius(), b.arcAngle / 360f, start);
            Lines.line(x, y, x + Angles.trnsx(start, radius()), y + Angles.trnsy(start, radius()));
            Lines.line(x, y, x + Angles.trnsx(start + b.arcAngle, radius()), y + Angles.trnsy(start + b.arcAngle, radius()));
            Draw.reset();
        }

        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void write(arc.util.io.Writes write){
            super.write(write);
            write.f(buildup);
            write.bool(broken);
            write.f(smoothRadius);
        }

        @Override
        public void read(arc.util.io.Reads read, byte revision){
            super.read(read, revision);
            if(revision >= 1){
                buildup = read.f();
                broken = read.bool();
                smoothRadius = read.f();
            }
        }
    }
}
