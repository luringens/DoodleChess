package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;

public class PaperBackground extends Actor {
    private final Texture pixel;
    private FrameBuffer paperBuffer;
    private ShaderProgram noiseShader;
    boolean first = true;

    public PaperBackground(AssetManager manager) {
        pixel = manager.get("pixel.png");
        paperBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int)LibgdxChessGame.WORLDWIDTH, (int)LibgdxChessGame.WORLDHEIGHT, false);
        noiseShader = new ShaderProgram(Gdx.files.internal("shaders/id.vert"), Gdx.files.internal("shaders/noise.frag"));
    }

    /**
     * Will regenerate the wrinkles for the background using a custom built shader.
     * We generate it in an offscreen buffer to prevent lag from computing the image every single draw call.
     *
     * For more information on how the shader works, check out "assets/shaders/paper.frag"
     */
    private void recomputeBackground(Batch batch)
    {
        Gdx.gl.glViewport(0, 0, (int)LibgdxChessGame.WORLDWIDTH, (int)LibgdxChessGame.WORLDHEIGHT);
        batch.setShader(noiseShader);
        Matrix4 trans = batch.getTransformMatrix();
        batch.setTransformMatrix(new Matrix4());
        paperBuffer.begin();
        batch.begin();
        noiseShader.setUniformf("u_offset", new Vector2((float)Math.random() * 100.f, (float)Math.random() * 100.f));
        batch.draw(paperBuffer.getColorBufferTexture(), 0, 0, LibgdxChessGame.WORLDWIDTH, LibgdxChessGame.WORLDHEIGHT);
        batch.end();
        paperBuffer.end();
        batch.setShader(null);
        batch.setTransformMatrix(trans);
        first = false;
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(first){
            batch.end();
            recomputeBackground(batch);
            batch.begin();
        }
        super.draw(batch, parentAlpha);
        batch.draw(paperBuffer.getColorBufferTexture(), 0, 0, LibgdxChessGame.WORLDWIDTH, LibgdxChessGame.WORLDHEIGHT);
    }
}
