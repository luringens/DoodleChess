package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.syntax_highlighters.chess.Board;

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

    public void setCenter(float x, float y) {
        if(text == null) return;

        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, text);

        setX(x - layout.width/2.f);
        setY(y + layout.height/2.f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.setColor(getColor());
        font.draw(batch, text, getX(), getY());
    }
}
