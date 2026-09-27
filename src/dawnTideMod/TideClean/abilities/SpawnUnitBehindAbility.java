package dawnTideMod.TideClean.abilities;

import arc.math.Angles;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

/** 周期性在单位后方生成指定单位（如：猎空尾部制造歼雨） */
public class SpawnUnitBehindAbility extends Ability {
    /** 生成的单位类型 */
    public UnitType unit;
    /** 生成间隔（秒） */
    public float period = 12f;
    /** 后方生成距离 */
    public float spawnDist = 14f;

    private float timer;

    public SpawnUnitBehindAbility(UnitType unit, float periodSeconds){
        this.unit = unit;
        this.period = periodSeconds;
    }

    @Override
    public void update(Unit unit){
        if(this.unit == null || !Vars.state.isPlaying()) return;

        timer += Time.delta;
        if(timer >= period * 60f){
            timer = 0f;
            float ang = unit.rotation + 180f;
            float sx = unit.x + Angles.trnsx(ang, spawnDist);
            float sy = unit.y + Angles.trnsy(ang, spawnDist);
            Unit spawned = this.unit.spawn(unit.team, sx, sy);
            if(spawned != null){
                spawned.rotation = ang;
                spawned.vel.trns(ang, unit.vel.len() + 0.5f);
            }
        }
    }

    @Override
    public String localized(){
        return "制造" + (unit == null ? "" : unit.localizedName);
    }
}
