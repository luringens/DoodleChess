package com.syntax_highlighters.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import com.syntax_highlighters.chess.chesspiece.AbstractChessPiece;
import com.syntax_highlighters.chess.chesspiece.ChessPieceBishop;
import com.syntax_highlighters.chess.chesspiece.ChessPieceKing;
import com.syntax_highlighters.chess.chesspiece.ChessPieceKnight;
import com.syntax_highlighters.chess.chesspiece.ChessPiecePawn;
import com.syntax_highlighters.chess.chesspiece.ChessPieceQueen;
import com.syntax_highlighters.chess.chesspiece.ChessPieceRook;
import com.syntax_highlighters.chess.chesspiece.IChessPiece;
import com.syntax_highlighters.chess.move.Move;

/**
 * Holds the current state of the board.
 *
 * The board state is stored in a 8x8 grid containing the pieces (null values
 * signifying empty squares).
 *
 * The white player starts at rows 1 and 2
 * The black player starts at rows 7 and 8
 */
public class    Board {
    // constants, just in case
    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;
    
    private Move lastMove;
    private IChessPiece whiteKing = null; // For caching purposes.
    private IChessPiece blackKing = null; // For caching purposes.

    // for performance
    private final int[][] positionsLookupTable = new int[BOARD_HEIGHT][BOARD_WIDTH];
    private final Map<Color, List<IChessPiece>> piecesOfColor = new HashMap<>();

    private List<IChessPiece> pieces = new ArrayList<>();

    /**
     * Create an empty board.
     */
    public Board() {
        initBoard();
    }

    /**
     * Create a board from a list of pieces.
     * @param pieces The pieces that belong to the board.
     */
    public Board(List<IChessPiece> pieces) {
        this.pieces.addAll(pieces);
        initBoard();
    }

    /**
     * Helper method: reinitialize all positions on the board.
     *
     * Modifies positionsLookupTable.
     */
    private void initBoard() {
        // expensive if we must do it a lot, but hopefully we won't have to
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                positionsLookupTable[i][j] = -1;
            }
        }
        // fill in all the positions of the pieces currently on the board
        for (int i = 0; i < this.pieces.size(); i++) {
            updatePositionIndex(this.pieces.get(i).getPosition(), i);
        }

        piecesOfColor.put(Color.WHITE, new ArrayList<>(pieces.stream()
                .filter(p->p.getColor()==Color.WHITE)
                .collect(Collectors.toList())));
        piecesOfColor.put(Color.BLACK, new ArrayList<>(pieces.stream()
                .filter(p->p.getColor()==Color.BLACK)
                .collect(Collectors.toList())));
    }

    /**
     * Helper method: update the lookup table's index based on the position.
     *
     * Modifies positionsLookupTable. No bounds checks.
     * @param pos The position to update.
     * @param index The index to save.
     */
    private void updatePositionIndex(Position pos, int index) {
        positionsLookupTable[pos.getY()-1][pos.getX()-1] = index;
    }

    /**
     * Helper method: return the index of the piece with the given position in
     * the list of pieces, or -1 if the position is empty.
     *
     * No bounds checks.
     *
     * @param pos The position to look up.
     * @return The index of the piece occupying the position in the pieces list,
     * if the position is occupied, or -1 otherwise
     */
    private int lookupPositionIndex(Position pos) {
        return positionsLookupTable[pos.getY()-1][pos.getX()-1];
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
        this.lastMove = null;
        this.whiteKing = null; // For caching purposes.
        this.blackKing = null; // For caching purposes.
        initBoard();

        // add all white pieces
        for (String p : whitePieces) {
            IChessPiece piece = AbstractChessPiece.fromChessNotation(p, Color.WHITE);
            putAtPosition(piece.getPosition(), piece);
        }

        // add all black pieces
        for (String p : blackPieces) {
            IChessPiece piece = AbstractChessPiece.fromChessNotation(p, Color.BLACK);
            putAtPosition(piece.getPosition(), piece);
        }
    }

    /**
     * Performs x random moves on the board, starting with white.
     *
     * @param numMoves How many random moves to do.
     */
    public void setupPracticeGame(int numMoves) {
        // Try three times to set up a valid game.
        // Otherwise keep it as-is.
        final int attempts = 3;
        for (int attempt = 0; attempt < attempts; attempt++) {
            setupNewGame();
            Color nextColor = Color.WHITE;
            for (int i = 0; i < numMoves; i++) {
                boolean didMove = doRandomMove(nextColor);
                nextColor = nextColor.opponentColor();

                // If a move was not performed, the game is likely in checkmate. Oops.
                if (!didMove) break;
            }

            // If both kings are still alive, don't try again.
            long kings = getAllPieces().stream().filter(p -> p instanceof ChessPieceKing).count();
            if (kings == 2) break;
        }
    }
    /**
     * Performs a random move for a color.
     * @param color The color to do a move for.
     * @return True if a move was performed, False if there were no legal moves.
     */
    private boolean doRandomMove(Color color) {
        List<Move> moves = getAllPieces(color).stream()
                .flatMap(p -> p.allPossibleMoves(this).stream())
                .collect(Collectors.toList());
        if (moves.size() == 0) return false;

        Move move = moves.get(new Random().nextInt(moves.size()));
        move.DoMove(this);
        return true;
    }

    /**
     * Put a piece at a position on the board.
     *
     * @param pos The position where the piece should be put
     * @param piece The piece to place
     */
    public void putAtPosition(Position pos, IChessPiece piece) {
        assert isOnBoard(pos);
        
        IChessPiece target = getAtPosition(pos);
        if (target != null) {
            removePiece(target);
        }

        Position oldPos = piece.getPosition();
        piece.setPosition(pos); // ensure position is correct for this piece
        updatePositionIndex(oldPos, -1);

        if (!this.pieces.contains(piece)) {
            this.pieces.add(piece);
            updatePositionIndex(pos, this.pieces.size()-1); // the index of the newly added piece
            piecesOfColor.get(piece.getColor()).add(piece);
        }
        else {
            updatePositionIndex(pos, this.pieces.indexOf(piece));
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
        if(pos.getY() > 8 || pos.getY() < 0 || pos.getX() < 0 || pos.getX() > 8)
            return true;
        return getAtPosition(pos) != null;
    }

    /**
     * Check if a given position is Enemy.
     *
     * @param piece The piece to compare *friendlyness* to.
     * @param pos The position to check
     * @return true if the position is Enemy piece, false otherwise
     */
    public boolean isEnemy(IChessPiece piece, Position pos) {
        IChessPiece pieceAtPos = getAtPosition(pos);
        return pieceAtPos != null && pieceAtPos.getColor() != piece.getColor();
    }

    /**
     * Check if a given position is friendly.
     *
     * @param piece The piece to compare *friendlyness* to.
     * @param pos The position to check
     * @return true if the position is friendly piece, false otherwise
     */
    public boolean isFriendly(IChessPiece piece, Position pos) {
        IChessPiece pieceAtPos = getAtPosition(pos);
        return pieceAtPos != null && pieceAtPos.getColor() == piece.getColor();
    }

    /**
     * Get the piece at a given position.
     *
     * @param pos The position on the board
     * @return The piece at the position
     */

    public IChessPiece getAtPosition(Position pos) {
        //assert isOnBoard(pos);
        if (!isOnBoard(pos)) return null;
        int index = lookupPositionIndex(pos);
        if (index != -1) {
            return this.pieces.get(index);
        }
        return null;
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
        return new ArrayList<>(this.pieces);
    }
    
    public List<IChessPiece> getAllPieces(Color color) {
        return new ArrayList<>(piecesOfColor.get(color));
    }

    /**
     * Gets a move for a piece to a position if there is one, then executes it.
     *
     * @param piece The piece to move
     * @param toPosition The position to move to
     */
    public void movePiece(IChessPiece piece, Position toPosition) {
        List<Move> moves = getMove(piece, toPosition);
        if (moves.size() == 1) {
            this.lastMove = moves.get(0);
            lastMove.DoMove(this);
        }
    }

    /**
     * Get a move for a piece to a position if there is one.
     *
     * @param piece The piece to move
     * @param toPosition The position to move to
     *
     * @return A list of all the moves to the given position, if any
     */
    public List<Move> getMove(IChessPiece piece, Position toPosition) {
        assert isOnBoard(toPosition);
        return piece.getMovesTo(toPosition, this);
    }

    /**
     * Copy the board so that modifications can safely be made without changing
     * the original.
     *
     * @return A new board which does not contain references to any part of the
     * old one
     */
    public Board copy() {
        return new Board(copyPieces());
    }

    /**
     * Test if a Position is on this board.
     *
     * @param pos The position to check.
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

    /**
     * Determine whether a given move puts a given king at risk.
     *
     * Used in order to determine illegal moves, seeing as moves which leave the
     * king threatened are not legal in chess.
     *
     * @param m The move to consider
     * @param kingColor The color of the king to check for
     *
     * @return true if the move is safe, false otherwise
     */
    public boolean moveDoesntPutKingInCheck(Move m, Color kingColor) {
        ChessPieceKing king = (ChessPieceKing)getKing(kingColor);
        if (king == null) return true;
        m.DoMove(this);
        boolean inCheck = king.isThreatened(this);
        m.UndoMove(this);
        return !inCheck;
    }
    
    /**
     * Get the last move performed in the game.
     *
     * @return The last Move that was performed
     */
    public Move getLastMove() {
        return this.lastMove;
    }

    /**
     * Determine whether the given player is in checkmate.
     *
     * @param playerColor The color of the player to check for
     *
     * @return true if the given player is in checkmate, false otherwise
     */
    public boolean checkMate(Color playerColor) {
        List<IChessPiece> allPieces = getAllPieces();
        if (allPieces.size() == 0) return false; // not possible
        ChessPieceKing king = (ChessPieceKing)getKing(playerColor);
        return king == null || king.isThreatened(this) && getAllPieces(playerColor).stream()
                .mapToLong(p -> p.allPossibleMoves(this).size()).sum() == 0;
    }

    /**
     * Convenience method for displaying the board.
     * 
     * @return A String indicating the current positions of each of the pieces
     * on the board, arranged in a 2D grid
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int y = 8; y >= 1; y--) {
            for (int x = 1; x <= 8; x++) {
                IChessPiece p = this.getAtPosition(new Position(x, y));
                if (p != null) {
                    if (p instanceof ChessPieceKing) b.append("K");
                    if (p instanceof ChessPieceQueen) b.append("Q");
                    if (p instanceof ChessPieceRook) b.append("R");
                    if (p instanceof ChessPieceBishop) b.append("B");
                    if (p instanceof ChessPieceKnight) b.append("N");
                    if (p instanceof ChessPiecePawn) b.append("p");
                }
                else b.append('.');
            }
            b.append('\n');
        }
        return b.toString();
    }

    /**
     * Removes a piece from the board.
     *
     * In order to not invalidate all the positions in the lookup table, swap
     * the piece in the list with the last piece, before removing
     *
     * @param p The piece to remove from the board.
     */
    public void removePiece(IChessPiece p) {
        int index = lookupPositionIndex(p.getPosition());
        assert(index >= 0 && index < pieces.size());
        int lastPieceIndex = pieces.size()-1;
        if (index != lastPieceIndex) {
            IChessPiece lastPieceInList = pieces.get(lastPieceIndex);
            pieces.set(index, lastPieceInList);
            updatePositionIndex(lastPieceInList.getPosition(), index);
        }
        
        // p should now be at the end of the list
        pieces.remove(pieces.size()-1);
        updatePositionIndex(p.getPosition(), -1);
        piecesOfColor.get(p.getColor()).remove(p);
    }

    /**
     * Find the king of the specified color and caches the result.
     *
     * @param color The color of king to look for.
     * @return The king of the specified color.
     */
    public IChessPiece getKing(Color color) {
        if (color.isWhite()) {
            if (whiteKing == null) {
                whiteKing = getAllPieces(Color.WHITE).stream()
                        .filter(p -> p instanceof ChessPieceKing)
                        .findFirst().orElse(null);
            }
            return whiteKing;
        }
        else {
            if (blackKing == null) {
                blackKing = getAllPieces(Color.BLACK).stream()
                        .filter(p -> p instanceof ChessPieceKing)
                        .findFirst().orElse(null);
            }
            return blackKing;
        }
    }

    /**
     * Sets the last performed move.
     * @param m The move to set.
     */
    public void setLastMove(Move m) {
        this.lastMove = m;
    }
}
