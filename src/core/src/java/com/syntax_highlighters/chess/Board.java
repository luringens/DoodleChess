package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Holds the current state of the board.
 *
 * The board state is stored in a 8x8 grid containing the pieces (null values
 * signifying empty squares).
 *
 * The white player starts at rows 1 and 2
 * The black player starts at rows 7 and 8
 */
public class Board {
    // constants, just in case
    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;

    List<IChessPiece> pieces = new ArrayList<>();

    /**
     * Create an empty board.
     */
    public Board() { }

    /**
     * Create a board from a list of pieces.
     */
    public Board(List<IChessPiece> pieces) {
        this.pieces.addAll(pieces);
    }

    /**
     * Sets up a new board with the white and black players in their starting
     * positions.
     */
    public void setupNewGame() {
        final String[] blackPieces = new String[]{
            "RA8", "NB8", "BC8", "QD8", "KE8", "BF8", "NG8", "RH8",
            "PA7", "PB7", "PC7", "PD7", "PE7", "PF7", "PG7", "PH7"
        };

        final String[] whitePieces = new String[]{
            "PA2", "PB2", "PC2", "PD2", "PE2", "PF2", "PG2", "PH2",
            "RA1", "NB1", "BC1", "QD1", "KE1", "BF1", "NG1", "RH1"
        };

        // reset board
        this.pieces = new ArrayList<>();

        // add all white pieces
        for (String p : whitePieces) {
            IChessPiece piece = AbstractChessPiece.fromChessNotation(p, true);
            putAtPosition(piece.getPosition(), piece);
        }

        // add all black pieces
        for (String p : blackPieces) {
            IChessPiece piece = AbstractChessPiece.fromChessNotation(p, false);
            putAtPosition(piece.getPosition(), piece);
        }
    }

    /**
     * Put a piece at a position on the board.
     *
     * @param pos The position where the piece should be put
     * @param piece The piece to place
     */
    public void putAtPosition(Position pos, IChessPiece piece) {
        assert isOnBoard(pos);

        piece.setPosition(pos); // ensure position is correct for this piece

        if (!this.pieces.contains(piece)) {
            this.pieces.add(piece);
        }
    }

    /**
     * Put a piece at a position after checking that it's empty.
     *
     * @param pos The position where the piece should be put
     * @param piece The piece to place
     *
     * @throws IllegalArgumentException if the position is already occupied
     */
    public void putAtEmptyPosition(Position pos, IChessPiece piece) {
        if (isOccupied(pos))
            throw new IllegalArgumentException("Position " + pos + " is already occupied");
        putAtPosition(pos, piece);
    }

    /**
     * Check if a given position is occupied.
     *
     * @param pos The position to check
     * @return true if the position is occupied, false otherwise
     */
    public boolean isOccupied(Position pos) {
        return getAtPosition(pos) != null;
    }

    /**
     * Check if a given position is Enemy.
     *
     * @param pos The position to check
     * @return true if the position is Enemy piece, false otherwise
     */
    public boolean isEnemy(IChessPiece piece, Position pos) {
        if(getAtPosition(pos) != null)
            return getAtPosition(pos).isWhite() != piece.isWhite();
        else
            return false;
    }

    /**
     * Check if a given position is friendly.
     *
     * @param pos The position to check
     * @return true if the position is friendly piece, false otherwise
     */
    public boolean isFriendly(IChessPiece piece, Position pos) {
        if(getAtPosition(pos) != null)
            return getAtPosition(pos).isWhite() == piece.isWhite();
        else
            return false;
    }

    /**
     * Get the piece at a given position.
     *
     * @param pos The position on the board
     * @return The piece at the position
     */

    public IChessPiece getAtPosition(Position pos) {
        //assert isOnBoard(pos);

        for (IChessPiece p : this.pieces) {
            if (p.getPosition().equals(pos)) return p;
        }
        return null;
    }

    /**
     * Move a piece to a position without doing checks.
     *
     * @param piece The piece to move
     * @param toPosition The position to move to
     */
    public void forceMovePiece(IChessPiece piece, Position toPosition) {
        assert isOnBoard(toPosition);

        IChessPiece target = getAtPosition(toPosition);
        if (target != null) {
            pieces.remove(target);
        }
        putAtPosition(toPosition, piece);
    }


    /**
     * Return a list of all the pieces on the board.
     *
     * @return A list of all the pieces currently on the board.
     */
    public List<IChessPiece> getAllPieces() {
        // Ensure that manipulating the returned list cannot modify the internal
        // list of the board
        // This does not fully encapsulate the board, but it does hopefully help
        // against accidentally adding/removing pieces without intending to
        List<IChessPiece> copied = new ArrayList<>();
        copied.addAll(this.pieces);
        return copied;
    }

    /**
     * An-passant only valid for 1 move after pawn moved 2 steps forward.
     * Before making a new move, the ability for the enemy to make an an-passant move on your pieces is taken away.
     * That way, pawns hasMoved() is only true for 1 round, and only if it moves 2 steps forward.
     *
     * @return void
     */
    private void resetPawnMoves(IChessPiece piece){
        List<IChessPiece> allPieces = getAllPieces();
        for(IChessPiece p : allPieces){
            if(p instanceof ChessPiecePawn)
                if(isFriendly(piece, p.getPosition()))
                    ((ChessPiecePawn) p).setPieceNotMoved();
        }
    }

    /**
     * If the king is moving 2 steps in any direction it can only be castling.
     * If king is castling, the ability to caste has already been checked, so the rook can move to it's new position
     * before the king is moved, without taking an extra turn.
     *
     * @return void
     */
    private void performCastling(IChessPiece piece, Position toPosition){
        int rookOldx;
        int rookNewx;
        int rooky = piece.getPosition().getY();
        if(piece.getPosition().getX() - toPosition.getX() < 0){
            rookOldx = 8;
            rookNewx = 5;
        } else{
            rookOldx = 1;
            rookNewx = 3;
        }
        ChessPieceRook rook = (ChessPieceRook) getAtPosition(new Position(rookOldx,rooky));
        Position rookPos = new Position(rookNewx, rooky);
        movePiece(rook,rookPos);
    }

    /**
     * Move a piece to a position, if it can move there.
     *
     * @param piece The piece to move
     * @param toPosition The position to move to
     *
     * @return true if the piece moved, false otherwise
     */
    public boolean movePiece(IChessPiece piece, Position toPosition) {
        assert isOnBoard(toPosition);

        if (piece.canMoveTo(toPosition, this)) {
            IChessPiece target = getAtPosition(toPosition);
            if (target != null) {
                pieces.remove(target);
            }
            resetPawnMoves(piece);
            if (piece instanceof ChessPieceKing) {
                ((ChessPieceKing) piece).setPieceToMoved();
                if (Math.abs(piece.getPosition().getX() - toPosition.getX()) > 1)
                    performCastling(piece, toPosition);
            } else if(piece instanceof ChessPiecePawn){
                if(Math.abs(piece.getPosition().getX() - toPosition.getX()) > 1)
                    ((ChessPiecePawn) piece).setPieceToMoved();
            } else if(piece instanceof ChessPieceRook)
                ((ChessPieceRook) piece).setPieceToMoved();
            putAtPosition(toPosition, piece);
            return true;
        }
        return false;
    }

    /**
     * Copy the board so that modifications can safely be made without changing
     * the original.
     *
     * @return A new board which does not contain references to any part of the
     * old one
     */
    public Board copy() {
        // copying of pieces happens in Board constructor
        return new Board(copyPieces());
    }

    /**
     * Test if a Position is on this board.
     *
     * @return true if the Position is on the board, false otherwise
     */
    public boolean isOnBoard(Position pos) {
        return pos.getX() >= 1 && pos.getX() <= BOARD_WIDTH
            && pos.getY() >= 1 && pos.getY() <= BOARD_HEIGHT;
    }

    /**
     * Helper method: return a List of pieces with every piece copied.
     *
     * @return A copied list
     */
    private List<IChessPiece> copyPieces() {
        List<IChessPiece> ret = new ArrayList<>();
        for (IChessPiece p : pieces) {
            ret.add(p.copy());
        }
        return ret;
    }
}
