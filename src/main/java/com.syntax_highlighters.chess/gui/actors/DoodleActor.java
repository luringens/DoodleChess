package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.Gdx;

public class DoodleActor extends Actor {
    private Texture tex;
    private float angle;
    private final static float CENTER_DISTANCE = 700.f;
    private final static float CENTER_OFFSET_BOUND = 40.f;
    private final static float SIZE = 200.f;

    private final float center_offset;

    public DoodleActor(Texture texture, float angle) {
        this.tex = texture;
        this.angle = angle;

        center_offset = (float)Math.random() * CENTER_OFFSET_BOUND;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float x = width/2 + ((float)Math.cos(angle))*(CENTER_DISTANCE + center_offset) - tex.getWidth()/2.f;
        float y = height/2 + ((float)Math.sin(angle))*(CENTER_DISTANCE + center_offset) - tex.getHeight()/2.f;
        batch.draw(tex, x, y, SIZE, SIZE);
    }
}
