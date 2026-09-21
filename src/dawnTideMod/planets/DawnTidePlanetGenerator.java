package dawnTideMod.planets;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.util.noise.Simplex;
import mindustry.content.Blocks;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.world.Block;

public class DawnTidePlanetGenerator extends PlanetGenerator{

    private static final Color tmp = new Color();

    public float rawHeight(Vec3 position){
        float n = Simplex.noise3d(
            DawnTidePalette.SEED,
            6, 0.5f, 1f / DawnTidePalette.NOISE_SCALE,
            position.x, position.y, position.z
        );

        return Mathf.clamp((n + 1f) * 0.5f);
    }

    @Override
    public float getHeight(Vec3 position){
        float h = rawHeight(position);
        float sea = DawnTidePalette.SEA_LEVEL;
        float shaped = h < sea ? sea : sea + (h - sea) * 1.6f;
        return shaped * DawnTidePalette.HEIGHT_MULT;
    }

    @Override
    public void getColor(Vec3 position, Color out){
        float h = rawHeight(position);

        Color c;
        if(h < DawnTidePalette.SEA_LEVEL){

            float t = Mathf.clamp(h / DawnTidePalette.SEA_LEVEL);
            c = tmp.set(DawnTidePalette.DEPTH).lerp(DawnTidePalette.SHALLOW, t);
        }else{

            float t = Mathf.clamp((h - DawnTidePalette.SEA_LEVEL) / (1f - DawnTidePalette.SEA_LEVEL));
            if(t < 0.18f){
                c = tmp.set(DawnTidePalette.SHORE).lerp(DawnTidePalette.PLAIN, t / 0.18f);
            }else if(t < 0.55f){
                c = tmp.set(DawnTidePalette.PLAIN).lerp(DawnTidePalette.HIGHLAND, (t - 0.18f) / 0.37f);
            }else{
                c = tmp.set(DawnTidePalette.HIGHLAND).lerp(DawnTidePalette.PEAK, (t - 0.55f) / 0.45f);
            }
        }

        float lat = Math.abs(position.y);
        if(lat > DawnTidePalette.POLAR_LAT){
            float t = Mathf.clamp((lat - DawnTidePalette.POLAR_LAT) / (1f - DawnTidePalette.POLAR_LAT));
            c.lerp(DawnTidePalette.POLAR, t * 0.85f);
        }

        out.set(c.r * DawnTidePalette.TINT.r,
                c.g * DawnTidePalette.TINT.g,
                c.b * DawnTidePalette.TINT.b,
                c.a * DawnTidePalette.TINT.a);
    }

    @Override
    public void genTile(Vec3 position, mindustry.world.TileGen tile){
        Block floor = getBlock(position);
        tile.floor = floor;
        tile.block = floor.asFloor().wall;

        if(Simplex.noise3d(DawnTidePalette.SEED + 7, 2, 0.5f, 1f / 18f,
            position.x, position.y, position.z) > 0.42f){
            tile.block = Blocks.air;
        }
    }

    public Block getBlock(Vec3 position){
        float h = rawHeight(position);
        float lat = Math.abs(position.y);

        if(h < DawnTidePalette.SEA_LEVEL * 0.6f) return Blocks.deepwater;
        if(h < DawnTidePalette.SEA_LEVEL)        return Blocks.water;

        float t = Mathf.clamp((h - DawnTidePalette.SEA_LEVEL) / (1f - DawnTidePalette.SEA_LEVEL));

        if(lat > DawnTidePalette.POLAR_LAT + 0.2f) return Blocks.iceSnow;
        if(t < 0.18f) return Blocks.sand;
        if(t < 0.55f) return Blocks.moss;
        if(t < 0.80f) return Blocks.stone;
        return Blocks.snow;
    }

    @Override
    public boolean isEmissive(){
        return false;
    }

    @Override
    public float getSizeScl(){
        return 2000 * 1.07f * 6f / 5f;
    }
}
