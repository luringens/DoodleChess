package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.CastlingMove;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;


import java.util.stream.Collectors;
import java.util.List;

/**
 * King chess piece.
 */
public class ChessPieceKing extends AbstractChessPiece {
    /**
     * Create a king at the given position with the given color.
     *
     * @param pos The position to place the king at
     * @param color The color of the piece
     */
    public ChessPieceKing(Position pos, Color color) {
        super(pos, color);
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

    private int[] endgamePositionScoreTable = new int[] {
        -50, -40, -30, -20, -20, -30, -40, -50,
        -30, -20, -10,   0,   0, -10, -20, -30,
        -30, -10,  20,  30,  30,  20, -10, -30,
        -30, -10,  30,  40,  40,  30, -10, -30,
        -30, -10,  30,  40,  40,  30, -10, -30,
        -30, -10,  20,  30,  30,  20, -10, -30,
        -30, -30,   0,   0,   0,   0, -30, -30,
        -50, -30, -30, -30, -30, -30, -30, -50
    };

    public int getEndgamePositionalScore() {
        Position p = getPosition();
        int x = p.getX();
        int y = p.getY();

        // Score board is based on starting on the bottom.
        // Reverse for black.
        if (color.isWhite()) {
            y = Board.BOARD_HEIGHT - y + 1;
        }

        // Adjust for 0-based indexing.
        x -= 1;
        y -= 1;

        return getPieceScore() + endgamePositionScoreTable[x + y * Board.BOARD_WIDTH];
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
            .map(p -> new Move(this.getPosition(), p, board))
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
                .filter(m -> board.moveDoesntPutKingInCheck(m, color))
                .collect(Collectors.toList());
    }
    
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
    private boolean canCastle(Board board, Position pos, Position target, Position.Manipulator direction) {

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
                            .filter(p -> p.getColor() != color)
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
        return piece != null && piece.getColor() == this.getColor() &&
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
            .filter(p -> p.getColor() != this.getColor())
            .anyMatch(p -> p.threatens(getPosition(), board));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IChessPiece copy() {
        return new ChessPieceKing(this.getPosition(), this.getColor());
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
        return "king_white.png";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean threatens(Position p, Board b) {
        return position.neighbors().stream().anyMatch(p::equals);
    }
}
