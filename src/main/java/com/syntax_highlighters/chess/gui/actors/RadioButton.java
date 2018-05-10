package com.syntax_highlighters.chess.gui.actors;

import com.syntax_highlighters.chess.gui.WobbleDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RadioButton extends AbstractSelectableButton {
    public RadioButton(AssetManager assetManager, String buttonText, Color textColor) {
        super(assetManager, buttonText, textColor);
        
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setSelected(true);
            }
        });
    }

    @Override
    protected void setSelectableTexture() {
        boxTexture = assetManager.get("circle.png", Texture.class);
        boxTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        box = new WobbleDrawable(boxTexture, assetManager);
        checkedTexture = assetManager.get("dot.png", Texture.class);
        checkedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        checked = new WobbleDrawable(checkedTexture, assetManager);
    }
}
