package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.IChessPiece;

/**
 * Promotion move includes info about pawns moving to the 1st or eigth rank, and
 * what they're promoted to.
 */
public class PromotionMove extends Move {
    private IChessPiece promoteTo;
    private IChessPiece oldPiece;

    private PromotionMove() {}

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
     * Get the move in long algebraic notation.
     *
     * Disambiguate to which kind of piece the player promoted using the letter
     * representing said kind of piece on the end of the string.
     *
     * @return The move in long algebraic notation for chess moves
     */
    @Override
    public String toString() {
        return super.toString() + promoteTo.toChessNotation();
    }

    /**
     * {@inheritDoc}
     */
    public Move copy() {
        PromotionMove m = new PromotionMove();
        m.oldPos = oldPos;
        m.newPos = newPos;
        m.hadMoved = hadMoved;
        m.pieceString = pieceString;
        m.tookPiece = tookPiece;
        m.hasDoneMove = hasDoneMove;
        m.promoteTo = promoteTo;
        m.oldPiece = oldPiece;
        return m;
    }
}
