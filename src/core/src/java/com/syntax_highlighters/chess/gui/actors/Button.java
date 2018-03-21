package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Button extends Actor {
    ShaderProgram shader;
    Texture texture;
    Text text;
    int timeOffset = 0;
    boolean selected = false;

    public Button(String buttonText, AssetManager manager)
    {
        shader = new ShaderProgram(Gdx.files.internal("shaders/id.vert"), Gdx.files.internal("shaders/offset.frag"));

        texture = manager.get("button_template.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        // WTF OpenGl...
        ShaderProgram.pedantic = false;
        boolean built = shader.isCompiled();

        Texture texture = new Texture(Gdx.files.internal("segoeui.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont font = new BitmapFont(Gdx.files.internal("segoeui.fnt"), new TextureRegion(texture), false);
        this.text = new Text(font);
        this.text.setText(buttonText);
        this.text.setColor(0,0,0,1);

        timeOffset = (int)(Math.random() * 100.f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
}

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.setShader(shader);

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        shader.setUniformi("u_texture", 0);
        Vector2 size = new Vector2(texture.getWidth(), texture.getHeight());
        shader.setUniformf("u_time", Gdx.graphics.getFrameId() / 100.f + timeOffset);
        shader.setUniformf("u_resolution", size);

        if(this.selected)
        {
            shader.setUniformf("u_color", new Color(0.443f, 0.7372f, 0.470f, 1));
        }
        else
        {
            shader.setUniformf("u_color", Color.BLACK);
        }
        batch.draw(texture, getX(),getY(),getWidth(),getHeight());

        text.setCenter(getX() + getWidth()/2.f, getY() + getHeight()/2.f);

        batch.setShader(null);
        text.draw(batch, 1.f);

    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
