package com.syntax_highlighters.chess;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.screens.MainScreen;

/**
 * Wrapper for LibGdx Game class
 */
public class ChessGame extends Game {
	private AssetManager assetManager;

	@Override
	public void create () {
		assetManager = new AssetManager();
		AssetLoader.LoadAssets(assetManager);

		// TODO: splash screen
		assetManager.finishLoading();
		setScreen(new MainScreen(assetManager));
	}

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void render () {
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
