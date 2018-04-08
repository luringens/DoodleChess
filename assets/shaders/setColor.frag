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
    
    // Average color of tint
    float avg = (u_tint.r + u_tint.g + u_tint.b) / 3.0f;
    //float avg = max(u_tint.r, max(u_tint.g, u_tint.b));

    // Get color of image
    vec4 col = texture2D(u_texture, uv);
	
    // Do not change alpha, it should stay the same
    gl_FragColor.a = col.a;
    
    // If the average color of the tint is less than 0.5f, do an additive blend with the inverted image
    if(avg < 0.25f) 
    {
        gl_FragColor.rgb = vec3(1,1,1) - col.rgb + u_tint.rgb;
        return;
    }
    
    // If the average color of the tint is more then 0.5f, do a multiplicative blend with the real image
    gl_FragColor.rgb = col.rgb * u_tint.rgb;
}