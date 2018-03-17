package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class ChessPiecePawn extends AbstractChessPiece {
    public ChessPiecePawn (Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    @Override
    protected int[] getPositionScoreTable() {
        return new int[]{
             0,  0,   0,   0,   0,   0,  0,  0,
            50, 50,  50,  50,  50,  50, 50, 50,
            10, 10,  20,  30,  30,  20, 10, 10,
             5,  5,  10,  25,  25,  10,  5,  5,
             0,  0,   0,  20,  20,   0,  0,  0,
             5, -5, -10,   0,   0, -10, -5,  5,
             5, 10,  10, -20, -20,  10, 10,  5,
             0,  0,   0,   0,   0,   0,  0,  0
        };
    }

    private boolean moved = false;

    public void setPieceToMoved () {
        this.moved = true;
    }
    public void setPieceNotMoved () {
        this.moved = false;
    }

    public boolean hasMoved () {
        return moved;
    }




    @Override
    public List<Move> allPossibleMoves (Board board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        int x = getPosition().getX();
        int y = getPosition().getY();

        //Black pieaces move "down", white up on the x axis
        int pieceColor;
        if (this.isWhite()) {
            pieceColor = 1;
        } else {
            pieceColor = -1;
        }

        checkMove(board, possibleMoves, new Position(x, y + (pieceColor)));

        takeEnemiesMove(board, possibleMoves, new Position(x + 1, y + (pieceColor)));
        takeEnemiesMove(board, possibleMoves, new Position(x - 1, y + (pieceColor)));
        if (!this.hasMoved()) {
            checkMove2(board, possibleMoves, new Position(x, y + (2 * pieceColor)),pieceColor);
        }
        enPassantCheck(board, possibleMoves, new Position(x + 1, y), pieceColor);
        enPassantCheck(board, possibleMoves, new Position(x - 1, y), pieceColor);

        return possibleMoves;
        //En passant
        //throw new NotImplementedException();
    }

    //Checks the move
    private void checkMove (Board board, ArrayList<Move> possibleMoves, Position pos) {
        if (board != null && board.isOnBoard(pos)) {
            if (board.getAtPosition(pos) == null) {
                possibleMoves.add(new Move(this.getPosition(), pos, this));
            }

        }
    }
    private void checkMove2 (Board board, ArrayList<Move> possibleMoves, Position pos, int piececolor) {
        if (board != null && board.isOnBoard(pos)) {
            if (board.getAtPosition(pos) == null && board.getAtPosition(new Position(pos.getX(), pos.getY() + (-1 * piececolor))) == null) {
                possibleMoves.add(new Move(this.getPosition(), pos, this));
            }
        }}


        //check enPassant
    private void enPassantCheck (Board board, ArrayList<Move> possibleMoves, Position pos, int pieceColor) {
        if (board.isOnBoard(pos)) {
            //  pawns have to be marked as false after every round (hasMoved)
            //Checks if the position to your left and right is occupied by an enemy
            IChessPiece pieceAtPos = board.getAtPosition(pos);
            if (pieceAtPos != null && board.isEnemy(this, pos) &&
                    pieceAtPos instanceof ChessPiecePawn &&
                    ((ChessPiecePawn) pieceAtPos).hasMoved()) {
                possibleMoves.add(new Move(this.getPosition(), new Position(pos.getX(), pos.getY() + (pieceColor)), this));
            }

        }
    }

    private void takeEnemiesMove (Board board, ArrayList<Move> possibleMoves, Position pos) {
        if (board.isOnBoard(pos)) {
            if (board.isEnemy(this, pos)) {
                possibleMoves.add(new Move(this.getPosition(), pos, this));
            }

        }
    }


    @Override
    public IChessPiece copy () {
        return new ChessPiecePawn(this.getPosition(), this.isWhite());
    }

    @Override
    public int getPieceScore() {
        return 10;
    }

    @Override
    public String getAssetName () {
        return isWhite() ? "pawn_white.png" : "pawn_black.png";
    }
}
