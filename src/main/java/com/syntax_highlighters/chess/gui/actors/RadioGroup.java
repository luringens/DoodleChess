package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import java.util.List;
import java.util.ArrayList;

public class RadioGroup extends Group {
    List<RadioButton> buttons = new ArrayList<>();
    private AssetManager assetManager;
    private boolean horizontal;
    private Callback callback;

    public RadioGroup(AssetManager assetManager, boolean horizontal) {
        this.assetManager = assetManager;
        this.horizontal = horizontal;
    }

    protected void signalClicked() {
        if (callback != null)
            callback.callback();
    }

    public void addButton(String s) {
        addButton(s, null);
    }

    public void addButton(String s, RadioButton.Callback callback) {
        RadioButton rb = new RadioButton(assetManager, s, Color.BLACK);
        rb.setParent(this);
        rb.setSelectionCallback(callback);
        buttons.add(rb);
        if (buttons.size() == 1) rb.setSelected(true);
        addActor(rb);
    }

    public void deselectAll() {
        for (RadioButton rb : buttons) {
            rb.setSelected(false);
        }
    }

    @Override
    public float getHeight() {
        return buttons.stream()
            .map(b -> b.getHeight())
            .reduce(0f, (x, y) -> horizontal? Math.max(x, y) : x+y);
    }
    
    @Override
    public float getWidth() {
        return buttons.stream()
            .map(b -> b.getWidth())
            .reduce(0f, (x, y) -> horizontal ? x+y : Math.max(x,y));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        setBounds(getX(), getY(), getWidth(), getHeight());
        
        for (int i = 0; i < buttons.size(); i++) {
            RadioButton rb = buttons.get(i);
            if (horizontal)
                rb.setPosition(getX()+i*rb.getWidth(), getY());
            else  {
                float y = getY()+(buttons.size()-i-1)*rb.getHeight();
                rb.setPosition(getX(), y);
            }
            rb.draw(batch, parentAlpha);
        }
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null) return;
        for (RadioButton rb : buttons) {
            stage.addActor(rb);
        }
    }

    public static interface Callback {
        void callback();
    }

    public void setSelectionCallback(Callback callback) {
        this.callback = callback;
    }

    public int getSelectedIndex() {
        for (int i = 0; i < buttons.size(); i++) {
            RadioButton rb = buttons.get(i);
            if (rb.isSelected()) return i;
        }
        return -1;
    }
} 
