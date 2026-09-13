package dawnTideMod.graphics;

import arc.graphics.gl.Shader;

/**
 * 「曙光潮涌」自定义着色器集合。
 *
 * 注意：Shader 构造会立即编译 GL 程序，必须等 GL 上下文就绪后才能调用 load()
 * （本类在 Trigger.draw 阶段惰性加载，绝不能在 loadContent 阶段构造）。
 */
public class AnnihilationShaders{
    /** 隐身半透明着色器：把单位整体 alpha 乘以 opacity */
    public static StealthShader stealthAlpha;

    /** 惰性加载（幂等），仅在客户端渲染阶段调用 */
    public static void load(){
        if(stealthAlpha != null) return;
        stealthAlpha = new StealthShader();
    }

    /** 隐身着色器：正常采样贴图，输出 alpha 乘以 opacity */
    public static class StealthShader extends Shader{
        /** 隐身透明度（0~1，越小越透明） */
        public float opacity = 0.35f;

        public StealthShader(){
            super(
                //顶点着色器：与 arc SpriteBatch 默认格式一致
                "attribute vec4 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "uniform mat4 u_projModelView;\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "void main(){\n" +
                "    v_color = a_color;\n" +
                "    v_texCoords = a_texCoord0;\n" +
                "    gl_Position = u_projModelView * a_position;\n" +
                "}",
                //片元着色器：alpha 乘以透明度
                "#define HIGHP\n" +
                "uniform sampler2D u_texture;\n" +
                "uniform float u_opacity;\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "void main(){\n" +
                "    vec4 c = texture2D(u_texture, v_texCoords) * v_color;\n" +
                "    gl_FragColor = vec4(c.rgb, c.a * u_opacity);\n" +
                "}"
            );
        }

        @Override
        public void apply(){
            super.apply();
            setUniformf("u_opacity", opacity);
        }
    }
}
