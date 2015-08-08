uniform mat4 u_Matrix;

attribute vec3 a_Position;
attribute vec2 a_TextureCoordinates;
varying vec2 v_TextureCoord;

void main()
{                           
    v_TextureCoord = a_TextureCoordinates;
    gl_Position = u_Matrix * vec4(a_Position,1);
}       
