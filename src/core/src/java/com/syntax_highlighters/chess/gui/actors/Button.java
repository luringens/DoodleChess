package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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
    int time = 0;

    public Button(AssetManager manager)
    {
        shader = new ShaderProgram(Gdx.files.internal("shaders/id.vert"), Gdx.files.internal("shaders/offset.frag"));

        texture = manager.get("button_template.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        // WTF OpenGl...
        ShaderProgram.pedantic = false;
        boolean built = shader.isCompiled();
        System.out.println(shader.getLog());
        System.out.println("Shader compiled " + (built ? "true" : "false"));

        Texture texture = new Texture(Gdx.files.internal("segoeui.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont font = new BitmapFont(Gdx.files.internal("segoeui.fnt"), new TextureRegion(texture), false);
        text = new Text(font);
        text.setText("Play");
        text.setColor(0,0,0,1);

        time = (int)(Math.random() * 10000.0f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        time+=1;

        batch.setShader(shader);

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        shader.setUniformi("u_texture", 0);
        Vector2 size = new Vector2(texture.getWidth(), texture.getHeight());
        shader.setUniformf("u_time", time / 100.f);
        shader.setUniformf("u_resolution", size);
        batch.draw(texture, getX(),getY(),getWidth(),getHeight());

        text.setCenter(getX() + getWidth()/2.f, getY() + getHeight()/2.f);

        batch.setShader(null);
        text.draw(batch, 1.f);

    }
}
