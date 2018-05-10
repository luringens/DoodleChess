package com.syntax_highlighters.chess.gui.particles;


import com.badlogic.gdx.graphics.Color;

public class Ember extends Particle {
    boolean direction;
    float turnSpeed;
    float z = 1.0f;
    float maxLife = 3.0f;
    public Ember() {
        width = 1.5f;
        height = 1.5f;
        direction = Math.random() > 0.5f;
        turnSpeed = (float)Math.random();
        maxLife = 1.0f + 1.0f * (float)Math.random();
        z = .5f + (float)Math.random();
    }

    @Override
    public String getTexture() {
        return "ember.png";
    }

    @Override
    public void act(float delta) {
        if(lifeTime > maxLife)
            isAlive = false;
        float t = lifeTime / maxLife;
        float uRand = (float)( Math.random() * Math.random() * Math.random());
        x += Math.cos(angle) * speed * delta;
        y += Math.sin(angle) * speed * delta;
        angle += (Math.random() * 30.f - 15.f + (direction ? Math.PI/6.f * turnSpeed : -Math.PI/6.f * turnSpeed)) * delta;
        speed += ((1500.f * Math.pow(1.0 - t, 8) + 100.f * uRand + speed / 4.f) * (Math.random() - .2f) - speed * Math.random()) * delta;

        z += (Math.random() * 0.8f - 0.15f) * delta;
        width = 1.5f * z;
        height = 1.5f * z;
    }

    @Override
    public Color getColor() {
        return new Color(0.96f, 0.7f, 0.1f, 1.0f - lifeTime / maxLife).lerp(0.83f,0,0, 1.0f - lifeTime / maxLife, lifeTime / maxLife);
    }
}
