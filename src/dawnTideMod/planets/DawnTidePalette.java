package dawnTideMod.planets;

import arc.graphics.Color;

public class DawnTidePalette{

    public static final Color DEPTH   = Color.valueOf("0b1f4d").a(0.05f);
    public static final Color SHALLOW = Color.valueOf("1560a8").a(0.06f);
    public static final Color SHORE   = Color.valueOf("c9b077").a(0.08f);
    public static final Color PLAIN   = Color.valueOf("4e8c3a").a(0.09f);
    public static final Color HIGHLAND= Color.valueOf("7a6a55").a(0.10f);
    public static final Color PEAK    = Color.valueOf("e8eef5").a(0.12f);
    public static final Color POLAR   = Color.valueOf("dff0ff").a(0.10f);
    public static final float POLAR_LAT = 0.62f;
    public static final Color TINT = Color.white.cpy();
    public static final Color CLOUD_1 = Color.valueOf("d6fff2");
    public static final Color CLOUD_2 = Color.valueOf("8fe8d4");
    public static final Color ATMOSPHERE = Color.valueOf("5fe0c0");
    public static final Color ICON = Color.valueOf("4fe0c0");
    public static final Color LAND_CLOUD = Color.valueOf("7fe8d0").a(0.5f);
    public static final Color MOON_BASE = Color.valueOf("8d8d96");
    public static final Color MOON_TINT = Color.valueOf("4a4a52");
    public static final Color RING_INNER = Color.valueOf("8ffff0");
    public static final Color RING_OUTER = Color.valueOf("1b9c86");
    public static final Color RING_GLOW = Color.valueOf("d8fff6");
    public static final Color RING_GLOW_DIM = Color.valueOf("9ce8da");
    public static final float RING_SHADOW = 0.35f;
    public static final float RING_ROTATE_SPEED = 1.6f;
    public static final int SEED = 20260919;
    public static final float NOISE_SCALE = 4.2f;
    public static final float SEA_LEVEL = 0.42f;
    public static final float HEIGHT_MULT = 0.16f;
}
