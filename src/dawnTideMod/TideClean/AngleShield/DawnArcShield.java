package dawnTideMod.TideClean.AngleShield;

import arc.graphics.g2d.Lines;
import mindustry.gen.Building;

public class DawnArcShield extends DawnShieldBase{
    public float thickness = 10f;
    public DawnArcShield(String name){
        super(name);
        repelUnits = false;
    }

    @Override
    public boolean checkAbsorb(float dx, float dy, float dist, float rad, int rotation){
        return inArcAngle(dx, dy, rotation) && Math.abs(dist - rad) <= thickness / 2f;
    }

    @Override
    public void drawShieldShape(Building build, float facing, float rad, float hit){
        float start = facing - arcAngle / 2f;
        float frac = arcAngle / 360f;
        Lines.stroke(1.5f);
        Lines.arc(build.x, build.y, rad + thickness / 2f, frac, start);
        Lines.arc(build.x, build.y, Math.max(rad - thickness / 2f, 1f), frac, start);
        Lines.arc(build.x, build.y, rad, frac, start);
    }
}
