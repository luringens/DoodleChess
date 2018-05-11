package com.syntax_highlighters.chess.gui.particles;


import com.badlogic.gdx.graphics.Color;

public class Glow extends Particle {
    final float maxLife;
    public Glow() {
        float z = (float) Math.random() * 4.f;
        width = 2.0f + z;
        height = 2.0f + z;
        maxLife = 1.0f;
    }

    @Override
    public String getTexture() {
        return "ember.png";
    }

    @Override
    public void act(float delta) {
        if(lifeTime > maxLife)
            isAlive = false;
        x += Math.cos(angle) * speed * delta;
        y += Math.sin(angle) * speed * delta;
    }

    @Override
    public Color getColor() {
        return new Color(1,0.84f,0, 1.0f - lifeTime / maxLife).lerp(0.8f,0.64f,0, 1.0f - lifeTime / maxLife, lifeTime / maxLife);
    }
}
