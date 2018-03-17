package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

import java.util.List;

public abstract class AbstractChessPiece implements IChessPiece {
    protected boolean isWhite;
    protected Position position;

    /**
     * Constructor.
     *
     * @param isWhite Whether or not the piece is white
     * @param pos The position the piece is created at
     */
    public AbstractChessPiece(Position pos, boolean isWhite) {
        this.isWhite = isWhite;
        this.position = pos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Position getPosition() {
        return this.position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canMoveTo(Position pos, Board board) {
        return allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(pos));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(Position pos) {
        this.position = pos;
    }
    
    /**
     * Convenicence method for creation of IChessPiece.
     *
     * Takes the notation of a piece using AN (for ex. "KA5") and whether the
     * piece is black or white, and creates a piece at the correct position of
     * the correct type.
     *
     * K = king
     * Q = queen
     * B = bishop
     * N = knight
     * R = rook
     * P = pawn
     *
     * @param piece The string defining the piece and position
     * @param isWhite Whether the piece is white or not
     *
     * @return The correct type of piece at the correct position
     */
    public static IChessPiece fromChessNotation(String piece, boolean isWhite) {
        assert piece.length() == 3; // of the form [KQNBRP][A-Ha-h][1-8]

        // assure that the first part is referencing a valid type of piece
        assert "KQNBRP".contains(piece.substring(0,1));

        Position pos = Position.fromChessNotation(piece.substring(1, piece.length()));
        switch(piece.charAt(0)) {
            case 'K':
                return new ChessPieceKing(pos, isWhite);
            case 'Q':
                return new ChessPieceQueen(pos, isWhite);
            case 'B':
                return new ChessPieceBishop(pos, isWhite);
            case 'R':
                return new ChessPieceRook(pos, isWhite);
            case 'N':
                return new ChessPieceKnight(pos, isWhite);
            case 'P':
                return new ChessPiecePawn(pos, isWhite);
            default:
                throw new IllegalArgumentException("Invalid piece notation: " + piece);
        }
    }

    public int getPositionalScore() {
        Position p = getPosition();
        int x = p.getX();
        int y = p.getY();

        // Score board is based on starting on the bottom.
        // Reverse for black.
        if (!isWhite()) {
            x = Board.BOARD_WIDTH - x;
            y = Board.BOARD_HEIGHT - y;
        }

        // Adjust for 0-based indexing.
        x -= 1;
        y -= 1;

        return getPieceScore() + getPositionScoreTable()[x + y * Board.BOARD_WIDTH];
    }

    protected abstract int[] getPositionScoreTable();
}
