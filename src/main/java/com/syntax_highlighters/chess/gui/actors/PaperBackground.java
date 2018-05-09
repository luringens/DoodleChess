package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;

public class PaperBackground extends Actor {
    private Texture pixel;

    public PaperBackground(AssetManager manager) {
        pixel = manager.get("pixel.png");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(pixel, 0, 0, LibgdxChessGame.WORLDWIDTH, LibgdxChessGame.WORLDHEIGHT);
    }
}
