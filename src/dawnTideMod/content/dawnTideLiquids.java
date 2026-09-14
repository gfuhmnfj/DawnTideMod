package dawnTideMod.content;

import arc.graphics.Color;
import mindustry.type.Liquid;

public class dawnTideLiquids {
    public static Liquid vulcanizing,MicroscaleFluid;

    public static void load(){
        vulcanizing = new Liquid("vulcanizing", Color.valueOf("FFAA5FFF")){{
            gas = false;
            boilPoint = 2;
            flammability = 1f;//可燃
            temperature = 1.5f;
            coolant = false;
            particleSpacing = 60;
            capPuddles = true;
        }};

        MicroscaleFluid = new Liquid("MicroscaleFluid", Color.valueOf("FFAA5FFF")){{
            gas = false;
            boilPoint = 0;
            temperature = -3f;
            coolant = false;
            particleSpacing = 60;
            capPuddles = true;
        }};
    }
}
