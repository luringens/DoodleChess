package com.syntax_highlighters.chess.gui.actors;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.Audio;

/**
 * Button class for easier button creation.
 *
 * Creates buttons with a cool wavy effect.
 */
public class Button extends Actor {
    private int textureId;
    private final Text text;
    private boolean selected = false;

    private static final int TEXTURE_COUNT = 20;
    private static final ArrayList<FrameBuffer> preRenderedButtons = new ArrayList<>();
    private long lastFrame = 0;

    public Button(String buttonText, AssetManager manager)
    {
        ShaderProgram shader = new ShaderProgram(Gdx.files.internal("shaders/id.vert"), Gdx.files.internal("shaders/offset.frag"));

        Texture texture = manager.get("button_template.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // WTF OpenGl...
        ShaderProgram.pedantic = false;

        if(!shader.isCompiled())
        {
            System.out.println(shader.getLog());
        }

        boolean hasRendered = false;
        if(!hasRendered)
            renderTextures(TEXTURE_COUNT, texture, shader);

        this.text = new Text(AssetLoader.GetDefaultFont(manager));
        this.text.setText(buttonText);
        this.text.setColor(0,0,0,1);

        textureId = (int)(Math.random() * TEXTURE_COUNT);

        this.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event,float x, float y, int pointer, Actor fromActor){
                super.enter(event,x,y,pointer,fromActor);
                Audio.menuSound(manager);
            }
        });
    }

    /**
     * Button builder for easier, more declarative button creation.
     */
    public static class Builder {
        private final Button b;
        public Builder(String text, AssetManager assetManager) {
            b = new Button(text, assetManager);
        }

        /**
         * Set the button's visibility.
         *
         * @param v Whether or not the button should be visible initially
         * @return This Builder
         */
        public Builder visible(boolean v) {
            b.setVisible(v);
            return this;
        }
        
        /**
         * Set the callback as a click listener for this button.
         *
         * @param c The callback function
         * 
         * @return This Builder
         */
        public Builder callback(Callback c) {
            if (c != null) {
                b.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        c.callback();
                    }
                });
            }
            return this;
        }
        
        /**
         * Stage the button as an actor to the stage.
         *
         * @param s The stage this button should be added to
         *
         * @return This Builder
         */
        public Builder stage(Stage s) {
            s.addActor(b);
            return this;
        }

        /**
         * Specify the size of this button.
         * 
         * @param width The width of the button
         * @param height The height of the button
         *
         * @return This Builder
         */
        public Builder size(float width, float height) {
            b.setSize(width, height);
            return this;
        }

        /**
         * Specify the size of this button
         * @param x The x position of the button
         * @param y The y position of the button
         * @return This object.
         */
        public Builder position(float x, float y) {
            b.setPosition(x, y);
            return this;
        }

        /**
         * Return the specified button.
         *
         * @return The newly created Button
         */
        public Button create() {
            return b;
        }
    }

    /**
     * Callback interface for ease of adding listeners.
     */
    public interface Callback {
        /**
         * The action to be performed when the button is clicked.
         *
         * Does not carry information about the event or the x/y location, only
         * that the button *was* clicked.
         */
        void callback();
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
            shader.setUniformf("u_resolution", new Vector2(template.getWidth(), template.getHeight()));

            batch.draw(template, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            batch.end();
            buffer.end();

            // Might not work
            preRenderedButtons.add(buffer);
        }
        batch.setShader(null);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if(Gdx.graphics.getFrameId() - lastFrame >= 60)
        {
            int old = textureId;
            while(old == textureId)
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
        Texture tex = preRenderedButtons.get(textureId).getColorBufferTexture();
        batch.draw(tex, getX(),getY(),getWidth(),getHeight(), 0, 0, tex.getWidth(), tex.getHeight(), false, true);

        text.setCenter(getX() + getWidth()/2.f, getY() + getHeight()/2.f);

        text.draw(batch, 1.f);

    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
