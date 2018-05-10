package com.syntax_highlighters.chess.gui;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class WobbleDrawable extends BaseDrawable {
    private Texture texture;
    private static HashMap<String, ArrayList<FrameBuffer>> preRender = new HashMap<>();
    private static SpriteBatch spriteBatch = new SpriteBatch();
    private long lastframe = 0;
    private int textureId = 0;
    private boolean doAnimation = true;
    private Color tint = Color.WHITE;

    public WobbleDrawable(Texture texture, AssetManager assetManager) {
        this.texture = texture;
        ShaderProgram wobbleShader = assetManager.get("wobble.frag");
        ShaderProgram.pedantic = false;

        if (!wobbleShader.isCompiled()) {
            System.out.println(wobbleShader.getLog());
        }

        if (!preRender.containsKey(texture.toString()))
            for (int i = 0; i < 10; ++i) {
                renderBuffer(wobbleShader, texture);
            }
    }

    public WobbleDrawable(Texture texture, AssetManager assetManager, Color tint) {
        this(texture, assetManager);

        this.tint = tint;
    }

    private static void renderBuffer(ShaderProgram wobble, Texture texture) {
        final String id = texture.toString();
        if (!preRender.containsKey(id)) {
            preRender.put(id, new ArrayList<>());
        }
        SpriteBatch batch = spriteBatch;
        batch.setShader(wobble);
        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, texture.getWidth(), texture.getHeight(), false);
        buffer.begin();
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 0);

        batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        wobble.setUniformf("u_time", (float) Math.random() * 10000.f);
        wobble.setUniformf("u_resolution", new Vector2(texture.getWidth(), texture.getHeight()));

        batch.draw(texture, 0, 0, (int)(800 * 1.6), 800);
        batch.end();

        buffer.end();
        preRender.get(id).add(buffer);
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        final String id = texture.toString();
        if (!preRender.containsKey(id)) {
            // Error
            System.out.println("No texture for WobbleDrawable");
            return;
        }
        if (doAnimation && Gdx.graphics.getFrameId() - lastframe >= 60) {
            textureId = (int) (Math.random() * preRender.get(id).size());
            lastframe = Gdx.graphics.getFrameId();
        }
        Texture selected = preRender.get(id).get(textureId).getColorBufferTexture();
        selected.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.setColor(tint);
        batch.draw(selected, x, y, width, height, 0, 0, selected.getWidth(), selected.getHeight(), false, true);

    }

    public Texture getTexture() {
        if (this.texture == null) return null;
        final String id = texture.toString();
        if (textureId >= preRender.get(id).size() || textureId < 0)
            return texture;
        return preRender.get(id).get(textureId).getColorBufferTexture();
    }

    public void doAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }
}
