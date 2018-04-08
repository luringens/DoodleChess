package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Knight chess piece.
 */
public class ChessPieceKnight extends AbstractChessPiece {
    /**
     * Create a knight at the given position with the given color.
     *
     * @param pos The position to place the knight at
     * @param isWhite Whether or not this knight is white
     */
    public ChessPieceKnight(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Return all possible moves the knight can make.
     *
     * A knight may jump two steps in one direction followed by one step in an
     * orthogonal direction. The only requirements are that the resulting
     * position is on the board and that it is free or occupied by an enemy
     * piece.
     *
     * @param board The current state of the board
     *
     * @return A List of all the possible moves the piece can make
     */
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
        return possibleMoves.stream()
                .filter(m -> board.moveDoesntPutKingInCheck(m, isWhite))
                .collect(Collectors.toList());
    }

    /**
     * Add the move to possible moves if the knight can perform the move.
     * 
     * @param board The current board state
     * @param possibleMoves Reference to the list of possible moves
     * @param pos The position to check
     */
    private void checkMove(Board board, ArrayList<Move> possibleMoves, Position pos){
        if(board !=null && board.isOnBoard(pos)){
                if(board.getAtPosition(pos) ==null || board.isEnemy(this,pos)){ //kan også bruke isFriendly-metoden
                    possibleMoves.add(new Move(this.getPosition(), pos, board));
                }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IChessPiece copy() {
        return new ChessPieceKnight(this.getPosition().copy(), this.isWhite());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPieceScore() {
        return 320;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAssetName() {
        return "knight_white.png";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean threatens(Position p, Board b) {
        return Math.abs(p.getX() - position.getX()) == 1 && Math.abs(p.getY() - position.getY()) == 2
                || Math.abs(p.getX() - position.getX()) == 2 && Math.abs(p.getY() - position.getY()) == 1;
    }
}
