package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.syntax_highlighters.chess.ChessGame;

/**
 * Abstract screen implementing common functionality among different screens.
 *
 * SUGGESTION: Refactor unused methods of extending classes into this abstract
 * class. Also, the dispose method, which seems to do the same thing for all of
 * them.
 */
public abstract class AbstractScreen implements Screen {
    private final ChessGame game;

    protected AbstractScreen(ChessGame game)
    {
        this.game = game;
    }

    protected Game getGame()
    {
        return game;
    }
}
