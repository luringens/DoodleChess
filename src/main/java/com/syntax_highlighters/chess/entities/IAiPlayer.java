package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.AbstractGame;
import com.syntax_highlighters.chess.Move;

/**
 * Interface for AI players.
 *
 * AI players can receive a new difficulty level, or perform a move on a Board.
 */
public interface IAiPlayer {
    /**
     * Set the difficulty level of this AI player.
     *
     * @param diff The difficulty level (Easy, Medium or Hard)
     */
    void SetDifficulty(AiDifficulty diff);

    /**
     * Choose a move and perform it on the board.
     *
     * Modifies the state of the board.
     *
     * @param game The game to work with.
     */
    void PerformMove(AbstractGame game);
    
    /**
     * Choose a move and return it without performing it on the board.
     *
     * @param game The game to work with.
     * @return The suggested best move.
     */
    Move GetMove(AbstractGame game);
}

