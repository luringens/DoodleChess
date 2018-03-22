package com.syntax_highlighters.java;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.syntax_highlighters.chess.ChessGame;


public class ChessDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 800;
		config.title = "Doodle chess";
		config.addIcon("icon256.png", Files.FileType.Internal);
		config.addIcon("icon32.png", Files.FileType.Internal);
		config.addIcon("icon16.png", Files.FileType.Internal);
		new LwjglApplication(new ChessGame(), config);
	}
}
