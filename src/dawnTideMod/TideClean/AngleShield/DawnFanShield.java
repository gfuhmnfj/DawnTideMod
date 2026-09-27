package dawnTideMod.TideClean.AngleShield;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
public class DawnFanShield extends DawnShieldBase{
    public DawnFanShield(String name){
        super(name);
        repelUnits = true;
    }

    @Override
    public boolean checkAbsorb(float dx, float dy, float dist, float rad, int rotation){
        return dist <= rad && inArcAngle(dx, dy, rotation);
    }
    @Override
    public void drawShieldShape(Building build, float facing, float rad, float hit){
        float start = facing - arcAngle / 2f;
        float frac = arcAngle / 360f;
        if(Vars.renderer.animateShields){
            Draw.z(Layer.shields + 0.001f * hit);
            Fill.arc(build.x, build.y, rad, frac, start, 40);
        }else{
            Lines.stroke(1.5f);
            Draw.alpha(0.09f + Mathf.clamp(0.08f * hit));
            Fill.arc(build.x, build.y, rad, frac, start, 40);
            Draw.alpha(1f);
            Lines.arc(build.x, build.y, rad, frac, start);
            Lines.line(build.x, build.y, build.x + Angles.trnsx(start, rad), build.y + Angles.trnsy(start, rad));
            Lines.line(build.x, build.y, build.x + Angles.trnsx(start + arcAngle, rad), build.y + Angles.trnsy(start + arcAngle, rad));
            Draw.reset();
        }
    }
}
