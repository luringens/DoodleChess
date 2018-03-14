package com.syntax_highlighters.chess;

import java.util.List;
import java.util.ArrayList;

import com.syntax_highlighters.chess.entities.ChessPieceKing;
import com.syntax_highlighters.chess.entities.ChessPiecePawn;
import com.syntax_highlighters.chess.entities.ChessPieceRook;
import com.syntax_highlighters.chess.entities.IChessPiece;
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
    private boolean whiteAI;
    private boolean blackAI;
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
        assert (AILevel >= 1 && AILevel <= 3);
        this.whiteAI = whiteAI;
        this.blackAI = blackAI;
        
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
        return board.movePiece(piece, to);
    }

    public Move PerformAIMoves() {
        if (nextPlayerIsAI()) {
            // Get AI move

            // Perform AI move

            // Return AI move
        }

        throw new NotImplementedException();
    }

    private boolean nextPlayerIsAI() {
        return (nextPlayerWhite && whiteAI) || (!nextPlayerWhite && blackAI);
    }

    public Board getBoard() {
        return this.board;
    }

    public IChessPiece getPieceAtPosition(Position p) {
        return this.board.getAtPosition(p);
    }

    public List<IChessPiece> getPieces() {
        return this.board.getAllPieces();
    }
}
