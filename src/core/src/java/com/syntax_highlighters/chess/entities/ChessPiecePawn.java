package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.EnPassantMove;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessPiecePawn extends AbstractChessPiece {
    public ChessPiecePawn (Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Return all possible moves the pawn can make.
     *
     * A pawn may move two steps forward if it hasn't moved before, otherwise
     * one step forward unless its path is blocked by another piece. It may also
     * capture an enemy piece diagonally forward.
     *
     * On the occasion that the pawn is on the fifth rank (if it's white - the
     * fourth rank if it's black) and an enemy pawn uses a double step to the
     * square beside it, the pawn may move as if the pawn had only taken a
     * single step and capture the piece. This is only possible the first turn
     * after the enemy pawn has moved.
     *
     * On the occasion that a pawn reaches the far side of the board (on the
     * side of the enemy), the pawn shall be replaced with a queen.
     *
     * @param board The current state of the board
     *
     * @return A List of all the possible moves the piece can make
     */
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

        return possibleMoves.stream()
                .filter(m -> board.moveDoesntPutKingInCheck(m, isWhite))
                .collect(Collectors.toList());

    }

    /**
     * If en passant can be performed onto the given position, add the move to
     * possible moves.
     *
     * @param board The current state of the board
     * @param possibleMoves Reference to the list of possible moves
     * @param pos The position to perform en passant onto
     */
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
            
            // check that the last move was a double step
            Position newPos = lastMove.getPosition();
            Position oldPos = lastMove.getOldPosition();
            if (Math.abs(newPos.getY() - oldPos.getY()) != 2) return; // last move was a single step
            
            // en passant can be performed
            if (isWhite()) {
                possibleMoves.add(new EnPassantMove(this.getPosition(), pos.north(1), this, pieceAtPos));
            }
            else {
                possibleMoves.add(new EnPassantMove(this.getPosition(), pos.south(1), this, pieceAtPos));
            }
        }
    }

    /**
     * If it's possible to capture an enemy normally by moving onto the given
     * position, add that move to possible moves.
     *
     * @param board The current state of the board
     * @param possibleMoves Reference to the list of possible moves
     * @param pos The position to perform capture onto
     */
    private void takeEnemiesMove (Board board, ArrayList<Move> possibleMoves, Position pos) {
        if (board.isOnBoard(pos)) {
            if (board.isEnemy(this, pos)) {
                possibleMoves.add(new Move(this.getPosition(), pos, this));
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public IChessPiece copy () {
        return new ChessPiecePawn(this.getPosition(), this.isWhite());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPieceScore() {
        return 100;
    }

    /**
     * {@inheritDoc}
     */
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

    @Override
    public boolean threatens(Position p, Board b) {
        return p.equals(position.east(1).north(1)) || p.equals(position.west(1).north(1))
            // en passant
            || p.equals(position.east(1)) && b.getAtPosition(position.east(1).north(1)) == null
            || p.equals(position.west(1)) && b.getAtPosition(position.west(1).north(1)) == null;
    }
}
