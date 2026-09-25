package dawnTideMod.TideClean.Wall;

import arc.math.Mathf;
import arc.util.Strings;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.Stat;

public class RegenWall extends Wall{

    public float healInterval = 60f;
    public float healPercent = 0.006f;
    public Effect healEffect = Fx.regenParticle;
    public final int timerHeal = timers++;

    public RegenWall(String name){
        super(name);
        update = true;
    }

    @Override
    public void setStats(){
        super.setStats();
        float maxHp = health;
        float intervalSec = healInterval / 60f;
        String rate = Mathf.equal(intervalSec, 1f) ? "每秒" : "每 " + Strings.autoFixed(intervalSec, 1) + " 秒";
        String text = healPercent > 0f
            ? "[stat]是[]，" + rate + "回复最大生命的 [stat]" + Strings.autoFixed(healPercent * 100f, 2) + "%[]（[lightgray]" + Strings.autoFixed(maxHp * healPercent, 0) + " 生命/次[]）"
            : "[negstat]否[]";
        stats.add(Stat.regenerationRate, text);
    }

    public class RegenWallBuild extends WallBuild{

        @Override
        public void updateTile(){
            if(!timer(timerHeal, healInterval) || health >= maxHealth) return;
            heal(maxHealth * healPercent);
            healEffect.at(x, y, 0f, team.color);
        }
    }
}
