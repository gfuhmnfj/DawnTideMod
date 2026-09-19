package dawnTideMod.content;

import arc.graphics.Color;
import dawnTideMod.content.entities.abilities.StealthStatusEffect;
import mindustry.content.Fx;
import mindustry.type.StatusEffect;

public class dawnTideStatuses{
    public static StatusEffect Stealth,MagneticDisorder;

    public static void load(){

        Stealth = new StealthStatusEffect("Stealth"){{
            speedMultiplier = 2f;
            color = Color.valueOf("F4F4F4FF");
        }};//隐身
        ((StealthStatusEffect)Stealth).install(); // 必须在赋值完成后调用，不能放进上面的 {{ }}（构造期间字段还是 null）

        MagneticDisorder = new StatusEffect("MagneticDisorder"){{//磁紊
            color = Color.valueOf("F4F4F4FF");
            speedMultiplier = 0.5f;
            healthMultiplier = 0.5f;
            reloadMultiplier = 0.5f;
            effect = Fx.wet;
            }};
    }
}