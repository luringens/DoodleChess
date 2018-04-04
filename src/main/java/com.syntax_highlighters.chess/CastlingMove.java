package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.ChessPieceKing;
import com.syntax_highlighters.chess.entities.ChessPieceRook;

/**
 * Special move which places the king behind the rook.
 *
 * Only legal if neither the king nor the rook has moved, the path between them
 * is clear, the king is not in check, and the king doesn't pass over or end up
 * at a position threatened by an enemy piece.
 *
 * The king moves two steps towards the rook, and the rook is then placed on the
 * other side of the king.
 *
 * This class does not perform any checks concerning whether castling is legal
 * between the king and the rook.
 */
public class CastlingMove extends Move {
    private final ChessPieceRook rook;
    private final Position rookOldPos;
    private final Position rookNewPos;

    /**
     * Construct a castling move between the given king and rook.
     *
     * @param king The king to castle with.
     * @param rook The rook to castle with.
     */
    public CastlingMove(ChessPieceKing king, ChessPieceRook rook) {
        this.oldPos = king.getPosition();
        this.rookOldPos = rook.getPosition();
        this.rook = rook;
        this.piece = king;

        if (rook.getPosition().getX() < king.getPosition().getX()) {
            this.newPos = king.getPosition().west(2);
            this.rookNewPos = newPos.east(1);
        }
        else {
            this.newPos = king.getPosition().east(2);
            this.rookNewPos = newPos.west(1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void DoMove(Board b) {
        if (hasDoneMove) throw new RuntimeException("Move has already been done.");
        hasDoneMove = true;
        b.putAtPosition(newPos, piece);
        b.putAtPosition(rookNewPos, rook);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void UndoMove(Board b) {
        if (!hasDoneMove) throw new RuntimeException("Can not undo a move that has not been done");
        hasDoneMove = false;
        b.putAtPosition(oldPos, piece);
        b.putAtPosition(rookOldPos, rook);
    }
}
