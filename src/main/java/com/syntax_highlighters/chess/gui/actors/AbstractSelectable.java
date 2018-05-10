package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Abstract class defining core selectable behavior.
 *
 * Selectable actors have a "selected" state that can be switched on and off,
 * and may specify an action that should be performed if they are selected.
 */
public abstract class AbstractSelectable extends Actor {
    private Callback callback;
    private boolean selected;
    private ISelectableGroup parentGroup;
    
    /**
     * Selection callback.
     *
     * Describes an action which should be performed if this selectable actor is
     * selected or deselected
     */
    public interface Callback {
        void callback(boolean selected);
    }

    /**
     * Define an action to perform when selection changed.
     *
     * @param callback The action to be performed
     */
    public void setOnSelectionChangedCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * Set selected state to true or false.
     *
     * If the selectable has a callback, and the selected state changed, perform
     * the callback.
     *
     * @param selected Whether the selectable is selected or not.
     */
    public void setSelected(boolean selected) {
        if (this.selected == selected) return; // nothing changed
        this.selected = selected;
        // selection state changed
        if (callback != null)
            callback.callback(selected);
        if (parentGroup != null)
            parentGroup.signalChanged(this);
    }
    
    /**
     * Determine if the selectable is currently selected.
     *
     * @return true if the selectable is selected, false otherwise
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Set the parent group of this selectable.
     *
     * The parent group is useful because it allows multiple selectables to be
     * grouped logically together, and may allow the user to specify something
     * that should happen if any button is selected.
     */
    public void setParent(ISelectableGroup parent) {
        this.parentGroup = parent;
    }
}
