package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class ChessPiecePawn extends AbstractChessPiece {
    private static final int PIECE_SCORE = 50;
    private static final int[] POS_SCORE = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5, 5, 10, 25, 25, 10, 5, 5,
            0, 0, 0, 20, 20, 0, 0, 0,
            5, -5, -10, 0, 0, -10, -5, 5,
            5, 10, 10, -20, -20, 10, 10, 5,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    public ChessPiecePawn (Position pos, boolean isWhite) {
        super(pos, isWhite);
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
        if (pieceColor == 1) {
            takeEnemiesMove(board, possibleMoves, new Position(x + 1, y + (pieceColor)));
        } else {
            takeEnemiesMove(board, possibleMoves, new Position(x - 1, y + (pieceColor)));
        }
        if (!this.hasMoved()) {
            checkMove(board, possibleMoves, new Position(x, y + (2 * pieceColor)));
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

    //check enPassant
    private void enPassantCheck (Board board, ArrayList<Move> possibleMoves, Position pos, int pieceColor) {
        if (board.isOnBoard(pos)) {
            //  pawns have to be marked as false after every round (hasMoved)
            //Checks if the position to your left and right is occupied by an enemy
            if (board.getAtPosition(pos) != null && board.isEnemy(this, pos) && ((ChessPiecePawn) board.getAtPosition(pos)).hasMoved()) {
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
    public int getScore () {
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
    public String getAssetName () {
        return "bondefinal.png";
    }
}
