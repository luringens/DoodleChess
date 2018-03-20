package com.syntax_highlighters.chess;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.syntax_highlighters.chess.gui.screens.MainMenuScreen;

/**
 * Wrapper for LibGdx Game class
 */
public class ChessGame extends Game {
	private AssetManager assetManager;

	@Override
	public void create () {
		assetManager = new AssetManager();
		setScreen(new MainMenuScreen(this, assetManager));
	}

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
