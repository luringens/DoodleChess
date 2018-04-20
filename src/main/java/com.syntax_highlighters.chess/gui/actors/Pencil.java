package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Pencil extends Actor {
    private Color color;
    private ShaderProgram setColorShader;

    private final int baseCount = 2;
    private final int woodCount = 3;

    private Texture base;
    private Texture wood;

    private boolean isSelected = false;

    public Pencil(AssetManager manager, Color color) {
        super();

        setColorShader = new ShaderProgram(Gdx.files.internal("shaders/id.vert"), Gdx.files.internal("shaders/setColorSimple.frag"));

        // TODO: Randomize

        int tex = (int)(Math.random() * baseCount) + 1;
        base = manager.get("pencils/base" + tex + ".png");

        tex = (int)(Math.random() * woodCount) + 1;
        wood = manager.get("pencils/wood" + tex + ".png", Texture.class);

        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(isSelected) return;

        batch.setColor(1,1,1,1);
        ShaderProgram program = batch.getShader();
        batch.flush();
        batch.setShader(setColorShader);
        setColorShader.setUniformf("u_tint", color);
        batch.draw(base, getX(), getY());
        batch.flush();
        batch.setShader(program);
        batch.draw(wood, getX(), getY());
    }


}

