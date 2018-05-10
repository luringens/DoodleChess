package com.syntax_highlighters.chess.gui.actors;

interface ISelectableGroup {
    void signalChanged(AbstractSelectable changed);
    void addSelectable(AbstractSelectable selectable, AbstractSelectable.Callback callback);
    int size();
    AbstractSelectable get(int index);
}
