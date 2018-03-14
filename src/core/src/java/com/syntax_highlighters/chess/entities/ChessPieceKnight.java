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
        if(board.isOnBoard(pos)){
                if(board.getAtPosition(pos).isWhite() != this.isWhite()){
                    possibleMoves.add(new Move(this.getPosition(), pos, this));
                }

        }
    }

    @Override
    public IChessPiece copy() {
        throw new NotImplementedException();
    }

    @Override
    public int getScore() {
        throw new NotImplementedException();
    }
}
