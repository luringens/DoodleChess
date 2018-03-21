package com.syntax_highlighters.chess;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.syntax_highlighters.chess.gui.screens.MainMenuScreen;

/**
 * Wrapper for LibGdx Game class
 */
public class ChessGame extends Game {
	private AssetManager assetManager;
	private SpriteBatch batch;
	ShaderProgram program;
	FrameBuffer paperBuffer;
	private Texture paper;

	@Override
	public void create () {
		paper = new Texture(Gdx.files.internal("paper.png"));
		assetManager = new AssetManager();

		batch = new SpriteBatch();
		program = new ShaderProgram(Gdx.files.internal("shaders/id.vert"), Gdx.files.internal("shaders/paper.frag"));
		paperBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, paper.getWidth(), paper.getHeight(), false);

		setScreen(new MainMenuScreen(this, assetManager));
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
		batch.setShader(program);
		paperBuffer.begin();
		batch.begin();
		program.setUniformf("u_offset", new Vector2((float)Math.random() * 100.f, (float)Math.random() * 100.f));
		batch.draw(paper, 0, 0, paper.getWidth(), paper.getHeight());
		batch.end();
		paperBuffer.end();
		batch.setShader(null);
	}

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(paperBuffer.getColorBufferTexture(), 0, 0);
		batch.end();
		super.render();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
