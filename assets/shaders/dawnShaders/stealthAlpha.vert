uniform mat4 u_projTrans;

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;
attribute vec4 a_mix_color;

varying lowp vec4 v_color;
varying lowp vec4 v_mix_color;
varying highp vec2 v_texCoords;

void main(){
    gl_Position = u_projTrans * a_position;
    v_texCoords = a_texCoord0;
    v_color = a_color;
    v_color.a *= 255.0 / 254.0;
    v_mix_color = a_mix_color;
    v_mix_color.a *= 255.0 / 254.0;
}
