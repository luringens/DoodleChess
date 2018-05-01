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

public class RadioGroup extends Group implements ISelectableGroup {
    List<RadioButton> buttons = new ArrayList<>();
    private AssetManager assetManager;
    private boolean horizontal;
    private Callback callback; // callback when something changes

    public RadioGroup(AssetManager assetManager, boolean horizontal) {
        this.assetManager = assetManager;
        this.horizontal = horizontal;
    }

    /**
     * Perform an action if a button has been selected.
     *
     * If a button in a RadioGroup is selected, all the others should be
     * deselected.
     *
     * @param changed The changed button
     */
    @Override
    public void signalChanged(AbstractSelectable changed) {
        if (!changed.isSelected()) return; // ignore deselected buttons
        // if changed was selected, deselect all the others
        for (RadioButton rb : buttons) {
            if (rb != changed) {
                rb.setSelected(false);
            }
        }

        callback.callback();
    }


    /**
     * Helper method: add a new radio button with the given text and no
     * callback.
     *
     * @param s The text beside the radio button
     */
    public void addButton(String s) {
        addButton(s, null);
    }

    /**
     * Helper method: add a new radio button with the given text and callback.
     *
     * @param s The text beside the radio button
     * @param callback The callback to be performed on selection change
     */
    public void addButton(String s, RadioButton.Callback callback) {
        RadioButton rb = new RadioButton(assetManager, s, Color.BLACK);
        if (buttons.size() == 0) rb.setSelected(true); // select first button by default
        
        addSelectable(rb, callback);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addSelectable(AbstractSelectable s, AbstractSelectable.Callback callback) {
        if (!(s instanceof RadioButton)) {
            throw new IllegalArgumentException("Argument must be RadioButton");
        }
        
        RadioButton rb = (RadioButton)s;
        
        rb.setParent(this);
        rb.setOnSelectionChangedCallback(callback);
        buttons.add(rb);
        addActor(rb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return buttons.stream()
            .map(b -> b.getHeight())
            .reduce(0f, (x, y) -> horizontal? Math.max(x, y) : x+y);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return buttons.stream()
            .map(b -> b.getWidth())
            .reduce(0f, (x, y) -> horizontal ? x+y : Math.max(x,y));
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null) return;
        for (RadioButton rb : buttons) {
            stage.addActor(rb);
        }
    }

    /**
     * Retrieve the index of the selected radio button.
     *
     * @return The index of the selected button.
     */
    public int getSelectedIndex() {
        for (int i = 0; i < buttons.size(); i++) {
            RadioButton rb = buttons.get(i);
            if (rb.isSelected()) return i;
        }
        return -1;
    }

    /**
     * Determine the number of radio buttons in this group.
     *
     * @return The number of buttons that belong to this group of buttons.
     */
    @Override
    public int size() {
        return buttons.size();
    }

    /**
     * Get a button by index.
     *
     * @param index The index of the button
     * @return The button indexed by the given index
     */
    @Override
    public RadioButton get(int index) {
        return buttons.get(index);
    }

    /**
     * Set a general action which should be performed when the selection
     * changes.
     *
     * @param callback The action to be performed on selection changed
     */
    public void setOnSelectionChangeCallback(Callback callback) {
        this.callback = callback;
    }

    public static interface Callback {
        void callback();
    }
} 
