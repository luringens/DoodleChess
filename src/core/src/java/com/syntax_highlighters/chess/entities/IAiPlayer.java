package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;

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
     * @param board The current board state
     */
    void PerformMove(Board board);
}

