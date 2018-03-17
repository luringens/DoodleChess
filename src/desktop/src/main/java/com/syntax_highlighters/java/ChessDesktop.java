package com.syntax_highlighters.java;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.syntax_highlighters.chess.ChessGame;


public class ChessDesktop {
	public static void main (String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setHdpiMode(Lwjgl3ApplicationConfiguration.HdpiMode.Pixels);
		config.setWindowedMode(800, 800);
		new Lwjgl3Application(new ChessGame(), config);
	}
}
