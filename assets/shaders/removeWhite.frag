

// Main Shader
#version 120
#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec2 v_texCoords;

void main() {
    vec4 myCol = texture2D(u_texture, v_texCoords);
    if(myCol.r == 1.0f && myCol.g == 1.0f && myCol.b == 1.0f)
    {
        gl_FragColor = vec4(0,0,0,0);
        return;
    }
    gl_FragColor = myCol;
}