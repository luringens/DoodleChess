package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class ChessPiecePawn extends AbstractChessPiece {
    public ChessPiecePawn(Position pos, boolean isWhite) {
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
        ArrayList<Move> possibleMoves = new ArrayList<>();
        int x = getPosition().getX();
        int y = getPosition().getY();

        //Black pieaces move "down", white up on the x axis
        int pieceColor;
        if(this.isWhite()){pieceColor =1;}
        else{pieceColor=-1;}
       checkMove(board, possibleMoves, new Position(x,y+(pieceColor)));
        takeEnemiesMove(board, possibleMoves, new Position(x+1,y+(pieceColor)));
        takeEnemiesMove(board, possibleMoves, new Position(x-1,y+(pieceColor)));
        if(!hasMoved()) { checkMove(board, possibleMoves, new Position(x, y + (2*pieceColor))); }
        enPassantCheck(board, possibleMoves, new Position(x+1,y),pieceColor);
        enPassantCheck(board, possibleMoves, new Position(x-1,y),pieceColor);

        return possibleMoves;
            //En passant
        //throw new NotImplementedException();
    }
    //Checks the move
    private void checkMove(Board board, ArrayList<Move> possibleMoves, Position pos){
        if(board.isOnBoard(pos)){
            if(board.getAtPosition(pos).isWhite() != this.isWhite()){
                possibleMoves.add(new Move(this.getPosition(), pos, this));
            }

        }
    }
    //check enPassant
    private  void enPassantCheck(Board board, ArrayList<Move> possibleMoves, Position pos, int pieceColor){
        if(board.isOnBoard(pos)){
           //  pawns have to be marked as false after every round (hasMoved)
            //Checks if the position to your left and right is occupied by an enemy
            if( board.isEnemy(this,pos) && ((ChessPiecePawn) board.getAtPosition(pos)).hasMoved() ){
                possibleMoves.add(new Move(this.getPosition(), new Position(pos.getX(),pos.getY()+(pieceColor)), this));
            }

        }
    }
    private  void takeEnemiesMove(Board board, ArrayList<Move> possibleMoves, Position pos){
        if(board.isOnBoard(pos)){
            if( board.isEnemy(this,pos)){
                possibleMoves.add(new Move(this.getPosition(), pos, this));
            }

        }
    }





    @Override
    public IChessPiece copy() {
        return new ChessPiecePawn(this.getPosition(), this.isWhite());
    }
    
    @Override
    public int getScore() {
        throw new NotImplementedException();
    }
    
    @Override
    public String getAssetName() {
        throw new NotImplementedException();
    }
}
