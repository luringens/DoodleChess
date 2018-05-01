package com.syntax_highlighters.chess.gui.actors;

import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.WobbleDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RadioButton extends AbstractSelectable {
    private Texture circleTexture;
    private Texture dotTexture;
    private WobbleDrawable circle;
    private WobbleDrawable dot;
    private Text text;
    private boolean selected;
    private final static float RADIO_BUTTON_SIZE = 20f;
    private final static float RADIO_BUTTON_DOT_SIZE = 10f;

    public RadioButton(AssetManager assetManager, String buttonText, Color textColor) {
        text = new Text(AssetLoader.GetDefaultFont(assetManager, false));
        text.setText(buttonText);
        text.setColor(textColor);

        circleTexture = assetManager.get("circle.png", Texture.class);
        circle = new WobbleDrawable(circleTexture, assetManager);
        dotTexture = assetManager.get("dot.png", Texture.class);
        dot = new WobbleDrawable(dotTexture, assetManager);

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setSelected(true);
            }
        });
    }

    @Override
    public float getWidth() {
        return RADIO_BUTTON_SIZE + text.getWidth();
    }
    
    @Override
    public float getHeight() {
        return Math.max(RADIO_BUTTON_SIZE, text.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        
        setBounds(getX(), getY(), getWidth(), getHeight());

        circle.draw(batch, getX(), getY(), RADIO_BUTTON_SIZE, RADIO_BUTTON_SIZE);
        if (isSelected()) {
            float x = getX() + RADIO_BUTTON_SIZE/2.f - RADIO_BUTTON_DOT_SIZE/2.f;
            float y = getY() + RADIO_BUTTON_SIZE/2.f - RADIO_BUTTON_DOT_SIZE/2.f;
            dot.draw(batch, x, y, RADIO_BUTTON_DOT_SIZE, RADIO_BUTTON_DOT_SIZE);
        }
        text.setX(getX() + RADIO_BUTTON_SIZE);
        text.setY(getY() + RADIO_BUTTON_SIZE/2.f - text.getHeight()/2.f);
        text.draw(batch, 1.f);
    }
}
