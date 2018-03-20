package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Text extends Actor {
    private BitmapFont font;
    private String text;

    public Text(BitmapFont font) {
        this.font = font;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.setColor(getColor());
        font.draw(batch, text, getX(), getY());
    }
}
