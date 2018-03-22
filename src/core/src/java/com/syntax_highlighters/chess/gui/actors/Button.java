package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.syntax_highlighters.chess.gui.AssetLoader;

import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Button extends Actor {
    private int textureId;
    private Text text;
    private boolean selected = false;

    private static final int TEXTURE_COUNT = 20;
    private static boolean hasRendered = false;
    public static ArrayList<FrameBuffer> preRenderedButtons = new ArrayList<>();
    private long lastFrame = 0;

    public Button(String buttonText, AssetManager manager)
    {
        ShaderProgram shader = new ShaderProgram(Gdx.files.internal("shaders/id.vert"), Gdx.files.internal("shaders/offset.frag"));

        Texture texture = manager.get("button_template.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // WTF OpenGl...
        ShaderProgram.pedantic = false;
        boolean built = shader.isCompiled();


        if(!hasRendered)
            renderTextures(TEXTURE_COUNT, texture, shader);

        this.text = new Text(AssetLoader.GetDefaultFont(manager));
        this.text.setText(buttonText);
        this.text.setColor(0,0,0,1);

        textureId = (int)(Math.random() * TEXTURE_COUNT);
    }

    private void renderTextures(int textureCount, Texture template, ShaderProgram shader) {

        // TODO: pass spritebatch
        SpriteBatch batch = new SpriteBatch();
        batch.setShader(shader);
        for(int i = 0; i < textureCount; ++i)
        {
            FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, template.getWidth(), template.getHeight(), false);
            buffer.begin();
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl.glClearColor(0,0,0,0);
            batch.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl.glClearColor(0,0,0,0);
            shader.setUniformf("u_time", (float)Math.random() * 10000.f);

            batch.draw(template, 0, 0, 800, 800);

            batch.end();
            buffer.end();

            // Might not work
            preRenderedButtons.add(buffer);
        }
        batch.setShader(null);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
}

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if(Gdx.graphics.getFrameId() - lastFrame >= 60)
        {
            textureId = (int)(Math.random() * (TEXTURE_COUNT-1));
            lastFrame = Gdx.graphics.getFrameId();
        }

        if(this.selected)
        {
            batch.setColor( new Color(0.443f, 0.7372f, 0.470f, 1));
        }
        else
        {
            batch.setColor(Color.BLACK);
        }
        batch.draw(preRenderedButtons.get(textureId).getColorBufferTexture(), getX(),getY(),getWidth(),getHeight());
        //batch.draw(preRenderedButtons.get(textureId).getColorBufferTexture(), getX(),getY());

        text.setCenter(getX() + getWidth()/2.f, getY() + getHeight()/2.f);

        text.draw(batch, 1.f);

    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
