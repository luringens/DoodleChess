package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Rook chess piece.
 */
public class ChessPieceRook extends AbstractChessPiece {
    /**
     * Construct a rook at the given position with the given color.
     *
     * @param pos The position to place the rook at
     * @param isWhite Whether or not this bishop is white
     */
    public ChessPieceRook(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int[] getPositionScoreTable() {
        return new int[]{
             0,  0,  0,  0,  0,  0,  0,  0,
             5, 10, 10, 10, 10, 10, 10,  5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
             0,  0,  0,  5,  5,  0,  0,  0
        };
    }

    /**
     * Return all possible moves the rook can make.
     *
     * A rook may move any number of steps horizontally or vertically, as long
     * as the path is not blocked by another piece. If the path is blocked by an
     * enemy piece, the rook may capture that piece and go no further.
     *
     * An unmoved rook may castle with an unmoved king.
     *
     * @see ChessPieceKing
     * @param board The current state of the board
     *
     * @return A List of all the possible moves the piece can make
     */
    @Override
    public List<Move> allPossibleMoves(Board board) {
        List<Move> n = movesInDirection(0, 1, board);
        List<Move> e = movesInDirection(1, 0, board);
        List<Move> s = movesInDirection(0, -1, board);
        List<Move> w = movesInDirection(-1, 0, board);
        return Stream.of(n, e, s, w)
                .flatMap(Collection::stream)
                 .filter(m -> board.moveDoesntPutKingInCheck(m, isWhite))
                .collect(Collectors.toList());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IChessPiece copy() {
        return new ChessPieceRook(this.getPosition().copy(), this.isWhite());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPieceScore() {
        return 500;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAssetName() {
        return isWhite() ? "rook_white.png" : "rook_black.png";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean threatens(Position p, Board b) {
        return threatenDirection(0, 1, p, b)
                || threatenDirection(0, -1, p, b)
                || threatenDirection(1, 0, p, b)
                || threatenDirection(-1, 0, p, b);
    }
}
