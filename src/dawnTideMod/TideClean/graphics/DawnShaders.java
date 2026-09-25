package dawnTideMod.TideClean.graphics;

import arc.graphics.gl.Shader;
public class DawnShaders{
    public static StealthShader stealthAlpha;
    private static boolean attempted;
    public static void init(){
        if(attempted) return;
        attempted = true;
        try{
            stealthAlpha = new StealthShader();
        }catch(Throwable t){
            stealthAlpha = null;
            t.printStackTrace();
        }
    }
    public static class StealthShader extends Shader{
        public float opacity = 0.35f;
        public StealthShader(){
            super(VERTEX, FRAGMENT);
        }

        @Override
        public void apply(){
            setUniformf("u_opacity", opacity);
        }
    }

    private static final String VERTEX =
        "attribute vec4 a_position;\n" +
        "attribute vec4 a_color;\n" +
        "attribute vec2 a_texCoord0;\n" +
        "uniform mat4 u_projTrans;\n" +
        "varying vec4 v_color;\n" +
        "varying vec2 v_texCoords;\n" +
        "void main(){\n" +
        "    v_color = a_color;\n" +
        "    v_texCoords = a_texCoord0;\n" +
        "    gl_Position = u_projTrans * a_position;\n" +
        "}\n";

    private static final String FRAGMENT =
        "varying lowp vec4 v_color;\n" +
        "varying vec2 v_texCoords;\n" +
        "uniform sampler2D u_texture;\n" +
        "uniform float u_opacity;\n" +
        "void main(){\n" +
        "    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" +
        "    gl_FragColor.a *= u_opacity;\n" +
        "}\n";
}
