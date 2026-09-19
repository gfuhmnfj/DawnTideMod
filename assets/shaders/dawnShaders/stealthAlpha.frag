varying lowp vec4 v_color;
varying lowp vec4 v_mix_color;
varying highp vec2 v_texCoords;

uniform sampler2D u_texture;
uniform lowp float u_alpha;

void main(){
    lowp vec4 color = texture2D(u_texture, v_texCoords);
    gl_FragColor = v_color * mix(color, vec4(v_mix_color.rgb, color.a), v_mix_color.a)
        * vec4(1.0, 1.0, 1.0, u_alpha);
}
