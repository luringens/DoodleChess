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

    /**
     * Create a new instance of the chess piece with the same color, same
     * position and same type.
     *
     * This is required because chess pieces are mutable, and as such, in order
     * to perform theoretical moves, we need to copy the chess to not affect the
     * board state.
     *
     * @return A new copy of the piece
     */
    IChessPiece copy();

    /**
     * Get the numerical value (score/weight) of a piece.
     *
     * Used to determine what moves are good in chess AI.
     *
     * @return The numerical value of the piece
     */
    int getPieceScore();

    /**
     * Get the numerical value of a piece at a given position.
     *
     * Used to determine what moves are good in chess AI.
     *
     * @return The numerical value of the piece at a given position
     */
    int getPositionalScore();

    /**
     * Set position of the piece.
     *
     * @param pos The new position
     */
    void setPosition(Position pos);

    /**
     * Get the filename of the image asset representing this piece.
     *
     * @return The filename of an image resource
     */
    String getAssetName();
}
