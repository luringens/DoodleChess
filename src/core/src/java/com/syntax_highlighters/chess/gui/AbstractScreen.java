package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.syntax_highlighters.chess.ChessGame;

public abstract class AbstractScreen implements Screen {
    private ChessGame game;

    protected AbstractScreen(ChessGame game)
    {
        this.game = game;
    }

    protected Game getGame()
    {
        return game;
    }
}
