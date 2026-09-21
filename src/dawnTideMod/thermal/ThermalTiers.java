package dawnTideMod.thermal;

import arc.graphics.Color;
import mindustry.graphics.Pal;

public class ThermalTiers{

    public static final float COMFORT_CENTER = 0.5f;

    public static final float COMFORT_HALF_WIDTH = 0.12f;

    public static final float MAX_HEALTH_PENALTY = 0.45f;

    public static final float MAX_RESIST_BONUS = 0.8f;

    public enum Tier{

        cold,

        comfort,

        hot
    }

    public static Tier tier(float temp){
        if(temp < COMFORT_CENTER - COMFORT_HALF_WIDTH) return Tier.cold;
        if(temp > COMFORT_CENTER + COMFORT_HALF_WIDTH) return Tier.hot;
        return Tier.comfort;
    }

    public static float deviation(float temp){
        float t = clamp01(temp);
        if(t < COMFORT_CENTER - COMFORT_HALF_WIDTH){
            float span = COMFORT_CENTER - COMFORT_HALF_WIDTH;
            return span <= 0f ? 0f : (span - t) / span;
        }
        if(t > COMFORT_CENTER + COMFORT_HALF_WIDTH){
            float span = 1f - (COMFORT_CENTER + COMFORT_HALF_WIDTH);
            return span <= 0f ? 0f : (t - (COMFORT_CENTER + COMFORT_HALF_WIDTH)) / span;
        }
        return 0f;
    }

    public static float healthMultiplier(float temp){
        return 1f - deviation(temp) * MAX_HEALTH_PENALTY;
    }

    public static float healthMultiplier(float temp, float heatResist, float coldResist){
        float dev = deviation(temp);
        if(dev <= 0f) return 1f;

        Tier t = tier(temp);

        float resist = t == Tier.hot ? clamp01(heatResist) : clamp01(coldResist);

        float penalty = dev * MAX_HEALTH_PENALTY * (1f - resist);

        float bonus = dev * MAX_RESIST_BONUS * resist;

        return 1f + bonus - penalty;
    }

    public static Color hotColor(float temp){
        float d = tier(temp) == Tier.hot ? deviation(temp) : 0f;
        return TEMP.set(Pal.lightOrange).lerp(Color.valueOf("ff3b1a"), d);
    }

    public static Color coldColor(float temp){
        float d = tier(temp) == Tier.cold ? deviation(temp) : 0f;
        return TEMP.set(Color.valueOf("8fc7ff")).lerp(Color.valueOf("3a6bff"), d);
    }

    public static Color statusColor(float temp){
        Tier t = tier(temp);
        if(t == Tier.hot) return hotColor(temp);
        if(t == Tier.cold) return coldColor(temp);
        return Color.white;
    }

    public static float clamp01(float v){
        return v < 0f ? 0f : (v > 1f ? 1f : v);
    }

    private static final Color TEMP = new Color();
}
