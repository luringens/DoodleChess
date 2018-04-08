#version 120
#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform vec4 u_tint;

varying vec2 v_texCoords;

void main()
{
    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = v_texCoords;

    // Get color of image
    vec4 col = texture2D(u_texture, uv);
	
    // Do not change alpha, it should stay the same
    gl_FragColor.a = col.a;
    
    
    // If the average color of the tint is more then 0.5f, do a multiplicative blend with the real image
    gl_FragColor.rgb = col.rgb * u_tint.rgb;
}