package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ChessPieceKnight extends AbstractChessPiece {
    private static final int PIECE_SCORE = 50; 
    private static final int[] POS_SCORE = new int[]{
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -30,  5, 10, 15, 15, 10,  5,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50,
    };

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
                if(board.getAtPosition(pos).isWhite() != this.isWhite()){ //kan også bruke isFriendly-metoden
                    possibleMoves.add(new Move(this.getPosition(), pos, this));
                }

        }
    }

    @Override
    public IChessPiece copy() {
        return new ChessPieceKnight(this.getPosition(), this.isWhite());
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
