#version 120
#ifdef GL_ES
precision mediump float;
#endif

#extension GL_EXT_gpu_shader4 : enable

uniform sampler2D u_texture;
uniform vec2 u_offset;

varying vec2 v_texCoords;

// Simplex 2D noise
//
vec3 permute(vec3 x) { return mod(((x*34.0)+1.0)*x, 289.0); }

float snoise(vec2 v){
  const vec4 C = vec4(0.211324865405187, 0.366025403784439,
           -0.577350269189626, 0.024390243902439);
  vec2 i  = floor(v + dot(v, C.yy) );
  vec2 x0 = v -   i + dot(i, C.xx);
  vec2 i1;
  i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);
  vec4 x12 = x0.xyxy + C.xxzz;
  x12.xy -= i1;
  i = mod(i, 289.0);
  vec3 p = permute( permute( i.y + vec3(0.0, i1.y, 1.0 ))
  + i.x + vec3(0.0, i1.x, 1.0 ));
  vec3 m = max(0.5 - vec3(dot(x0,x0), dot(x12.xy,x12.xy),
    dot(x12.zw,x12.zw)), 0.0);
  m = m*m ;
  m = m*m ;
  vec3 x = 2.0 * fract(p * C.www) - 1.0;
  vec3 h = abs(x) - 0.5;
  vec3 ox = floor(x + 0.5);
  vec3 a0 = x - ox;
  m *= 1.79284291400159 - 0.85373472095314 * ( a0*a0 + h*h );
  vec3 g;
  g.x  = a0.x  * x0.x  + h.x  * x0.y;
  g.yz = a0.yz * x12.xz + h.yz * x12.yw;
  return 130.0 * dot(m, g);
}

float ridgenoise(vec2 v) {
  return 2.0 * (0.5 - abs(snoise(v)));
}

void main()
{
    // Normalized pixel coordinates (from 0 to 1)
	
    float endNoise = 1.0f;
    float noiseOffset = 1.0f;
    
    float offset = (snoise(v_texCoords/10.0)+1.0f)/2.0f;
    
    float e = 1.0f;
    float e2 = 1.0f;
    
    float thing2 = snoise(vec2(v_texCoords.y, v_texCoords.x) / 1e2f);
    
    for(float i = 1.0f; i < 6.0f; ++i)
    {
        float thing = float(1 << int(i));
        e = 1.0f/i * ridgenoise((vec2(v_texCoords.x + 100.0-v_texCoords.y * 2.0f, v_texCoords.y+v_texCoords.x * 2.0f) / 1.5f * (5.0f-i) + u_offset) / i * thing) * endNoise;
        e = -e;
        endNoise += e;
        
        e2 = 1.0f/i * ridgenoise((vec2(v_texCoords.x + 100.0-v_texCoords.y * 2.0f, v_texCoords.y+v_texCoords.x * 2.0f) / 1.5f * (5.0f-i) + u_offset) / i* thing - vec2(0.01f, 0.01f)) 
            * noiseOffset;
        e2 = -e2;
        noiseOffset += e2;
	}
    endNoise -= 1.0f;
    noiseOffset -= 1.0f;
    
    endNoise = pow(endNoise, 1.0f);
    noiseOffset = pow(noiseOffset, 1.0f);
    endNoise = ((noiseOffset-endNoise));

    // Output to screen
    gl_FragColor = vec4(endNoise, endNoise, endNoise,1.0f);
}