package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.Color;
import com.syntax_highlighters.chess.entities.IChessPiece;

import java.io.Serializable;
import java.util.Objects;

import com.syntax_highlighters.chess.entities.ChessPiecePawn;
import com.syntax_highlighters.chess.entities.ChessPieceQueen;
import com.syntax_highlighters.chess.gui.Audio;

/**
 * Stores info about a move in the game.
 *
 * Records which player moved, where the old position was, where the new
 * position is, and which piece was moved. Can be used to represent both past
 * moves and future potential moves.
 *
 * Can perform a move without any validity checks. Stores enough information
 * about the previous board state to reset it, provided that no further state
 * changes to the board have happened since the move was applied.
 *
 * IMPORTANT CONCURRENCY NOTE:
 * Move attempts to not save any references to pieces, and as such, a move
 * generated on one board can be used on any other board, provided the board is
 * in the same state.
 * This is no longer the case once the move has been used! This is due to how the
 * move may store a reference to taken pieces in order to restore them.
 */
public class Move implements Serializable {
    boolean hasDoneMove = false;
    Position oldPos;
    Position newPos;
    private boolean hadMoved;
    private IChessPiece tookPiece = null;

    /**
     * IMPORTANT: This must be changed on every release of the class
     * in order to prevent cross-version serialization.
     */
    private static final long serialVersionUID = 1;

    /**
     * For inheritance only!
     */
    protected Move() {}
    
    /**
     * Construct a new Move from the given position to the given position using
     * the given piece.
     *
     * @param oldPos The old position of the piece
     * @param newPos The new position of the piece
     * @param board The board to get piece info from
     */
    public Move(Position oldPos, Position newPos, Board board) {
        this.oldPos = oldPos;
        this.newPos = newPos;
        IChessPiece piece = this.getPiece(board);
        this.hadMoved = piece.hasMoved();
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
     * @param board The board to get the piece from
     * @return The piece
     */
    public IChessPiece getPiece(Board board) {
        Position p = hasDoneMove ? newPos : oldPos;
        return board.getAtPosition(p);
    }

    /**
     * Get the piece first Letter
     *
     * @param board The board to get the piece from
     * @return The pieces first char
     */
    public String getPieceString(Board board) {
        Position p = hasDoneMove ? newPos : oldPos;
        return board.getAtPosition(p).toChessNotation();
    }

    /**
     * Get the color of the moved piece.
     *
     * @param board The board to get the piece from
     * @return The color of the piece
     */
    public Color getColor(Board board) {
        return this.getPiece(board).getColor();
    }

    /**
     * Apply the move to the board.
     *
     * Modifies the board state.
     *
     * @param b The current state of the board
     * @throws RuntimeException if the move has been applied before
     */
    public void DoMove(Board b) {
        if (hasDoneMove) throw new RuntimeException("Move has already been done.");
        tookPiece = b.getAtPosition(newPos);

        IChessPiece piece = this.getPiece(b);
        piece.setHasMoved(true);
        b.putAtPosition(newPos, piece);
        hasDoneMove = true;
    }

    /**
     * Revert to the old state of the board.
     *
     * Modifies the board state.
     *
     * @param b The state of the board after this move was performed
     * @throws RuntimeException if the move has not been applied before
     */
    public void UndoMove(Board b) {
        if (!hasDoneMove) throw new RuntimeException("Can not undo a move that has not been done");

        IChessPiece piece = this.getPiece(b);
        b.putAtPosition(oldPos, piece);
        piece.setHasMoved(hadMoved);
        if (tookPiece != null) {
            b.putAtPosition(tookPiece.getPosition(), tookPiece);
        }

        hasDoneMove = false;
    }

    /**
     * Custom equality method.
     *
     * Two moves are equal if all fields are equal.
     * This does not depend on board state!
     * As such, a move that moves a rook from A1 to A2 and a move that moves
     * a queen from A1 to A2 are equal.
     *
     * @param other The object to test for equality
     * @return true if the object is equal to this Position, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Move)) return false;
        Move o = (Move) other;
        return o.hadMoved == this.hadMoved
            && o.hasDoneMove == this.hasDoneMove
            && Objects.equals(oldPos, this.oldPos)
            && Objects.equals(newPos, this.newPos)
            && Objects.equals(o.tookPiece, this.tookPiece);
    }

    /**
     * Custom hashCode method.
     *
     * Use Objects.hash for simplicity. Considers the x and y coordinate.
     *
     * @return The hash of the Position
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.hadMoved, this.hasDoneMove, this.newPos, this.oldPos, this.tookPiece);
    }
}
