package com.syntax_highlighters.chess;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
//import com.syntax_highlighters.chess.ChessGame;

/**
 * Application entry point.
 *
 *
 * Creates a new application running our amazing version of chess!
 */
public class Main {
    /**
     * Program entry point.
     *
     * @param args Command line arguments (unused)
     */
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int)(800 * 1.6);
		config.height = 800;
		config.forceExit = false;
		config.title = "Doodle chess";
		config.addIcon("icon256.png", Files.FileType.Internal);
		config.addIcon("icon32.png", Files.FileType.Internal);
		config.addIcon("icon16.png", Files.FileType.Internal);
		new LwjglApplication(new ChessGame(), config);

	}
}
