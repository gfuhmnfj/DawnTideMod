package dawnTideMod.content;

import arc.graphics.Color;
import mindustry.type.Liquid;
public class DawnTideLiquids{

    public static Liquid vulcanizing, microscaleFluid;

    public static void load(){
        vulcanizing = new Liquid("vulcanizing", Color.valueOf("FFAA5FFF")){{
            localizedName = "硫化液";
            gas = false;
            boilPoint = 2;
            flammability = 1f;
            temperature = 1.5f;
            coolant = false;
            particleSpacing = 60;
            capPuddles = true;
        }};

        microscaleFluid = new Liquid("MicroscaleFluid", Color.valueOf("FFAA5FFF")){{
            localizedName = "微米流体";
            gas = false;
            boilPoint = 0;
            temperature = -3f;
            coolant = false;
            particleSpacing = 60;
            capPuddles = true;
        }};
    }
}
