package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.WobbleDrawable;

public abstract class AbstractSelectableButton extends AbstractSelectable {
    protected Texture boxTexture;
    protected Texture checkedTexture;
    protected WobbleDrawable box;
    protected WobbleDrawable checked;
    private Text text;
    private final static float SELECTABLE_BUTTON_SIZE = 20f;
    protected AssetManager assetManager;

    public AbstractSelectableButton(AssetManager am, String buttonText, Color textColor) {
        this.assetManager = am;
        text = new Text(AssetLoader.GetDefaultFont(assetManager, false));
        text.setText(buttonText);
        text.setColor(textColor);
        
        setSelectableTexture();
    }

    protected abstract void setSelectableTexture();

    @Override
    public float getWidth() {
        return SELECTABLE_BUTTON_SIZE + text.getWidth();
    }

    @Override
    public float getHeight() {
        return Math.max(SELECTABLE_BUTTON_SIZE, text.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        
        setBounds(getX(), getY(), getWidth(), getHeight());

        box.draw(batch, getX(), getY(), SELECTABLE_BUTTON_SIZE, SELECTABLE_BUTTON_SIZE);
        if (isSelected()) {
            checked.draw(batch, getX(), getY(), SELECTABLE_BUTTON_SIZE, SELECTABLE_BUTTON_SIZE);
        }
        text.setX(getX() + SELECTABLE_BUTTON_SIZE);
        text.setY(getY() + SELECTABLE_BUTTON_SIZE/2.f - text.getHeight()/2.f);
        text.draw(batch, 1.f);
    }
}
