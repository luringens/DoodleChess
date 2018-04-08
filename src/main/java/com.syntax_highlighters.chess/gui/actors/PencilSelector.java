package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class PencilSelector extends Actor {
    private ArrayList<Pencil> pencils = new ArrayList<>();

    private int hovered = -1;

    public PencilSelector(AssetManager manager) {
        super();

        pencils.add(new Pencil(manager, Color.WHITE));
        pencils.add(new Pencil(manager, Color.BLACK));
        for(float i = 0; i < 16; ++i)
        {
            Color test = new Color(1,1,1,1);
            float percentage = i / 16.f;
            float hue = percentage * 360.f;
            pencils.add(new Pencil(manager, test.fromHsv(hue, 1.0f, 1.0f)));
        }

        this.setWidth(32.f * pencils.size());
        this.setHeight(512.f + 48);

        Actor self = this;

        addListener(new ClickListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                int index = (int)(x / 32.f);
                if(index != hovered && index >= 0 && index < pencils.size())
                {

                    if(hovered != -1)
                    {
                        Pencil hoveredP = pencils.get(hovered);
                        MoveToAction action = new MoveToAction();
                        action.setPosition(hoveredP.getX(), self.getY() + 48 * ((float)Math.random() * 2.0f - 1.0f));
                        action.setDuration(0.1f);
                        hoveredP.addAction(action);
                    }
                    Pencil hoveredP = pencils.get(index);
                    MoveToAction action = new MoveToAction();
                    action.setPosition(hoveredP.getX(), self.getY() + 100);
                    action.setDuration(0.1f);
                    hoveredP.addAction(action);

                    hovered = index;
                }

                return super.mouseMoved(event, x, y);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if(hovered != -1)
                {
                    Pencil hoveredP = pencils.get(hovered);
                    MoveToAction action = new MoveToAction();
                    action.setPosition(hoveredP.getX(), self.getY() + 48 * ((float)Math.random() * 2.0f - 1.0f));
                    action.setDuration(0.1f);
                    hoveredP.addAction(action);
                }
                hovered = -1;
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (Pencil pencil : pencils) {
            pencil.act(delta);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        int i = 0;
        for (Pencil pencil : pencils) {
            pencil.setPosition(x + 32 * (i++), y + 48 * ((float)Math.random() * 2.0f - 1.0f));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        for (Pencil pencil : pencils) {
            pencil.draw(batch, parentAlpha);
        }
    }
}
