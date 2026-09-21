package dawnTideMod.content;

import arc.graphics.Color;
import mindustry.type.Liquid;

/** 「曙光潮涌」液体注册类。 */
public class DawnTideLiquids{

    public static Liquid vulcanizing, microscaleFluid;

    public static void load(){
        vulcanizing = new Liquid("vulcanizing", Color.valueOf("FFAA5FFF")){{ // 硫化液
            gas = false;
            boilPoint = 2;
            flammability = 1f;
            temperature = 1.5f;
            coolant = false;
            particleSpacing = 60;
            capPuddles = true;
        }};

        microscaleFluid = new Liquid("MicroscaleFluid", Color.valueOf("FFAA5FFF")){{ // 微量流体
            gas = false;
            boilPoint = 0;
            temperature = -3f;
            coolant = false;
            particleSpacing = 60;
            capPuddles = true;
        }};
    }
}
