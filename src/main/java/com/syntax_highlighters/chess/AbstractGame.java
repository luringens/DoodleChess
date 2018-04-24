package com.syntax_highlighters.chess;

import java.util.List;
import java.util.ArrayList;

import com.syntax_highlighters.chess.entities.*;

/**
 * Game class keeping track of the game state.
 *
 * Contains a Board, as well as the two players, white and black. Keeps track of
 * which player's turn it is. The UI uses the API of this class to interact with
 * the game.
 */
public abstract class AbstractGame {
    protected Board board;
    protected IAiPlayer whiteAI = null;
    protected IAiPlayer blackAI = null;
    protected Color nextPlayerColor = Color.WHITE;
    protected List<Move> moveHistory = new ArrayList<>();
    private boolean gameOver = false;

    /**
     * Return a List of String containing all moves to date.
     *
     * @return A list of strings containing all moves.
     */
    public List<String> getMoveHistory() {
        List<String> moves = new ArrayList<>();
        for (Move m : moveHistory) {
            String piece = String.valueOf((moveHistory.size()/2)+1) + ". ";
            if(board.getAtPosition(m.newPos).getColor().isWhite()) {
                piece += "WHITE ";
            }
            else{
                piece += "BLACK ";
            }
            piece += m.getPieceString(board) + ": ";
            int a = m.getPosition().getX();
            switch(a){
                case 1:
                    piece += "A";
                    break;
                case 2:
                    piece += "B";
                    break;
                case 3:
                    piece += "C";
                    break;
                case 4:
                    piece += "D";
                    break;
                case 5:
                    piece += "E";
                    break;
                case 6:
                    piece += "F";
                    break;
                case 7:
                    piece += "G";
                    break;
                case 8:
                    piece += "H";
                    break;
                default:
                    piece += "ERROR";
                    break;
            }
            piece += String.valueOf(m.getPosition().getY());
            moves.add(piece);
        }
        return moves;
    }

    private void addMoveToHistory(Move m)
    {
        moveHistory.add(m);
    }

    /**
     * Validate and perform a move.
     * @param from Coordinate from
     * @param to Coordinate to
     * @return If the move was valid and performed.
     */
    public List<Move> performMove(Position from, Position to) {
        IChessPiece piece = getPieceAtPosition(from);
        
        // Check that the piece exists
        if (piece == null) return new ArrayList<>(); // there is no piece at the given position
        
        // Check that the piece belongs to the current player
        if (piece.getColor() != nextPlayerColor) return new ArrayList<>(); // wrong color of piece
        
        // Performs move if valid, returns whether move was performed
        List<Move> result = board.getMove(piece, to);
        if (result.size() == 1) {
            Move move = result.get(0);
            performMove(move);
        }
        return result;
    }

    /**
     * Perform the move.
     *
     * NOTE: no validation, such as in performMove - the game assumes that the
     * move is valid, since it should have been retrieved using a previous call
     * to performMove - the caller is responsible for ensuring that they do not
     * call this move with an argument that is invalid.
     *
     * @param m The move to be performed
     */
    public void performMove(Move m) {
        assert m != null;
        m.DoMove(board);
        board.setLastMove(m);
        addMoveToHistory(m);
        nextPlayerColor = nextPlayerColor.opponentColor();
    }

    /**
     * Perform AI move if next player is AI.
     *
     * If the next player is an AI player, make the player perform a move, and
     * then change turns. Otherwise do nothing.
     *
     * @return The move that was performed or null if no move was performed.
     */
    public Move PerformAIMove() {
        if (nextPlayerIsAI()) {
            Move move;
            if (nextPlayerColor.isWhite()) {
                move = whiteAI.GetMove(board);
            }
            else {
                move = blackAI.GetMove(board);
            }
            this.performMove(move);
            return move;
        }
        return null;
    }

    /**
     * Check whether the next player is AI player or human player.
     *
     * @return true if the next player is an AI, false if the next player is a
     * human
     */
    public boolean nextPlayerIsAI() {
        return (nextPlayerColor.isWhite() && whiteAI != null)
                || (nextPlayerColor.isBlack() && blackAI != null);
    }

    /**
     * Get the board.
     *
     * NOTE: Returns a reference to the internal Board in this class. Changes
     * made to the return value affects the original.
     *
     * @return The current board state
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
     * @return null if position is unoccupied, the piece occupying it otherwise
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
    public Color nextPlayerColor() {
        return nextPlayerColor;
    }

    /**
     * Checks if game is over.
     *
     * Deliberately ignores fifty-move rule and threefold repetition (at least
     * for the time being).
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Determine whether two squares are the same color.
     *
     * @param p1 A position to compare.
     * @param p2 A position to compare.
     * @return true if the squares are the same color, false otherwise
     */
    public boolean sameColoredSquare(Position p1, Position p2) {
        return (p1.getX() + p1.getY()) % 2 == (p2.getX() + p2.getY()) % 2;
    }

    /**
     * Determine the winner of the game, if any.
     *
     * Only determines winner by checkmate (not by resignation).
     *
     * @return 1 if white wins, -1 if black wins, or 0 if the game is not over
     * or a draw
     */
    public abstract int getWinner();

    /**
     * Force the game to end.
     *
     * This is called by the UI when a player gives up.
     */
    public void forceGameEnd() {
        gameOver = true;
    }
}
