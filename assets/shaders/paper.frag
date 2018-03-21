#version 120
#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform vec2 u_offset;

varying vec2 v_texCoords;


//
// Simplex 2D noise
//
vec3 hash3( vec2 p ){
    vec3 q = vec3( dot(p,vec2(127.1,311.7)), 
                   dot(p,vec2(269.5,183.3)), 
                   dot(p,vec2(419.2,371.9)) );
    return fract(sin(q)*43758.5453);
}

float iqnoise( in vec2 x, float u, float v ){
    vec2 p = floor(x);
    vec2 f = fract(x);
        
    float k = 1.0+63.0*pow(1.0-v,4.0);
    
    float va = 0.0;
    float wt = 0.0;
    for( int j=-2; j<=2; j++ )
    for( int i=-2; i<=2; i++ )
    {
        vec2 g = vec2( float(i),float(j) );
        vec3 o = hash3( p + g )*vec3(u,u,1.0);
        vec2 r = g - f + o.xy;
        float d = dot(r,r);
        float ww = pow( 1.0-smoothstep(0.0,1.414,sqrt(d)), k );
        va += o.z*ww;
        wt += ww;
    }
    
    return va/wt;
}

void main()
{
    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = v_texCoords;
    
    vec2 uvc = uv - 0.5f;
    
    
    vec2 pos = vec2(uv.x / 0.5f + u_offset.x, uv.y / 0.5f + u_offset.y);
   
    
    float f = 1.f;
    for(float i = 1.f; i < 5.f; ++i)
    {
        f = f * iqnoise( pos * (150.f / i), 1.4f,1.5f );
    }
    
    f = f * 0.25f;
    f = 1.f-f;
    
    
    // Output to screen
    gl_FragColor = f * texture2D(u_texture, uv * f);

    //gl_FragColor = vec4(noise, noise, noise, 1.0f);
}
