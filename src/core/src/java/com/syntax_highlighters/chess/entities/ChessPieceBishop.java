package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class ChessPieceBishop extends AbstractChessPiece {
    private static final int PIECE_SCORE = 50; 
    private static final int[] POS_SCORE = new int[]{
        -20,-10,-10,-10,-10,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -10,  5,  5, 10, 10,  5,  5,-10,
        -10,  0, 10, 10, 10, 10,  0,-10,
        -10, 10, 10, 10, 10, 10, 10,-10,
        -10,  5,  0,  0,  0,  0,  5,-10,
        -20,-10,-10,-10,-10,-10,-10,-20,
    };

    public ChessPieceBishop(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }
    
    @Override
    public List<Move> allPossibleMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();
        int xpos = this.getPosition().getX();
        int ypos = this.getPosition().getY();
        Position lastPos;
        Position nextPos;
        int i=1;
        while (i+ypos<9 || i+xpos<9) {
            lastPos = new Position(xpos+i-1,ypos+i-1);
            nextPos = new Position(xpos+ i, ypos + i);
            if (board.isEnemy(this,lastPos) || !board.isOnBoard(nextPos) || board.isFriendly(this, nextPos))
                break;
            else
                possibleMoves.add(new Move(lastPos, nextPos, this));
            i++;
        }
        i=1;
        while (i+ypos<9 || i+xpos<9) {
            lastPos = new Position(xpos+i-1,ypos-i+1);
            nextPos = new Position(xpos+i, ypos-i);
            if (board.isEnemy(this,lastPos) || !board.isOnBoard(nextPos) || board.isFriendly(this, nextPos))
                break;
            else
                possibleMoves.add(new Move(lastPos, nextPos, this));
            i++;
        }
        i=1;
        while (ypos-i>0 || xpos-i>0) {
            lastPos = new Position(xpos-i+1,ypos+i+1);
            nextPos = new Position(xpos-i, ypos + i);
            if (board.isEnemy(this,lastPos) || !board.isOnBoard(nextPos) || (board.isFriendly(this, nextPos)))
                break;
            else
                possibleMoves.add(new Move(lastPos, nextPos, this));
            i++;
        }
        i=1;
        while (ypos-i>0 || xpos-i>0) {
            lastPos = new Position(xpos-i+1,ypos-i+1);
            nextPos = new Position(xpos-i, ypos-i);
            if (board.isEnemy(this,lastPos) || !board.isOnBoard(nextPos) || (board.isFriendly(this, nextPos)))
                break;
            else
                possibleMoves.add(new Move(lastPos, nextPos, this));
            i++;
        }
        return possibleMoves;
    }

    @Override
    public IChessPiece copy() {
        return new ChessPieceBishop(this.getPosition(), this.isWhite());
    }

    @Override
    public int getScore() {
        Position p = getPosition();
        int x = p.getX();
        int y = p.getY();

        // Score board is based on starting on the bottom.
        // Reverse for black
        if (!isWhite()) {
            x = Board.BOARD_WIDTH - x;
            y = Board.BOARD_HEIGHT - y;
        }

        return PIECE_SCORE + POS_SCORE[p.getX() + p.getY() * Board.BOARD_WIDTH];
    }

    @Override
    public String getAssetName() {
        throw new NotImplementedException();
    }
}
