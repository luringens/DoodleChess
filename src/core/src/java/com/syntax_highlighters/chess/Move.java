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
public class Move {
    boolean hasDoneMove = false;
    protected IChessPiece piece;
    Position oldPos;
    Position newPos;
    private boolean hadMoved;
    private IChessPiece tookPiece = null;

    /**
     * For inheritance only!
     */
    protected Move() {}
    
    /**
     * Constructor.
     *
     * @param oldPos The old position of the piece
     * @param newPos The new position of the piece
     * @param piece The piece that was moved
     */
    public Move(Position oldPos, Position newPos, IChessPiece piece) {
        this.oldPos = oldPos;
        this.newPos = newPos;
        this.piece = piece;
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
     * @return The piece
     */
    public IChessPiece getPiece() {
        return this.piece;
    }

    /**
     * Get the color of the moved piece.
     *
     * @return Whether the moved piece was white
     */
    public boolean isWhite() {
        return this.piece.isWhite();
    }

    public void DoMove(Board b) {
        if (hasDoneMove) throw new RuntimeException("Move has already been done.");
        hasDoneMove = true;
        tookPiece = b.getAtPosition(newPos);
        piece.setHasMoved(true);
        b.putAtPosition(newPos, piece);
    }

    public void UndoMove(Board b) {
        if (!hasDoneMove) throw new RuntimeException("Can not undo a move that has not been done");
        hasDoneMove = false;
        b.putAtPosition(oldPos, piece);
        piece.setHasMoved(hadMoved);
        if (tookPiece != null) {
            b.putAtPosition(tookPiece.getPosition(), tookPiece);
        }
    }
}
