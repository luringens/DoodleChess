package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
    protected final Stage stage;

    /**
     * Create a screen based on a ChessGame.
     *
     * @param game The ChessGame instance of the gui
     */
    protected AbstractScreen(ChessGame game)
    {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
    }

    /**
     * Getter for the game object.
     *
     * @return The ChessGame instance of the gui
     */
    protected Game getGame()
    {
        return game;
    }
    
    /**
     * Disposes classes that needs disposing.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
    
    /**
     * Dummy implementation of unused method show.
     */
    @Override
    public void show() {}

    /**
     * Dummy implementation of unused method hide.
     */
    @Override
    public void hide() {}

    /**
     * Dummy implementation of unused method pause.
     */
    @Override
    public void pause() {}

    /**
     * Dummy implementation of unused method resume.
     */
    @Override
    public void resume() {}
}
