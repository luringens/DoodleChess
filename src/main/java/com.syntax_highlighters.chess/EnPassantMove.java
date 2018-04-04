package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.IChessPiece;

/**
 * Special move: pawns can capture other pawns in a weird way under very
 * specific circumstances.
 *
 * If a pawn is on its fifth rank, and an enemy pawn uses a double step forward
 * to land on the square beside it, this pawn may strike and capture the enemy
 * pawn as if he had only taken a single step forward.
 *
 * This move can only be performed immediately after the enemy moved their pawn
 * two steps forward.
 */
public class EnPassantMove extends Move {
    private final IChessPiece passantTakesPiece;

    /**
     * Constructor.
     *
     * @param oldPos The old position of the pawn
     * @param newPos The new position of the pawn
     * @param piece The pawn that was moved
     * @param takes The pawn this pawn captures
     */
    public EnPassantMove(Position oldPos, Position newPos, IChessPiece piece, IChessPiece takes) {
        super(oldPos, newPos, piece);
        this.passantTakesPiece = takes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void DoMove(Board b) {
        super.DoMove(b);
        b.removePiece(passantTakesPiece);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void UndoMove(Board b) {
        super.UndoMove(b);
        b.putAtPosition(passantTakesPiece.getPosition(), passantTakesPiece);
    }
}
