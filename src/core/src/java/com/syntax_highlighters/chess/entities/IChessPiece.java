package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.Move;

import java.util.List;

/**
 * Common interface for all chess pieces.
 *
 * This will hopefully be a flexible approach, allowing the implementation
 * details of, for instance, getting all possible moves up to the implementing
 * subclasses.
 */
public interface IChessPiece {
    /**
     * Get the color of the piece.
     *
     * @return true if the piece is white, false otherwise
     */
    boolean isWhite();
    
    /**
     * Get the position the piece is currently at.
     *
     * @return The piece's current position
     */
    Position getPosition();

    /**
     * Check whether the piece can move to the given position given the current
     * board state.
     *
     * @param pos The position to move to
     * @param board The current board state
     *
     * @return true if the piece can move to the given position, false otherwise
     */
    boolean canMoveTo(Position pos, Board board);

    /**
     * Return all possible moves the piece can make.
     *
     * @param board The current state of the board
     * 
     * @return A List of all the possible moves the piece can make
     */
    List<Move> allPossibleMoves(Board board);
}
