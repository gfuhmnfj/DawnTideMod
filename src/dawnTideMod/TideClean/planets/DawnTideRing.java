package dawnTideMod.TideClean.ui.planets;

import arc.graphics.Color;
import mindustry.graphics.g3d.GenericMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.type.Planet;

public class DawnTideRing{

    public static GenericMesh innerRing;

    public static GenericMesh outerRing;

    public static void attach(Planet planet, java.util.function.Supplier<GenericMesh> cloudMaker){
        planet.cloudMeshLoader = () -> {

            if(innerRing == null){
                innerRing = createRing(planet,
                    192,
                    1.42f,
                    1.86f,
                    -14f,
                    18f,
                    -0.34f,
                    DawnTidePalette.RING_INNER,
                    DawnTidePalette.RING_OUTER,
                    DawnTidePalette.RING_GLOW,
                    DawnTidePalette.SEED + 11);
            }

            if(outerRing == null){
                outerRing = createRing(planet,
                    160,
                    2.05f,
                    2.72f,
                    -14f,
                    9f,
                    -0.30f,
                    DawnTidePalette.RING_OUTER,
                    DawnTidePalette.RING_INNER,
                    DawnTidePalette.RING_GLOW_DIM,
                    DawnTidePalette.SEED + 27);
            }

            GenericMesh clouds = cloudMaker == null ? null : cloudMaker.get();
            return clouds == null
                ? new MultiMesh(innerRing, outerRing)
                : new MultiMesh(clouds, innerRing, outerRing);
        };
    }

    private static RingMesh createRing(Planet planet, int divisions, float innerRadius, float outerRadius,
                                       float tilt, float bandScale, float gapThreshold,
                                       Color innerColor, Color outerColor, Color glowColor, int seed){
        RingMesh ring = new RingMesh(planet, divisions, innerRadius, outerRadius, tilt,
            innerColor, outerColor, glowColor);

        ring.bandScale = bandScale;
        ring.gapThreshold = gapThreshold;
        ring.seed = seed;
        ring.rotateSpeed = DawnTidePalette.RING_ROTATE_SPEED;
        return ring;
    }

    public static void dispose(){
        if(innerRing instanceof RingMesh rm) rm.dispose();
        if(outerRing instanceof RingMesh rm) rm.dispose();
        innerRing = outerRing = null;
    }
}
