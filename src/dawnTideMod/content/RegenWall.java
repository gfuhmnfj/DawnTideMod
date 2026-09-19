package dawnTideMod.content;

import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.world.blocks.defense.Wall;

/**
 * 「曙光潮涌」再生墙：每隔一段时间回复一定比例的血量。
 *
 * 原理（对照源码）：
 * 1. 继承 Wall，内部类 RegenWallBuild extends WallBuild（Mindustry 自动绑定 build 类）；
 * 2. Block.update 必须设 true，否则 updateTile() 每帧不会被调用；
 * 3. timer(timerHeal, healInterval) 来自 TimerComp 组件：到期返回 true 并自动重置计时；
 * 4. 结算：heal(maxHealth * healPercent)。
 */
public class RegenWall extends Wall{

    /** 回血结算间隔（帧，60 = 1 秒）。 */
    public float healInterval = 60f;
    /** 每次结算回复的最大血量百分比（0.006 = 0.6%）。 */
    public float healPercent = 0.006f;
    public Effect healEffect = Fx.regenParticle;

    public RegenWall(String name){
        super(name);
        update = true;
    }

    public final int timerHeal = timers++;

    public class RegenWallBuild extends WallBuild{

        @Override
        public void updateTile(){
            if(!timer(timerHeal, healInterval) || health >= maxHealth) return;
            heal(maxHealth * healPercent);
            healEffect.at(x, y, 0f, team.color);
        }
    }
}
