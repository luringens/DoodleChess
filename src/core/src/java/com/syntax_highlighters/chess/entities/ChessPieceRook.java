package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class ChessPieceRook extends AbstractChessPiece {
    public ChessPieceRook(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    private boolean moved = false;

    public void setPieceToMoved(){
        this.moved = true;
    }

    public boolean hasMoved(){
        return moved;
    }
    
    @Override
    public List<Move> allPossibleMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();
        int xpos = this.getPosition().getX();
        int ypos = this.getPosition().getY();
        for(int i=1; i<8; i++){
            Position boardPos = new Position(xpos, ypos+i);
            if(board.isFriendly(this, boardPos)){
                possibleMoves.add(new Move(this.getPosition(), boardPos, this));
            }
        }
        throw new NotImplementedException();
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
