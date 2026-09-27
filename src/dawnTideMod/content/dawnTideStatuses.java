package dawnTideMod.content;

import arc.graphics.Color;
import dawnTideMod.content.entities.abilities.StealthStatusEffect;
import mindustry.content.Fx;
import mindustry.type.StatusEffect;

public class dawnTideStatuses {

    public static StealthStatusEffect stealth;
    public static StatusEffect magneticDisorder;

    public static void load(){

        stealth = new StealthStatusEffect("Stealth"){{
            localizedName = "隐身";
            speedMultiplier = 2f;
            color = Color.valueOf("6E7080FF");
            install();
        }};

        magneticDisorder = new StatusEffect("MagneticDisorder"){{
            localizedName = "磁紊";
            color = Color.valueOf("84F491FF");
            speedMultiplier = 0.5f;
            healthMultiplier = 0.5f;
            reloadMultiplier = 0.5f;
            effect = Fx.wet;
        }};
    }
}
