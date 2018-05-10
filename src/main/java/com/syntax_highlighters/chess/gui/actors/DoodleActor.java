package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DoodleActor extends Actor {
    private Texture tex;
    private float angle;
    private final static float CENTER_DISTANCE = 650.f;
    private final static float CENTER_OFFSET_BOUND = 200.f;
    private final static float SIZE = 150.f;

    private final float center_offset;
    private final float random_size;

    public DoodleActor(Texture texture, float angle) {
        this.tex = texture;
        this.angle = angle;

        center_offset = (float)Math.random() * CENTER_OFFSET_BOUND;
        random_size = (float)Math.random() * (SIZE - 50.f) + 100.f;

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float x = width/2 + ((float)Math.cos(angle))*(CENTER_DISTANCE + center_offset) - tex.getWidth()/2.f;
        float y = height/2 + ((float)Math.sin(angle))*(CENTER_DISTANCE + center_offset) - tex.getHeight()/2.f;
        if(x < 0) x = 0;
        if(y < 0) y = 0;
        if(x > Gdx.graphics.getWidth() - random_size) x = Gdx.graphics.getWidth() - random_size;
        if(y > Gdx.graphics.getHeight() - random_size) y = Gdx.graphics.getHeight() - random_size;
        batch.setColor(1,1,1,1);
        batch.draw(tex, x, y, random_size, random_size);
    }
}
