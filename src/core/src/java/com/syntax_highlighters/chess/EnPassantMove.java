package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.IChessPiece;

/**
 * Stores info about a move in the game.
 *
 * Records which player moved, where the old position was, where the new
 * position is, and which piece was moved. Can be used to represent both past
 * moves and future potential moves.
 *
 * This class should be immutable.
 */
public class EnPassantMove extends Move {
    protected IChessPiece piece;
    private IChessPiece passantTakesPiece;

    /**
     * Constructor.
     *
     * @param oldPos The old position of the piece
     * @param newPos The new position of the piece
     * @param piece The piece that was moved
     */
    public EnPassantMove(Position oldPos, Position newPos, IChessPiece piece, IChessPiece takes) {
        super(oldPos, newPos, piece);
        this.passantTakesPiece = takes;
    }

    @Override
    public void DoMove(Board b) {
        super.DoMove(b);
        b.removePiece(passantTakesPiece);
    }

    @Override
    public void UndoMove(Board b) {
        super.DoMove(b);
        b.putAtPosition(passantTakesPiece.getPosition(), passantTakesPiece);
    }
}
