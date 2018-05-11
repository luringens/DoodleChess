package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.game.AbstractGame;
import com.syntax_highlighters.chess.move.Move;

/**
 * Interface allowing a class to get a move based on a game in a synchronous
 * manner.
 *
 * Presumably assumes that the player *can* make a legal move.
 */
public interface IBlockingPlayer {
    /**
     * Retrieve a legal move given the game state.
     *
     * @param game The game state
     * @return A legal Move the player can make given the game state
     */
    Move GetMove(AbstractGame game);
}
