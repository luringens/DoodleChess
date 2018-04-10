package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class PencilSelector extends Actor {
    private ArrayList<Pencil> pencils = new ArrayList<>();
    private ArrayList<Float> pencilOffsets = new ArrayList<>();

    private int hovered = -1;

    private float myY = 0;

    private boolean hidden = true;

    public PencilSelector(AssetManager manager) {
        super();

        pencils.add(new Pencil(manager, Color.WHITE));
        pencilOffsets.add(48 * ((float)Math.random() * 2.0f-1.0f));
        pencils.add(new Pencil(manager, Color.BLACK));
        pencilOffsets.add(48 * ((float)Math.random() * 2.0f-1.0f));
        for(float i = 0; i < 16; ++i)
        {
            Color test = new Color(1,1,1,1);
            float percentage = i / 16.f;
            float hue = percentage * 360.f;
            pencils.add(new Pencil(manager, test.fromHsv(hue + 16.f, 1.0f, 1.0f)));
            pencilOffsets.add(48 * ((float)Math.random() * 2.0f-1.0f));
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
                        action.setPosition(hoveredP.getX(), self.getY() + pencilOffsets.get(hovered));
                        action.setDuration(0.1f);
                        action.setInterpolation(Interpolation.pow2);
                        hoveredP.addAction(action);
                    }
                    Pencil hoveredP = pencils.get(index);
                    MoveToAction action = new MoveToAction();
                    action.setPosition(hoveredP.getX(), self.getY() + 100);
                    action.setDuration(0.1f);
                    action.setInterpolation(Interpolation.pow2);
                    hoveredP.addAction(action);

                    hovered = index;
                }

                return super.mouseMoved(event, x, y);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                int index = (int)(x / 32.f);
                if(index >= 0 && index < pencils.size())
                {
                    Pencil selectedP = pencils.get(index);
                    if (!selectedP.isSelected())
                        self.fire(new ColorSelectEvent(pencils.get(index)));
                }

            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if(hovered != -1)
                {
                    Pencil hoveredP = pencils.get(hovered);
                    MoveToAction action = new MoveToAction();
                    action.setPosition(hoveredP.getX(), self.getY() + pencilOffsets.get(hovered));
                    action.setDuration(0.1f);
                    action.setInterpolation(Interpolation.pow2);
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
            pencil.setPosition(x + 32 * (i), y + pencilOffsets.get(i++));
        }
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        super.setPosition(x, y, alignment);
        int i = 0;
        for (Pencil pencil : pencils) {
            pencil.setPosition(x + 32 * (i), y + pencilOffsets.get(i++));
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for (Pencil pencil : pencils) {
            if(pencil.isSelected()) continue;
            pencil.draw(batch, parentAlpha);
        }
    }

    public void hide(float duration) {
        hovered = -1;

        hidden = false;
        myY = getY();
        MoveToAction action = new MoveToAction();
        action.setPosition(this.getX(), -600);
        action.setDuration(duration);
        action.setInterpolation(Interpolation.pow4);
        this.addAction(action);
    }

    public void show(float duration) {
        hovered = -1;
        hidden = true;
        MoveToAction action = new MoveToAction();
        action.setPosition(this.getX(), myY);
        action.setDuration(duration);
        action.setInterpolation(Interpolation.pow4);
        this.addAction(action);
    }

    public void releaseColor(Color color) {
        for(Pencil pen : pencils)
        {
            if(pen.getColor() == color)
            {
                pen.setSelected(false);
            }
        }
    }

    public void selectColor(Color color) {
        for(Pencil pen : pencils)
        {
            if(pen.getColor() == color)
            {
                pen.setSelected(true);
            }
        }
    }

    public class ColorSelectEvent extends Event {
        private Pencil pencil;

        public ColorSelectEvent(Pencil pencil) {
            this.pencil = pencil;
        }

        public Color getColor() {
            return pencil.getColor();
        }

        public Pencil getPencil() {
            return pencil;
        }
    }

    public abstract static class ColorSelectListener implements EventListener {

        @Override
        public boolean handle(Event event) {
            if(event instanceof ColorSelectEvent)
            {
                ColorSelectEvent colorEvent = (ColorSelectEvent) event;
                colorSelected(colorEvent, colorEvent.getColor());
                return true;
            }
            return false;
        }

        public abstract void colorSelected(ColorSelectEvent event, Color color);
    }
}
