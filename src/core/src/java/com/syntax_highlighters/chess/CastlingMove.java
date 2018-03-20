package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.ChessPieceKing;
import com.syntax_highlighters.chess.entities.ChessPieceRook;

public class CastlingMove extends Move {
    private ChessPieceRook rook;
    private Position rookOldPos;
    private Position rookNewPos;

    /**
     * Constructor.
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

    @Override
    public void DoMove(Board b) {
        if (hasDoneMove) throw new RuntimeException("Move has already been done.");
        hasDoneMove = true;
        b.putAtPosition(newPos, piece);
        b.putAtPosition(rookNewPos, rook);
    }

    @Override
    public void UndoMove(Board b) {
        if (!hasDoneMove) throw new RuntimeException("Can not undo a move that has not been done");
        hasDoneMove = false;
        b.putAtPosition(oldPos, piece);
        b.putAtPosition(rookOldPos, rook);
    }
}
