package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class ChessPieceBishop extends AbstractChessPiece {
    public ChessPieceBishop(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    @Override
    protected int[] getPositionScoreTable() {
        return new int[]{
            -20, -10, -10, -10, -10, -10, -10, -20,
            -10,   0,   0,   0,   0,   0,   0, -10,
            -10,   0,   5,  10,  10,   5,   0, -10,
            -10,   5,   5,  10,  10,   5,   5, -10,
            -10,   0,  10,  10,  10,  10,   0, -10,
            -10,  10,  10,  10,  10,  10,  10, -10,
            -10,   5,   0,   0,   0,   0,   5, -10,
            -20, -10, -10, -10, -10, -10, -10, -20,
        };
    }

    @Override
    public List<Move> allPossibleMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();
        int x = this.getPosition().getX();
        int y = this.getPosition().getY();
        Position lastPos = this.getPosition();

        Position[] possiblePositions = new Position[]{
            new Position(x + 2, y + 1),
            new Position(x + 2, y - 1),
            new Position(x - 2, y + 1),
            new Position(x - 2, y - 1),
            new Position(x + 1, y + 2),
            new Position(x + 1, y - 2),
            new Position(x - 1, y + 2),
            new Position(x - 1, y - 2)
        };

        for (Position pos : possiblePositions) {
            if (board.isOnBoard(pos) && !board.isFriendly(this, pos))
            possibleMoves.add(new Move(lastPos, pos, this));
        }

        return possibleMoves;
    }

    @Override
    public IChessPiece copy() {
        return new ChessPieceBishop(this.getPosition(), this.isWhite());
    }

    @Override
    public int getPieceScore() {
        return 30;
    }

    @Override
    public String getAssetName() {
        return isWhite() ? "bishop_white.png" : "bishop_black.png";
    }
}
