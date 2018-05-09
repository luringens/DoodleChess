#version 120
// Texture coordinates
varying vec2 v_texCoords;

// Splash balls
uniform vec4 u_safePos[32];
uniform int u_safePosCount;

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
    
    // Calculate metaball strength (Best word i could find >.>)
    float v = 0.0f;
    for(int i = 0; i < u_safePosCount; ++i) {
		vec4 ball = u_safePos[i];
        v += getV(ball, uv);
    }

    gl_FragColor = vec4(0);
    if(v > 1.2f)
        gl_FragColor = vec4(0.0f, 0.0f, 1.0f, 0.5f);
}