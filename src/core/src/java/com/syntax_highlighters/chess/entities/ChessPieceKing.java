package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.ArrayList;
public class ChessPieceKing extends AbstractChessPiece {

    public ChessPieceKing(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    private boolean moved = false;

    public void setPieceToMoved(){
        this.moved = true;
    }

    public boolean hasMoved(){
        return moved;
    }

    private List<Position> allEnemyMoves(Board board){
        List<Position> possibleMoves = new ArrayList<>();
        for(IChessPiece a : board.getAllPieces()) {
            if (a.isWhite() != this.isWhite()) {
                Position piecePos = a.getPosition();
                if(!possibleMoves.contains(piecePos)){
                    possibleMoves.add(piecePos);
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public List<Move> allPossibleMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();
        int xpos = this.getPosition().getX();
        int ypos = this.getPosition().getY();
        //all fields surrounding king.
        for(int i = xpos-1; i<xpos+2; i++){
            for(int j = ypos-1; j<ypos+2; j++){
                Position boardPos = new Position(i,j);
                if(board.isOccupied(boardPos)){
                    continue;
                }
                if(board.getAtPosition(new Position(1, 8)) != null) {
                    if (board.getAtPosition(boardPos).isWhite() == this.isWhite()) {
                        continue;
                    }
                }
                if(allEnemyMoves(board).contains(boardPos)){
                    continue;
                }
                possibleMoves.add(new Move(this.getPosition(), boardPos,this));
            }
        }
        //rokade
        if(this.isWhite()) {
            if(board.getAtPosition(new Position(1, 8)) != null) {
                if (board.getAtPosition(new Position(1, 8)) instanceof ChessPieceRook) {
                    if (!this.hasMoved() && !((ChessPieceRook) board.getAtPosition(new Position(1, 8))).hasMoved()) {
                        possibleMoves.add(new Move(this.getPosition(), new Position(1, 7), this));
                    }
                }
                if (board.getAtPosition(new Position(1, 1)) instanceof ChessPieceRook) {
                    if (!this.hasMoved() && !((ChessPieceRook) board.getAtPosition(new Position(1, 1))).hasMoved()) {
                        possibleMoves.add(new Move(this.getPosition(), new Position(1, 3), this));
                    }
                }
            }
        }else {
            if (board.getAtPosition(new Position(1, 8)) != null) {
                if (board.getAtPosition(new Position(8, 8)) instanceof ChessPieceRook) {
                    if (!this.hasMoved() && !((ChessPieceRook) board.getAtPosition(new Position(8, 8))).hasMoved()) {
                        possibleMoves.add(new Move(this.getPosition(), new Position(8, 7), this));
                    }
                }
                if (board.getAtPosition(new Position(8, 1)) instanceof ChessPieceRook) {
                    if (!this.hasMoved() && !((ChessPieceRook) board.getAtPosition(new Position(8, 1))).hasMoved()) {
                        possibleMoves.add(new Move(this.getPosition(), new Position(8, 3), this));
                    }
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public IChessPiece copy() {
        return new ChessPieceKing(this.getPosition(), this.isWhite());
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
