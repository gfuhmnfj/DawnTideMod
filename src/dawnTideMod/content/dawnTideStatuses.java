package dawnTideMod.content;

import arc.graphics.Color;
import dawnTideMod.content.entities.abilities.StealthStatusEffect;
import mindustry.content.Fx;
import mindustry.type.StatusEffect;

public class dawnTideStatuses{
    public static StatusEffect Stealth,MagneticDisorder;

    public static void load(){
        Stealth = new StealthStatusEffect("Stealth");//隐身
        ((StealthStatusEffect)Stealth).install();

        MagneticDisorder = new StatusEffect("MagneticDisorder"){{//磁紊
            color = Color.valueOf("FF0000FF");
            speedMultiplier = 0.5f;
            healthMultiplier = 0.5f;
            reloadMultiplier = 0.5f;
            effect = Fx.wet;
            }};
    }
}