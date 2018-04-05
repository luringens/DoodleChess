package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.syntax_highlighters.chess.gui.AssetLoader;

/**
 * Abstract overlay class implementing common functionality between overlays.
 */
public class AbstractOverlay extends Actor {
    private final Texture pixel;
    private final Texture overlay;
    private final Text title;

    public AbstractOverlay(String titleText, AssetManager assetManager) {
        pixel = assetManager.get("pixel.png", Texture.class);
        overlay = assetManager.get("overlay.png", Texture.class);

        BitmapFont font = AssetLoader.GetDefaultFont(assetManager);
        title = new Text(font);
        title.setText(titleText);
        title.setColor(0,0,0,1);
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

        title.setCenter(getX() + getWidth()/2.f, getY() + getHeight() - 60);
        title.draw(batch, parentAlpha);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return this;
    }
}
