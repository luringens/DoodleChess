package com.syntax_highlighters.chess.gui.screens;

import com.syntax_highlighters.chess.NetworkChessGame;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;

/**
 * Game main screen.
 */
public class NetworkGameScreen extends GameScreen {
    /**
     * Constructor.
     * <p>
     * We have put the AI on a separate thread to stop the window from becoming unresponsive while the AI is thinking.
     *  @param chessGame current ChessGame
     * @param attrib1   Attributes for player 1 (account info, AI difficulty,
     *                  piece color)
     * @param attrib2   Attributes for player 2 (account info, AI difficulty,
     * @param randomBoard Whether or not to generate a random board or a regular one.
     */
    public NetworkGameScreen(LibgdxChessGame chessGame, NetworkChessGame game) {
        super(chessGame, game);
    }

    /**
     * Render the screen
     * <p>
     * Due to how the AI works do we have to draw it to an offscreen buffer before we let it think.
     * This is so that we can safely draw the game while the ai is thinking.
     * We also wait til the next draw call to resize the buffer the ai is thinking.
     *
     * @param delta time passed since last frame, in seconds
     */
    @Override
    public void render(float delta) {
        super.render(delta);
    }

    /**
     * Resize event.
     * <p>
     * Used to correctly position the elements on screen and update the viewport size to support the new window size.
     *
     * @param width  new window width
     * @param height new window height
     */
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
