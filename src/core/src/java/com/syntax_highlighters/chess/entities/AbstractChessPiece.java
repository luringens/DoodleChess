package com.syntax_highlighters.chess.entities;

public abstract class AbstractChessPiece implements IChessPiece {
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
        assert piece.length == 3; // of the form [KQNBRP][A-Ha-h][1-8]

        // assure that the first part is referencing a valid type of piece
        assert "KQNBRP".contains(piece.substr(0,1));

        Position pos = piece.substr(1, piece.length());
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
}
