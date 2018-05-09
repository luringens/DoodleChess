#version 120
#pragma optionNV (unroll all)

// Texture coordinates
varying vec2 v_texCoords;

// Noise texture
uniform sampler2D u_texture;

// Burns
uniform vec4 burn;
uniform vec4 burn2;

// Splash balls
uniform vec4 u_safePos[32];
uniform int u_safePosCount;

// Gradient colors
const vec4 red = vec4(0.83f,0,0,1);
const vec4 orange = vec4(0.96f, 0.7f, 0.1f, 1.0f);
const vec4 darkRed = vec4(0.2f, 0.1f, 0.1f, 1.f);

// Gradient percentages
const float change = 0.6f;
const float change2 = 0.8f;

float getV(vec4 ball, vec2 uv) {
    vec2 ballPos = ball.xy;
    vec2 bs = ball.zw;
    vec2 rel = ballPos - uv;
    return 1.0f/(rel.x * rel.x / (bs.x * bs.x) + rel.y * rel.y / (bs.y * bs.y));
}

void main()
{
    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = v_texCoords;
    
    // Get noise from texture
    float noiseX = texture2D(u_texture, uv).r;
    // Normalize noise from [0-1]
    noiseX = noiseX * 2.0f - 1.0f;
    
    // Calculate metaball strength (Best word i could find >.>)
    float v = 0.0f;
    for(int i = 0; i < u_safePosCount; ++i) {
		vec4 ball = u_safePos[i];
        v += getV(ball, uv);
    }

    // Calculate burn metaball strength
    float v2 = getV(burn, uv);
    v2 += getV(burn2, uv);

    // Make them interact
    //v2 -= v;

    // Normalize gradient
    if(v > 0.8f) {
        v += 0.2f;
        float c = v2;
        if (c >= 1.4f) c = 1.4f;
        v2 = 1.0f / v * c;
    }
    
    // Apply noise
    v2 += noiseX / 8.0f;
    
    // Apply gradient to subsection of metaball membrane
    if(v2 >= 0.8f) {
    	
        const vec4 red = vec4(1,0,0,1);
        const vec4 green = vec4(0,1,0,1);
        float t = (v2 - 0.8f) / 0.4f;
        
        vec4 col = mix(red, orange, (t - change2) / (1.0f - change2) );
        if(t < change2)
            col = mix(darkRed, red, (t - change) / (change2 - change));
        if(t < change)
            col = mix(gl_FragColor, darkRed, t / change);
        gl_FragColor = col;
    
    }
    

    // Fill rest with black
    if(v2 >= 1.20f) gl_FragColor = vec4(0, 0, 0, 1);

    if(v2 < 0.8f) {
        if(v - noiseX / 1.5f > 1.0f) gl_FragColor += vec4(0.2f, 0.2f, 0.2f, 0.4f);
    }

}