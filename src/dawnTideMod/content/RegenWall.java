package dawnTideMod.content;

import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.world.blocks.defense.Wall;

public class RegenWall extends Wall{

    public float healInterval = 60f;

    public float healPercent = 0.006f;
    public Effect healEffect = Fx.regenParticle;

    public final int timerHeal = timers++;

    public RegenWall(String name){
        super(name);
        update = true;
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
