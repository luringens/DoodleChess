package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Overlay extends Actor {
    private Texture pixel;
    private Texture overlay;

    public Overlay(AssetManager assetManager) {
        pixel = assetManager.get("pixel.png", Texture.class);
        overlay = assetManager.get("overlay.png", Texture.class);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(0,0,0,075f);
        batch.draw(pixel, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float cw = Gdx.graphics.getWidth() / 2.f;
        float ch = Gdx.graphics.getHeight() / 2.f;
        float tw = overlay.getWidth();
        float th = overlay.getHeight();
        setBounds(cw - tw / 2.f, ch - th/2.f, tw, th);
        batch.setColor(1,1,1,1);
        batch.draw(overlay, getX(), getY(), getWidth(), getHeight());
    }
}
