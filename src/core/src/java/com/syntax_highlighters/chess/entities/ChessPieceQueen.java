package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class ChessPieceQueen extends AbstractChessPiece {
    public ChessPieceQueen(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    @Override
    protected int[] getPositionScoreTable() {
        return new int[]{
            -20, -10, -10, -5, -5, -10, -10, -20,
            -10,   0,   0,  0,  0,   0,   0, -10,
            -10,   0,   5,  5,  5,   5,   0, -10,
             -5,   0,   5,  5,  5,   5,   0,  -5,
              0,   0,   5,  5,  5,   5,   0,  -5,
            -10,   5,   5,  5,  5,   5,   0, -10,
            -10,   0,   5,  0,  0,   0,   0, -10,
            -20, -10, -10, -5, -5, -10, -10, -20
        };
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
        i=1;
        while (i+ypos<9) {
            lastPos = new Position(xpos,ypos+i-1);
            nextPos = new Position(xpos, ypos + i);
            if (board.isEnemy(this,lastPos) || (ypos+i > 8) || board.isFriendly(this, nextPos))
                break;
            else
                possibleMoves.add(new Move(lastPos, nextPos, this));
            i++;
        }
        i=1;
        while (i+xpos<9) {
            lastPos = new Position(xpos+i-1,ypos);
            nextPos = new Position(xpos+i, ypos);
            if (board.isEnemy(this,lastPos) || (xpos+i > 8) || board.isFriendly(this, nextPos))
                break;
            else
                possibleMoves.add(new Move(lastPos, nextPos, this));
            i++;
        }
        i=1;
        while (ypos-i>0) {
            lastPos = new Position(xpos,ypos-i+1);
            nextPos = new Position(xpos, ypos - i);
            if (board.isEnemy(this,lastPos) || (ypos-i < 1) || (board.isFriendly(this, nextPos)))
                break;
            else
                possibleMoves.add(new Move(lastPos, nextPos, this));
            i++;
        }
        i=1;
        while (xpos-i>0) {
            lastPos = new Position(xpos-i+1,ypos);
            nextPos = new Position(xpos-i, ypos);
            if (board.isEnemy(this,lastPos) || (xpos-1 < 1) || (board.isFriendly(this, nextPos)))
                break;
            else
                possibleMoves.add(new Move(lastPos, nextPos, this));
            i++;
        }
        return possibleMoves;
    }

    @Override
    public IChessPiece copy() {
        return new ChessPieceQueen(this.getPosition(), this.isWhite());
    }

    @Override
    public int getPieceScore() {
        return 90;
    }

    @Override
    public String getAssetName() {
        return isWhite() ? "queen_white.png" : "queen_black.png";
    }
}
