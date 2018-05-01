package com.syntax_highlighters.chess;

import java.util.Objects;

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
    private final Position passantTakesPos;
    private IChessPiece passantTakesPiece = null;

    private EnPassantMove(Position passantTakesPos) {
        this.passantTakesPos = passantTakesPos;
    }

    /**
     * IMPORTANT: This must be changed on every release of the class
     * in order to prevent cross-version serialization.
     */
    private static final long serialVersionUID = 1;

    /**
     * Constructor.
     *
     * @param oldPos The old position of the pawn
     * @param newPos The new position of the pawn
     * @param board The board to work on
     * @param takes The pawn this pawn captures
     */
    public EnPassantMove(Position oldPos, Position newPos, Board board, IChessPiece takes) {
        super(oldPos, newPos, board);
        this.passantTakesPos = takes.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void DoMove(Board b) {
        super.DoMove(b);
        setTakenPiece(b.getAtPosition(passantTakesPos));
        b.removePiece(getTakenPiece());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void UndoMove(Board b) {
        super.UndoMove(b);
        b.putAtPosition(passantTakesPos, getTakenPiece());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Move copy() {
        EnPassantMove m = new EnPassantMove(passantTakesPos);
        m.oldPos = oldPos;
        m.newPos = newPos;
        m.hadMoved = hadMoved;
        m.pieceString = pieceString;
        m.tookPiece = tookPiece;
        m.hasDoneMove = hasDoneMove;
        m.passantTakesPiece = passantTakesPiece;
        return m;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof EnPassantMove)) return false;
        EnPassantMove o = (EnPassantMove) other;
        return super.equals(o)
            && Objects.equals(o.passantTakesPos, this.passantTakesPos)
            && Objects.equals(o.passantTakesPiece, this.passantTakesPiece);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.passantTakesPos, this.passantTakesPiece);
    }
}
