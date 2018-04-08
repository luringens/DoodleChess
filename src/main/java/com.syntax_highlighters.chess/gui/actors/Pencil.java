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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

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

