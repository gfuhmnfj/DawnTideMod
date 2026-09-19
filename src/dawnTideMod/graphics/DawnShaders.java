package dawnTideMod.graphics;

import arc.graphics.gl.Shader;

/**
 * 模组自带的着色器集合。
 * GLSL 内嵌为字符串，避免依赖外部资源文件的加载路径。
 */
public class DawnShaders{
    /** 单位隐身时的低透明度重绘着色器；加载失败时保持 null。 */
    public static StealthShader stealthAlpha;

    /** 惰性创建 GL 资源；只应在渲染线程调用。 */
    public static void load(){
        if(stealthAlpha != null) return;
        try{
            stealthAlpha = new StealthShader();
        }catch(Throwable ignored){
            stealthAlpha = null;
        }
    }

    /** 在原精灵着色器基础上，把最终 alpha 乘以 {@link #opacity}。 */
    public static class StealthShader extends Shader{
        public float opacity = 1f;

        public StealthShader(){
            super(
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
                "}\n",

                "varying lowp vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "uniform sampler2D u_texture;\n" +
                "uniform float u_opacity;\n" +
                "\n" +
                "void main(){\n" +
                "    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" +
                "    gl_FragColor.a *= u_opacity;\n" +
                "}\n"
            );
        }

        @Override
        public void apply(){
            setUniformf("u_opacity", opacity);
        }
    }
}
