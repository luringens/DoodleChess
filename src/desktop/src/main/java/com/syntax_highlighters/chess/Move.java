package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.IPlayer;
import com.syntax_highlighters.chess.entities.IChessPiece;

/**
 * Stores info about a move in the game.
 *
 * Records which player moved, where the old position was, where the new
 * position is, and which piece was moved.
 *
 * This class should be immutable.
 */
public class Move {
    private IChessPiece piece;
    private Position oldPos;
    private Position newPos;
    
    /**
     * Constructor.
     *
     * NOTE: Information about which player the move belonged to is stored in
     * the IChessPiece class (?)
     *
     * @param oldPos The old position of the piece
     * @param newPos The new position of the piece
     * @param piece The piece that was moved
     */
    public Move(Position oldPos, Position newPos, IChessPiece piece) {
        this.oldPos = oldPos;
        this.newPos = newPos;
        this.piece = piece;
    }

    /**
     * Get the new position of the piece.
     *
     * @return The position the piece ends up on after the move
     */
    public Position getPosition() {
        return this.newPos;
    }

    /**
     * Get the old position of the piece.
     *
     * @return The position the piece was on before the move was made
     */
    public Position getOldPosition() {
        return this.oldPos;
    }

    /**
     * Get the piece that was moved.
     *
     * @return The piece
     */
    public IChessPiece getPiece() {
        return this.piece;
    }

    /**
     * Get the player who made the move.
     *
     * @return The player
     */
    public IPlayer getPlayer() {
        return this.piece.getPlayer();
    }
}
