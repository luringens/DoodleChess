package com.syntax_highlighters.chess;

import java.util.Objects;

import com.syntax_highlighters.chess.entities.IChessPiece;

/**
 * Promotion move includes info about pawns moving to the 1st or eigth rank, and
 * what they're promoted to.
 */
public class PromotionMove extends Move {
    private IChessPiece promoteTo;
    private IChessPiece oldPiece;

    /**
     * IMPORTANT: This must be changed on every release of the class
     * in order to prevent cross-version serialization.
     */
    private static final long serialVersionUID = 1;

    /**
     * Create a PromotionMove from the given position to the new position, which
     * promotes the pawn to the piece specified.
     *
     * Can in theory be applied to any kind of piece and promote to any kind of
     * piece.
     *
     * @param oldPos The old position
     * @param newPos The new position
     * @param board The current board state
     * @param promoteToPiece The piece the pawn should be promoted to
     */
    public PromotionMove(Position oldPos, Position newPos, Board board, IChessPiece promoteToPiece) {
        super(oldPos, newPos, board);
        promoteTo = promoteToPiece;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void DoMove(Board b) {
        oldPiece = getPiece(b);
        super.DoMove(b);
        b.removePiece(oldPiece);
        b.putAtPosition(getPosition(), promoteTo);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void UndoMove(Board b) {
        b.removePiece(promoteTo);
        b.putAtPosition(getPosition(), oldPiece);
        super.UndoMove(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof PromotionMove)) return false;
        PromotionMove o = (PromotionMove) other;
        return super.equals(o)
            && Objects.equals(o.promoteTo, this.promoteTo)
            && Objects.equals(o.oldPiece, this.oldPiece);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.promoteTo, this.oldPiece);
    }
}
