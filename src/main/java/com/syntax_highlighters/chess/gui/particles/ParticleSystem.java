package com.syntax_highlighters.chess.gui.particles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public class ParticleSystem <T extends Particle> {
    final int maxParticles;
    T[] particles;
    int back = 0;
    int front = 0;
    AssetManager assetManager;

    public ParticleSystem(int maxParticles, Class<T> particleType, AssetManager assetManager) {
        particles = Particle.generate(maxParticles, particleType);
        if(particles == null) throw new RuntimeException("Invalid particle type");
        this.assetManager = assetManager;
        this.maxParticles = maxParticles;
    }

    public void spawnParticle(float x, float y, float angle, float speed) {
        Particle p = particles[front];
        if(p.isAlive || (front + 1) % maxParticles == back) {
            System.out.println("Max size of particle system not big enough!");
            return;
        }
        p.isAlive = true;
        p.x = x;
        p.y = y;
        p.angle = angle;
        p.speed = speed;
        p.lifeTime = 0;
        front = (front + 1) % maxParticles;

    }

    public void act(float delta) {
        while(!particles[back].isAlive && (back + 1) % maxParticles != front) back = (back + 1) % maxParticles;
        if(front < back) {
            for(int i = back; i < maxParticles; ++i)
            {
                particles[i].lifeTime += delta;
                particles[i].act(delta);
            }
            for(int i = 0; i < front; ++i) {
                particles[i].lifeTime += delta;
                particles[i].act(delta);
            }
        } else {

            for(int i = back; i < front; ++i)
            {
                particles[i].lifeTime += delta;
                particles[i].act(delta);
            }
        }

    }

    public void draw(Batch batch) {
        Texture tex = assetManager.get(particles[0].getTexture());

        if(front < back) {
            for(int i = back; i < maxParticles; ++i)
            {
                Particle p = particles[i];
                batch.setColor(p.getColor());
                batch.draw(tex, p.x, p.y, p.width, p.height);
            }

            for(int i = 0; i < front; ++i) {
                Particle p = particles[i];
                batch.setColor(p.getColor());
                batch.draw(tex, p.x, p.y, p.width, p.height);
            }
        } else {
            for(int i = back; i < front; ++i)
            {
                Particle p = particles[i];
                batch.setColor(p.getColor());
                batch.draw(tex, p.x, p.y, p.width, p.height);
            }
        }
    }
}
