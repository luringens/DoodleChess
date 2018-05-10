package com.syntax_highlighters.chess.gui.actors;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PencilSelector extends Group {
    private final Image background;
    private final ArrayList<Pencil> pencils = new ArrayList<>();
    private final ArrayList<Float> pencilOffsets = new ArrayList<>();
    private final ColorAction colorAction;

    private float myY = 0;


    public PencilSelector(AssetManager manager) {
        super();

        background = new Image(manager.get("pixel.png", Texture.class));
        background.setPosition(-1000, -1000);
        background.setSize(10000, 10000);
        background.setColor(0,0,0,0.5f);
        background.setName("Background");
        PencilSelector self = this;
        background.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                self.hide(1.0f);
            }
        });

        addPencil(0, new Pencil(manager, Color.WHITE));
        pencilOffsets.add(48 * ((float)Math.random() * 2.0f-1.0f));
        addPencil(1, new Pencil(manager, Color.BLACK));
        pencilOffsets.add(48 * ((float)Math.random() * 2.0f-1.0f));
        for(int i = 0; i < 16; ++i)
        {
            Color test = new Color(1,1,1,1);
            float percentage = i / 16.f;
            float hue = percentage * 360.f;

            addPencil(i + 2, new Pencil(manager, test.fromHsv(hue + 16.f, 1.0f, 1.0f)));
            pencilOffsets.add(48 * ((float)Math.random() * 2.0f-1.0f));
        }

        colorAction = new ColorAction();
        colorAction.setDuration(0.2f);
        colorAction.setInterpolation(Interpolation.linear);
        colorAction.setColor(background.getColor());

        this.setWidth(32.f * pencils.size());
        this.setHeight(512.f + 48);
    }

    private void addPencil(int id, Pencil pencil)
    {
        addActor(pencil);
        pencils.add(pencil);

        pencil.setSize(32, 512);

        Actor self = this;
        pencil.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                MoveToAction action = new MoveToAction();
                action.setPosition(pencil.getX(), 100);
                action.setDuration(0.1f);
                action.setInterpolation(Interpolation.pow2);
                pencil.addAction(action);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                MoveToAction action = new MoveToAction();
                action.setPosition(pencil.getX(), pencilOffsets.get(id));
                action.setDuration(0.1f);
                action.setInterpolation(Interpolation.pow2);
                pencil.addAction(action);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (!pencil.isSelected())
                    self.fire(new ColorSelectEvent(pencil));
            }
        });
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        int i = 0;
        for (Pencil pencil : pencils) {
            pencil.setX(32 * (i));
            MoveToAction action = new MoveToAction();
            action.setPosition(32 * (i), pencilOffsets.get(i++));
            action.setDuration(0.1f);
            action.setInterpolation(Interpolation.pow2);
            pencil.addAction(action);
        }
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        super.setPosition(x, y, alignment);
        int i = 0;
        for (Pencil pencil : pencils) {
            MoveToAction action = new MoveToAction();
            action.setPosition(32 * (i), pencilOffsets.get(i++));
            action.setDuration(0.1f);
            action.setInterpolation(Interpolation.pow2);
            pencil.addAction(action);
        }
    }

    public void hide(float duration) {
        myY = getY();
        MoveToAction action = new MoveToAction();
        action.setPosition(this.getX(), -600);
        action.setDuration(duration);
        action.setInterpolation(Interpolation.pow4);
        this.addAction(action);

        background.setColor(0,0,0,0.5f);
        colorAction.setColor(background.getColor());
        colorAction.setEndColor(new Color(0,0,0,0.0f));
        colorAction.reset();

        RunnableAction callback = new RunnableAction();
        callback.setRunnable(() -> this.removeActor(background));
        if(this.findActor("Background") != null)
            background.addAction(new SequenceAction(Actions.delay(duration/2.f), colorAction, callback));
    }

    public void show(float duration) {
        MoveToAction action = new MoveToAction();
        action.setPosition(this.getX(), myY);
        action.setDuration(duration);
        action.setInterpolation(Interpolation.pow4);
        this.addAction(action);

        background.setColor(0,0,0,0.0f);
        addActorAt(0,background);
        colorAction.setColor(background.getColor());
        colorAction.setEndColor(new Color(0,0,0,0.5f));
        colorAction.reset();
        background.addAction(colorAction);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(colorAction.getColor() != null)
            background.setColor(colorAction.getColor());
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
        private final Pencil pencil;

        ColorSelectEvent(Pencil pencil) {
            this.pencil = pencil;
        }

        Color getColor() {
            return pencil.getColor();
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
