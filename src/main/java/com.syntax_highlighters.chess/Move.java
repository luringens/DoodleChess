package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.Color;
import com.syntax_highlighters.chess.entities.IChessPiece;
import com.syntax_highlighters.chess.entities.ChessPiecePawn;
import com.syntax_highlighters.chess.entities.ChessPieceQueen;

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
public class Move {
    boolean hasDoneMove = false;
    Position oldPos;
    Position newPos;
    private boolean hadMoved;
    private IChessPiece tookPiece = null;
    private boolean wasPawn = false;

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
        if (piece instanceof ChessPiecePawn && (newPos.getY() == 1 || newPos.getY() == 8)) {
            this.wasPawn = true;
            b.removePiece(piece);
            b.putAtPosition(newPos, new ChessPieceQueen(newPos, piece.getColor()));
        }
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
        if (this.wasPawn) {
            b.removePiece(getPiece(b));
            b.putAtPosition(newPos, piece);
        }
        b.putAtPosition(oldPos, piece);
        piece.setHasMoved(hadMoved);
        if (tookPiece != null) {
            b.putAtPosition(tookPiece.getPosition(), tookPiece);
        }

        hasDoneMove = false;
    }
}
