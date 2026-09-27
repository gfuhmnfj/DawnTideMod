package dawnTideMod.TideClean.abilities;

import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;

/** 单位弧形盾：只格挡单位朝向 ±arcAngle/2 范围内的子弹（盾面跟随单位转向） */
public class ArcShieldAbility extends Ability {
    /** 盾半径 */
    public float radius = 24f;
    /** 弧形角度 */
    public float arcAngle = 108f;
    /** 最大盾值 */
    public float max = 500f;
    /** 修复速度（每 tick） */
    public float regen = 1f;
    /** 破盾后冷却（tick） */
    public float cooldown = 300f;

    protected float alpha;
    protected boolean wasBroken = true;

    private static Unit paramUnit;
    private static ArcShieldAbility paramShield;

    private static final Cons<Bullet> shieldConsumer = b -> {
        if(b.team != paramUnit.team && b.type.absorbable && paramUnit.shield > 0
            && b.dst(paramUnit) <= paramShield.radius
            && Angles.within(paramUnit.rotation, paramUnit.angleTo(b.x(), b.y()), paramShield.arcAngle / 2f)){
            b.absorb();
            Fx.absorb.at(b);
            Sounds.shieldHit.at(b.x, b.y, 1f + Mathf.range(0.1f), 0.12f);
            paramUnit.shield -= b.type().shieldDamage(b);
            paramShield.alpha = 1f;
        }
    };

    public ArcShieldAbility(float radius, float arcAngle, float max, float regenPerSecond, float cooldownSeconds){
        this.radius = radius;
        this.arcAngle = arcAngle;
        this.max = max;
        this.regen = regenPerSecond / 60f;
        this.cooldown = cooldownSeconds * 60f;
    }

    public float scaledMax(Unit unit){
        return max * Vars.state.rules.unitHealth(unit.team);
    }

    @Override
    public void update(Unit unit){
        if(unit.shield <= 0f && !wasBroken){
            // 破盾：把盾值压到负值，修复到 0 所需时间即冷却
            unit.shield -= cooldown * regen;
            Fx.shieldBreak.at(unit.x, unit.y, radius, unit.type.shieldColor(unit), this);
            Sounds.shieldBreakSmall.at(unit.x, unit.y);
        }
        wasBroken = unit.shield <= 0f;

        if(unit.shield < scaledMax(unit)){
            unit.shield += Time.delta * regen;
        }

        alpha = Math.max(alpha - Time.delta / 10f, 0f);

        if(unit.shield > 0){
            paramUnit = unit;
            paramShield = this;
            Groups.bullet.intersect(unit.x - radius, unit.y - radius, radius * 2f, radius * 2f, shieldConsumer);
        }
    }

    @Override
    public void draw(Unit unit){
        if(unit.shield <= 0) return;

        Draw.color(unit.type.shieldColor(unit), Color.white, Mathf.clamp(alpha));
        float frac = arcAngle / 360f;
        float start = unit.rotation - arcAngle / 2f;

        if(Vars.renderer.animateShields){
            Draw.z(Layer.shields + 0.001f * alpha);
            Draw.alpha(0.22f);
            Fill.arc(unit.x, unit.y, radius, frac, start);
        }else{
            Draw.z(Layer.shields);
            Lines.stroke(1.5f);
            Draw.alpha(0.09f);
            Fill.arc(unit.x, unit.y, radius, frac, start);
            Draw.alpha(1f);
            Lines.arc(unit.x, unit.y, radius, frac, start);
        }
        Draw.reset();
    }

    @Override
    public void displayBars(Unit unit, Table bars){
        bars.add(new Bar("stat.shieldhealth", Pal.accent, () -> unit.shield / scaledMax(unit))).row();
    }

    @Override
    public String localized(){
        return "弧形盾";
    }

    @Override
    public void addStats(Table t){
        super.addStats(t);
        t.add("范围: " + (radius / 8f) + "格").row();
        t.add("护盾: " + (int)max).row();
        t.add("修复: " + (int)(regen * 60f) + "/秒").row();
        t.add("角度: " + (int)arcAngle + "°").row();
    }
}
