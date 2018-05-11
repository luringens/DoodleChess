package com.syntax_highlighters.chess.gui.particles;

import com.badlogic.gdx.graphics.Color;

public abstract class Particle {
    float lifeTime = 0;
    boolean isAlive = false;
    float x, y;
    float angle, speed;
    float width, height;

    public abstract String getTexture();
    public abstract void act(float delta);
    public abstract Color getColor();

    public static <T extends Particle> T[] generate(int maxParticles, Class<T> particleType) {
        if(particleType == Ember.class) {
            Ember[] em = new Ember[maxParticles];
            for(int i = 0; i < maxParticles; ++i) em[i] = new Ember();
            return (T[]) em;
        }
        if(particleType == Glow.class) {
            Glow[] em = new Glow[maxParticles];
            for(int i = 0; i < maxParticles; ++i) em[i] = new Glow();
            return (T[]) em;
        }
        return null;
    }

}
