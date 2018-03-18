package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

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

    public boolean hasMoved () {
        return moved;
    }

    @Override
    public List<Move> allPossibleMoves (Board board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        Position pos = getPosition();
        
        // one step forward
        Position f1 = this.forward(1);
        
        // check if one step forward can be performed
        if (board.isOnBoard(f1) && !board.isOccupied(f1)) {
            possibleMoves.add(new Move(pos, f1, this));
            
            // two steps forward
            Position f2 = this.forward(2);
            
            // if one step forward can be performed, check if two steps forward
            // can be performed
            if (!this.hasMoved() && board.isOnBoard(f2) && !board.isOccupied(f2)) {
                possibleMoves.add(new Move(pos, f2, this));
            }
        }

        // add move to take enemies, if possible
        takeEnemiesMove(board, possibleMoves, this.forward(1).east(1));
        takeEnemiesMove(board, possibleMoves, this.forward(1).west(1));
        
        // add move to perform en passant, if possible
        enPassantCheck(board, possibleMoves, pos.east(1));
        enPassantCheck(board, possibleMoves, pos.west(1));

        return possibleMoves;
    }

    //Checks the move
    private void checkMove (Board board, ArrayList<Move> possibleMoves, Position pos) {
        if (board != null && board.isOnBoard(pos)) {
            if (board.getAtPosition(pos) == null) {
                possibleMoves.add(new Move(this.getPosition(), pos, this));
            }
        }
    }
    
    // WHAT DOES THIS EVEN DO? - Vegard
    private void checkMove2 (Board board, ArrayList<Move> possibleMoves, Position pos, int piececolor) {
        if (board != null && board.isOnBoard(pos)) {
            if (board.getAtPosition(pos) == null && board.getAtPosition(new Position(pos.getX(), pos.getY() + (-1 * piececolor))) == null) {
                possibleMoves.add(new Move(this.getPosition(), pos, this));
            }
        }}


        //check enPassant
    private void enPassantCheck (Board board, ArrayList<Move> possibleMoves, Position pos) {
        if (board.isOnBoard(pos)) {
            // en passant can only be performed at a pawn's fifth rank
            if (isWhite() && pos.getY() != 5) return;
            if (!isWhite() && pos.getY() != 4) return; // this is black's "fifth rank"
            
            IChessPiece pieceAtPos = board.getAtPosition(pos);
            Move lastMove = board.getLastMove();
            
            if (lastMove == null) return; // pawn can't possibly have just moved there
            if (pieceAtPos == null) return; // there is no piece at that position, enemy or friend
            if (lastMove.getPiece() != pieceAtPos) return; // the piece wasn't the last moved piece
            if (!(pieceAtPos instanceof ChessPiecePawn)) return; // piece isn't pawn
            
            // en passant can be performed
            if (isWhite()) {
                possibleMoves.add(new Move(this.getPosition(), pos.north(1), this));
            }
            else {
                possibleMoves.add(new Move(this.getPosition(), pos.south(1), this));
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

    /**
     * Helper method: return the position n steps forward.
     *
     * No bounds checks. Works correctly depending on whether your are black or
     * white.
     *
     * @return The position n steps forward
     */
    private Position forward(int nSteps) {
        if (isWhite) return this.getPosition().north(nSteps);
        return this.getPosition().south(nSteps);
    }
}
