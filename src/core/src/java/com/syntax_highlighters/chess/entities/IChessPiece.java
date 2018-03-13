package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.Move;

import java.util.ArrayList;

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
    boolean canMove(Position pos, Board board);

    // This has been commented out due to changes in the IChessPiece API during
    // early development
//    /**
//     * Get all the moves the piece can possibly make given the current state of
//     * the board and the history of the board.
//     *
//     * NOTE: The history of the board is required knowledge because some moves
//     * are dependent on it. For instance, en passant can only be performed if
//     * the pawn it is performed on has only just moved to the appropriate rank,
//     * and castling cannot be performed if the king or the rook it's trying to
//     * castle with have moved previously. This could be handled in special
//     * cases, but at any rate, this is a more general and IMO a more elegant way
//     * to do it.
//     *
//     * @param b The current state of the board
//     * @param history The moves which were performed in the game up until this
//     * point, in chronological order
//     * @return The moves currently available to the piece
//     */
//    ArrayList<Move> getPossibleMoves(Board b, ArrayList<Move> history);
}
