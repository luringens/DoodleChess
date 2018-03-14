package com.syntax_highlighters.chess;

import com.badlogic.gdx.Game;
import com.syntax_highlighters.chess.screens.MainScreen;

/**
 * Wrapper for LibGdx Game class
 */
public class ChessGame extends Game {

	@Override
	public void create () {
		setScreen(new MainScreen());
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
