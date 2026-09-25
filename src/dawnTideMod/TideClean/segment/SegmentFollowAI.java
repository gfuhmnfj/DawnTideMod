package dawnTideMod.TideClean.segment;

import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.entities.units.AIController;
import mindustry.gen.Segmentc;
import mindustry.gen.Unit;

public class SegmentFollowAI extends AIController{
    public static final float DYING_HEALTH_FRACTION = 0.25f;
    public float alignSpeed = 0.15f;

    @Override
    public void updateMovement(){
        if(!(unit instanceof Segmentc seg)) return;
        if(seg.isHead()){
            if(fallback != null){
                fallback.unit(unit);
                fallback.updateUnit();
            }
            return;
        }

        if(!(seg.parentSegment() instanceof Unit parent)) return;
        float target = Mathf.angle(unit.x - parent.x, unit.y - parent.y);
        float cur = unit.rotation;
        float dist = Angles.angleDist(cur, target);
        if(Math.abs(dist) > 90f){
            unit.rotation = target;
        }else{
            unit.rotation = Mathf.slerpDelta(cur, target, alignSpeed);
        }
        if(seg.headSegment() instanceof Unit head){
            unit.vel.set(head.vel);
        }
    }

    @Override
    public void updateTargeting(){
        target = null;
        noTargetTime = 0f;
    }

    @Override
    public void updateWeapons(){

    }

    @Override
    public boolean keepState(){
        return false;
    }

    @Override
    public void updateVisuals(){
        if(seg() != null && seg().headSegment() instanceof Unit head){
            unit.aimX = head.aimX;
            unit.aimY = head.aimY;
            unit.isShooting = head.isShooting;
        }else{
            unit.aimX = unit.x + Tmp.v1.set(1f, 0f).rotate(unit.rotation).x;
            unit.aimY = unit.y + Tmp.v1.set(1f, 0f).rotate(unit.rotation).y;
            unit.isShooting = false;
        }
    }
    private Segmentc seg(){
        return unit instanceof Segmentc s ? s : null;
    }
}
