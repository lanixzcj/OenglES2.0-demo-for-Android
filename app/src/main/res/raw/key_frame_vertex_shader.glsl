uniform mat4 u_Matrix;

attribute vec3 a_Position0;
attribute vec3 a_Position1;
attribute vec3 a_Position2;
attribute vec3 a_Position3;
attribute vec3 a_Position4;
attribute vec3 a_Position5;
attribute vec3 a_Position6;
uniform float u_factor;
attribute vec2 a_TextureCoordinates;
varying vec2 v_TextureCoord;

void main()
{
    vec3 position;

    if (u_factor < 1.0) {
        position = mix(a_Position0, a_Position1, u_factor);
    } else if (u_factor < 2.0) {
        position = mix(a_Position1, a_Position2, u_factor - 1.0);
    } else if (u_factor < 3.0) {
        position = mix(a_Position2, a_Position3, u_factor - 2.0);
    } else if (u_factor < 4.0) {
        position = mix(a_Position3, a_Position4, u_factor - 3.0);
    } else if (u_factor < 5.0) {
        position = mix(a_Position4, a_Position5, u_factor - 4.0);
    } else {
        position = mix(a_Position5, a_Position6, u_factor - 5.0);
    }
    v_TextureCoord = a_TextureCoordinates;
    gl_Position = u_Matrix * vec4(position,1);
}       
