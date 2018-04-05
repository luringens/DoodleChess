package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that implements common chess piece functionality.
 *
 * Also contains a static method for creating chess pieces based on algebraic
 * notation (AN). Algebraic notation is generally used for describing chess
 * moves (Ne5 is, for instance, read as "knight to E5"), but they're equally
 * adequate at simply describing a piece of a given kind at a given position.
 */
public abstract class AbstractChessPiece implements IChessPiece {
    protected final boolean isWhite;
    protected Position position;
    protected boolean hasMoved = false;

    /**
     * Chess piece at given position with given color.
     *
     * @param isWhite Whether or not the piece is white
     * @param pos The position the piece is created at
     */
    public AbstractChessPiece(Position pos, boolean isWhite) {
        this.isWhite = isWhite;
        this.position = pos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Position getPosition() {
        return this.position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canMoveTo(Position pos, Board board) {
        return getMoveTo(pos, board) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Move getMoveTo(Position pos, Board board) {
        List<Move> moves = allPossibleMoves(board).stream()
            .filter(p -> p.getPosition().equals(pos))
            .collect(Collectors.toList());
        if (moves.size() == 0) return null;
        return moves.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(Position pos) {
        this.position = pos;
    }
    
    /**
     * Convenicence method for creation of IChessPiece.
     *
     * Takes the notation of a piece using AN (for ex. "KA5") and whether the
     * piece is black or white, and creates a piece at the correct position of
     * the correct type.
     *
     * K = king
     * Q = queen
     * B = bishop
     * N = knight
     * R = rook
     * P = pawn
     *
     * The purpose of this method is to simplify batch creation of different
     * pieces by allowing a loop over a collection of strings.
     *
     * @param piece The string defining the piece and position
     * @param isWhite Whether the piece is white or not
     *
     * @return The correct type of piece at the correct position
     *
     * @throws IllegalArgumentException if the piece notation does not conform
     * to the expected format
     */
    public static IChessPiece fromChessNotation(String piece, boolean isWhite) {
        assert piece.length() == 3; // of the form [KQNBRP][A-Ha-h][1-8]

        // assure that the first part is referencing a valid type of piece
        assert "KQNBRP".contains(piece.substring(0,1));

        Position pos = Position.fromChessNotation(piece.substring(1, piece.length()));
        switch(piece.charAt(0)) {
            case 'K':
                return new ChessPieceKing(pos, isWhite);
            case 'Q':
                return new ChessPieceQueen(pos, isWhite);
            case 'B':
                return new ChessPieceBishop(pos, isWhite);
            case 'R':
                return new ChessPieceRook(pos, isWhite);
            case 'N':
                return new ChessPieceKnight(pos, isWhite);
            case 'P':
                return new ChessPiecePawn(pos, isWhite);
            default:
                throw new IllegalArgumentException("Invalid piece notation: " + piece);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getPositionalScore() {
        Position p = getPosition();
        int x = p.getX();
        int y = p.getY();

        // Score board is based on starting on the bottom.
        // Reverse for black.
        if (isWhite()) {
            y = Board.BOARD_HEIGHT - y + 1;
        }

        // Adjust for 0-based indexing.
        x -= 1;
        y -= 1;

        return getPieceScore() + getPositionScoreTable()[x + y * Board.BOARD_WIDTH];
    }

    /**
     * Get the table for positional scores.
     *
     * Used in getPositionalScore. Subclasses will implement.
     *
     * @return The table used for computing positional scores
     */
    protected abstract int[] getPositionScoreTable();

    /** Creates a list of possible moves in a direction.
     *
     * Stops when reaching another piece, or when the end of the board is
     * reached.
     *
     * @param dx 0 for no horizontal movement, -1 or 1 for movement.
     * @param dy 0 for no vertical movement, -1 or 1 for movement.
     * @param board The board to look at.
     * @return A list containing all possible moves in a direction
     */
    protected List<Move> movesInDirection(int dx, int dy, Board board) {
        ArrayList<Move> moves = new ArrayList<>();
        Position nextPos = new Position(position.getX() + dx, position.getY() + dy);

        while (board.isOnBoard(nextPos)) {
            if (board.isFriendly(this, nextPos)) break;
            if (board.isEnemy(this, nextPos)) {
                moves.add(new Move(this.position, nextPos, board));
                break;
            }
            moves.add(new Move(this.position, nextPos, board));
            nextPos = new Position(nextPos.getX() + dx, nextPos.getY() + dy);
        }

        return moves;
    }

    /**
     * Helper method: determine whether the piece, if moving in a certain
     * direction, will end up threatening the target position.
     *
     * Stops when reaching another piece, when the target position is reached,
     * or when the edge of the board is reached.
     *
     * @param dx 0 for no horizontal movement, -1 or 1 for movement
     * @param dy 0 for no vertical movement, -1 or 1 for movement
     * @param board The board to look at
     * @return true if this piece threatens the target position, false otherwise
     */
    protected boolean threatenDirection(int dx, int dy, Position target, Board board) {
        Position nextPos = new Position(position.getX() + dx, position.getY() + dy);
        while (board.isOnBoard(nextPos)) {
            if (board.isFriendly(this, nextPos)) return false;
            if (board.isEnemy(this, nextPos))
                return nextPos.equals(target);
            if (target.equals(nextPos)) return true;
            nextPos = new Position(nextPos.getX() + dx, nextPos.getY() + dy);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasMoved() {
        return this.hasMoved;
    }

    /**
     * {@inheritDoc}
     */
    public void setHasMoved(boolean b) {
        this.hasMoved = b;
    }
}
