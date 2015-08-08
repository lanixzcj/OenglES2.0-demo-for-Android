precision mediump float;
uniform sampler2D u_TextureUnit;//纹理内容数据
varying vec2 v_TextureCoord;

void main() {
    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoord);
}