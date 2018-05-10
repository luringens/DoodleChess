package com.syntax_highlighters.chess.move;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.chesspiece.AbstractChessPiece;
import com.syntax_highlighters.chess.Color;
import com.syntax_highlighters.chess.chesspiece.IChessPiece;

/**
 * Promotion move includes info about pawns moving to the 1st or eigth rank, and
 * what they're promoted to.
 */
public class PromotionMove extends Move {
    private transient IChessPiece promoteTo = null;
    private transient IChessPiece oldPiece = null;

    // For serialization purposes:
    private String promoteToStringrep = null;
    private Color color = null;

    private PromotionMove() {}

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

        // For serialization:
        promoteToStringrep = promoteToPiece.toChessNotation();
        color = promoteToPiece.getColor();
    }

    @Override
    public List<PositionChange> getPositionChanges(Board b) {
        List<PositionChange> ret = new ArrayList<>();
        IChessPiece p = hasDoneMove ? oldPiece : b.getAtPosition(oldPos);
        ret.add(new PositionChange(p, oldPos, newPos));
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void DoMove(Board b) {
        if (promoteTo == null) {
            promoteTo = AbstractChessPiece.fromChessNotation(promoteToStringrep, color);
        }
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
            && Objects.equals(o.promoteToStringrep, this.promoteToStringrep)
            && this.color.isWhite() == o.color.isWhite();
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
        return super.toString() + promoteToStringrep;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.promoteTo, this.oldPiece);
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

    void writeObject(java.io.ObjectOutputStream oos) 
      throws java.io.IOException {
        super.writeObject(oos);
        oos.writeUTF(promoteToStringrep);
        oos.writeBoolean(color.isWhite());
    }
 
    void readObject(java.io.ObjectInputStream ois) 
      throws ClassNotFoundException, java.io.IOException {
        super.readObject(ois);
        promoteToStringrep = ois.readUTF();
        Boolean isWhite = ois.readBoolean();
        color = isWhite ? Color.WHITE : Color.BLACK;
    }
}
