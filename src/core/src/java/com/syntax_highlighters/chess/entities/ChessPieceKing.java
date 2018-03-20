package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.CastlingMove;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;


import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
public class ChessPieceKing extends AbstractChessPiece {
    public ChessPieceKing(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int[] getPositionScoreTable() {
        return new int[]{
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -20, -30, -30, -40, -40, -30, -30, -20,
            -10, -20, -20, -20, -20, -20, -20, -10,
             20,  20,   0,   0,   0,   0,  20,  20,
             20,  30,  10,   0,   0,  10,  30,  20
        };
    }

    /**
     * Return all possible moves the king can make.
     *
     * A king can move one step in any direction, but only if it's free or
     * occupied by an enemy piece, and it doesn't put the king in a threatened
     * state.
     *
     * A king which has not previously moved can also perform castling with
     * one of the rooks, as long as the rook also hasn't moved, the path between
     * them is clear, the king is not in check, and does not pass through or end
     * up on a piece which is not in check.
     *
     * @param board The current state of the board
     *
     * @return A List of all the possible moves the piece can make
     */
    @Override 
    public List<Move> allPossibleMoves(Board board) {
        // get legal regular moves
        List<Move> possibleMoves = getPosition().neighbors().stream()
            .filter(p -> board.isOnBoard(p) &&
                    !board.isFriendly(this, p))
            .map(p -> new Move(this.getPosition(), p, this))
            .collect(Collectors.toList());

        // handle castling
        if(!this.hasMoved() && getPosition().getX() == 5){
            Position pos = this.getPosition();
            
            // Don't castle if in check
            if (!isThreatened(board)) {
                if (canCastle(board, pos, pos.west(4), p -> p.west(1))){
                    ChessPieceRook r = (ChessPieceRook)board.getAtPosition(pos.west(4));
                    possibleMoves.add(new CastlingMove(this, r));
                }
                if (canCastle(board, pos, pos.east(3), p -> p.east(1))) {
                    ChessPieceRook r = (ChessPieceRook)board.getAtPosition(pos.east(3));
                    possibleMoves.add(new CastlingMove(this, r));
                }
            }
        }

        return possibleMoves.stream()
                .filter(m -> board.moveDoesntPutKingInCheck(m, isWhite))
                .collect(Collectors.toList());
    }

    // I might want to make this either an interface in
    // com.syntax_highlighters.chess or a static interface in Position.
    private interface PositionManipulator {
        Position transform(Position pos);
    }
    
    // checks that given the board state, the list of threatened positions, the
    // starting position, the target position, and the direction, the king can
    // castle
    /**
     * Check if the king can castle in the specified direction.
     *
     * @param board The current board state
     * @param pos The starting position
     * @param target The ending position (rook should be there)
     * @param direction The direction to check in (ends at target)
     *
     * @return true if castling is possible in the given direction, false
     * otherwise
     */
    private boolean canCastle(Board board, Position pos, Position target, PositionManipulator direction) {

        // Check the squares between the king and rook (assuming the rook is in
        // the direction specified by direction) for whether they're occupied,
        // threatened, or off the board (actually an error in programming,
        // should maybe throw exception instead)
        do {
            pos = direction.transform(pos);
            Position finalPos = pos; // hax to make the compiler shut up
            if (!board.isOnBoard(pos)
                    || board.isOccupied(pos)
                    || board.getAllPieces().stream()
                            .filter(p -> p.isWhite() != isWhite)
                            .anyMatch(p -> p.threatens(finalPos, board))                    )
                return false;
        } while (!direction.transform(pos).equals(target));

        // Check that it's possible to castle with this piece
        // Conditions:
        // - There is a piece at the position
        // - The piece is the same color as the king
        // - The piece is a rook
        // - The rook hasn't moved
        IChessPiece piece = board.getAtPosition(direction.transform(pos));
        return piece != null && piece.isWhite() == isWhite() &&
               piece instanceof ChessPieceRook && !piece.hasMoved();
    }

    /**
     * Check whether the king is threatened by an enemy piece.
     *
     * @param board The board
     * @return true if a piece threatens the king, false otherwise
     */
    public boolean isThreatened(Board board) {
        return board.getAllPieces().stream()
            .filter(p -> p.isWhite() != isWhite())
            .anyMatch(p -> p.threatens(getPosition(), board));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IChessPiece copy() {
        return new ChessPieceKing(this.getPosition(), this.isWhite());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPieceScore() {
        return 20000;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAssetName() {
        return isWhite() ? "king_white.png" : "king_black.png";
    }

    @Override
    public boolean threatens(Position p, Board b) {
        return position.neighbors().stream().anyMatch(p::equals);
    }
}
