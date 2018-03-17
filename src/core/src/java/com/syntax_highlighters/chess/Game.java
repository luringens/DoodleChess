package com.syntax_highlighters.chess;

import java.util.List;
import java.util.ArrayList;

import com.syntax_highlighters.chess.entities.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Game class keeping track of the game state.
 *
 * Contains a Board, as well as the two players, white and black. Keeps track of
 * which player's turn it is. Records move history. The UI uses the API of this
 * class to interact with the game.
 */
public class Game {
    private Board board;
    private IAiPlayer whiteAI = null;
    private IAiPlayer blackAI = null;
    private boolean nextPlayerWhite = true;
    private List<Move> history;

    /**
     * Constructor.
     *
     * Creates a new, blank game and sets up the board.
     *
     * @param whiteAI True if white should be an AI
     * @param blackAI True if black should be an AI
     * @param AILevel AI difficulty level from 1 to 3
     */
    public Game(boolean whiteAI, boolean blackAI, int AILevel) {
        MiniMaxAIPlayer.Difficulty d;
        switch (AILevel) {
            case 1: d = MiniMaxAIPlayer.Difficulty.Easy; break;
            case 2: d = MiniMaxAIPlayer.Difficulty.Medium; break;
            case 3: d = MiniMaxAIPlayer.Difficulty.Hard; break;
            default: throw new RuntimeException("Difficulty must be between 1 and 3.");
        }
        if (whiteAI) this.whiteAI = new MiniMaxAIPlayer(true, d);
        if (blackAI) this.blackAI = new MiniMaxAIPlayer(false, d);
        
        this.board = new Board();
        this.history = new ArrayList<>();
        
        this.board.setupNewGame();
    }

    /**
     * Validate and perform a move.
     * @param from Coordinate from
     * @param to Coordinate to
     * @return If the move was valid and performed.
     */
    public boolean performMove(Position from, Position to) {
        IChessPiece piece = getPieceAtPosition(from);
        
        // Check that the piece exists
        if (piece == null) return false; // there is no piece at the given position
        
        // Check that the piece belongs to the current player
        if (piece.isWhite() != nextPlayerWhite) return false; // wrong color of piece
        
        // Performs move if valid, returns whether move was performed

        if (piece instanceof ChessPieceKing){
            ((ChessPieceKing) piece).setPieceToMoved();
        }

        if (piece instanceof ChessPieceRook) {
            ((ChessPieceRook) piece).setPieceToMoved();
        }
        
        //TODO: move rook if castling.
        //TODO: update all "movedLast" pawns such that the specialty move an-passant is only possible for 1 move.

        boolean result = board.movePiece(piece, to);
        if (result) {
            nextPlayerWhite = !nextPlayerWhite;
        }
        return result;
    }

    public void PerformAIMove() {
        if (nextPlayerIsAI()) {
            if (nextPlayerWhite) {
                whiteAI.PerformMove(board);
            }
            else {
                blackAI.PerformMove(board);
            }
            nextPlayerWhite = !nextPlayerWhite;
        }
    }

    /**
     * Check whether the next player is AI player or human player.
     *
     * @return true if the next player is an AI, false if the next player is a
     * human
     */
    private boolean nextPlayerIsAI() {
        return (nextPlayerWhite && whiteAI != null) || (!nextPlayerWhite && blackAI != null);
    }

    /**
     * Get the board.
     *
     * NOTE: Returns a reference to the internal Board in this class. Changes
     * made to the return value affects the original.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Check whether a Position is on the board.
     *
     * @param p The position to check
     * @return true if the position is on the board, false otherwise
     */
    public boolean isOnBoard(Position p) {
        return board.isOnBoard(p);
    }

    /**
     * Get the piece currently occupying position p
     *
     * @param p The position to retrieve piece at
     * @return null if position was occupied, the piece occupying it otherwise
     */
    public IChessPiece getPieceAtPosition(Position p) {
        return this.board.getAtPosition(p);
    }

    /**
     * Get all pieces on board.
     *
     * @return A list of all the pieces currently on the board
     */
    public List<IChessPiece> getPieces() {
        return this.board.getAllPieces();
    }

    /**
     * Check whose turn it is next.
     *
     * @return true if it's white's turn to move, false otherwise
     */
    public boolean nextPlayerIsWhite() {
        return nextPlayerWhite;
    }
}
