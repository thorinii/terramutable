attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_worldView;
uniform mat4 u_model;

varying vec4 v_color;

void main() {
  v_color = a_color;
  gl_Position = u_worldView * u_model * a_position;
}
