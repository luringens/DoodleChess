package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

class MenuIcon extends Actor {
    private final Texture bgr;
    private final Texture icon;
    public static final float SIZE = 80;
    private static final int BORDER_THICKNESS = 2;
    
    public MenuIcon(Texture icon) {
        this.icon = icon;
        
        // create the background as simply a white square
        int iSize = (int)SIZE;
        Pixmap pixmap = new Pixmap(iSize, iSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0, 0, iSize, iSize);
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(BORDER_THICKNESS, BORDER_THICKNESS,
            iSize-2* BORDER_THICKNESS, iSize-2* BORDER_THICKNESS);
        this.bgr = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(bgr, getX(), getY(), getWidth(), getHeight());
        batch.draw(icon, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public float getWidth() {
        return SIZE;
    }

    @Override
    public float getHeight() {
        return SIZE;
    }
}
