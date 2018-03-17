package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ChessPieceKnight extends AbstractChessPiece {
    public ChessPieceKnight(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    @Override
    protected int[] getPositionScoreTable() {
        return new int[]{
            -50, -40, -30, -30, -30, -30, -40, -50,
            -40, -20,   0,   0,   0,   0, -20, -40,
            -30,   0,  10,  15,  15,  10,   0, -30,
            -30,   5,  15,  20,  20,  15,   5, -30,
            -30,   0,  15,  20,  20,  15,   0, -30,
            -30,   5,  10,  15,  15,  10,   5, -30,
            -40, -20,   0,   5,   5,   0, -20, -40,
            -50, -40, -30, -30, -30, -30, -40, -50,
        };
    }

    @Override
    public List<Move> allPossibleMoves(Board board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        int x = getPosition().getX();
        int y = getPosition().getY();

        checkMove(board, possibleMoves, new Position(x+1,y+2));
        checkMove(board, possibleMoves, new Position(x+1,y-2));
        checkMove(board, possibleMoves, new Position(x-1,y+2));
        checkMove(board, possibleMoves, new Position(x-1,y-2));
        checkMove(board, possibleMoves, new Position(x+2,y+1));
        checkMove(board, possibleMoves, new Position(x+2,y-1));
        checkMove(board, possibleMoves, new Position(x-2,y+1));
        checkMove(board, possibleMoves, new Position(x-2,y-1));
        return possibleMoves;


        //throw new NotImplementedException();
    }
    //Checks the move
    public void checkMove(Board board, ArrayList<Move> possibleMoves, Position pos){
        if(board !=null && board.isOnBoard(pos)){
                if(board.getAtPosition(pos) ==null || board.isEnemy(this,pos)){ //kan også bruke isFriendly-metoden
                    possibleMoves.add(new Move(this.getPosition(), pos, this));
                }

        }
    }

    @Override
    public IChessPiece copy() {
        return new ChessPieceKnight(this.getPosition(), this.isWhite());
    }

    @Override
    public int getPieceScore() {
        return 30;
    }

    @Override
    public String getAssetName() {
        return isWhite() ? "knight_white.png" : "knight_black.png";
    }
}
