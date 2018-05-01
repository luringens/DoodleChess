package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;


public class PopupSelectionMenu extends Actor {
    private List<MenuIcon> buttons = new ArrayList<>();

    private ClickListener listener;
    
    public PopupSelectionMenu(String title, Texture ...buttonIcons) {
        buttons = new ArrayList<>();
        for (Texture t : buttonIcons) {
            buttons.add(new MenuIcon(t));
        }
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        for (int i = 0; i < buttons.size(); i++) {
            MenuIcon ic = buttons.get(i);
            ic.setX(x + i * MenuIcon.SIZE);
        }
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        for (int i = 0; i < buttons.size(); i++) {
            MenuIcon ic = buttons.get(i);
            ic.setY(y);
        }
    }

    @Override
    public float getWidth() {
        return buttons.size()*MenuIcon.SIZE;
    }

    @Override
    public float getHeight() {
        return MenuIcon.SIZE;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for (MenuIcon ic : buttons) {
            ic.draw(batch, parentAlpha);
        }
        float x = getX();
        float y = getY();
        this.setBounds(x, y, getWidth(), getHeight());
    }

    public void setListener(ClickListener listener) {
        if (this.listener != null)
            this.removeListener(this.listener);
        this.listener = listener;
        this.addListener(listener);
    }
}
