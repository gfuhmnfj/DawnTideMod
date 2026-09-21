package dawnTideMod.content;

import arc.graphics.Color;
import dawnTideMod.content.entities.abilities.StealthStatusEffect;
import mindustry.content.Fx;
import mindustry.type.StatusEffect;

public class DawnTideStatuses{

    public static StealthStatusEffect stealth;
    public static StatusEffect magneticDisorder;

    public static void load(){

        stealth = new StealthStatusEffect("Stealth");
        stealth.speedMultiplier = 2f;
        stealth.color = Color.valueOf("F4F4F4FF");
        stealth.install();

        magneticDisorder = new StatusEffect("MagneticDisorder");
        magneticDisorder.color = Color.valueOf("F4F4F4FF");
        magneticDisorder.speedMultiplier = 0.5f;
        magneticDisorder.healthMultiplier = 0.5f;
        magneticDisorder.reloadMultiplier = 0.5f;
        magneticDisorder.effect = Fx.wet;
    }
}
