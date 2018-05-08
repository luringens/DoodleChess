package com.syntax_highlighters.chess;

import java.util.List;
import java.util.stream.Collectors;
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
    protected AsyncPlayer whiteAI = null;
    protected AsyncPlayer blackAI = null;
    protected Color nextPlayerColor = Color.WHITE;
    protected List<Move> moveHistory = new ArrayList<>();
    protected boolean gameOver = false;

    /**
     * Return a List of String containing all moves to date.
     *
     * @return A list of strings containing all moves.
     */
    public List<String> getMoveHistory() {
        List<String> moves = new ArrayList<>();
        for (int i = 0; i < moveHistory.size(); i++) {
            Move m = moveHistory.get(i);
            String piece = String.valueOf(i+1) + ". ";
            moves.add(piece + m.toString());
        }
        return moves;
    }

    private void addMoveToHistory(Move m)
    {
        moveHistory.add(m);
    }

    public List<Move> getMoves(Position from, Position to) {
        IChessPiece piece = getPieceAtPosition(from);
        
        // Check that the piece exists
        if (piece == null) return new ArrayList<>(); // there is no piece at the given position
        
        // Check that the piece belongs to the current player
        if (piece.getColor() != nextPlayerColor) return new ArrayList<>(); // wrong color of piece
        
        // Performs move if valid, returns whether move was performed
        return board.getMove(piece, to);
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
     * Undoes the last performed move.
     */
    public void undoMove() {
        // Don't undo if there's nothing left to do
        if (moveHistory.size() < 1) return;
        
        // Pop the most recent move off the list.
        int i = moveHistory.size() - 1;
        Move lastMove = moveHistory.get(i);
        moveHistory.remove(i);
        
        // Set board's last move, null if there's none left.
        Move lastmove = moveHistory.size() > 0 ? moveHistory.get(i - 1) : null;
        board.setLastMove(lastmove);
        
        nextPlayerColor = nextPlayerColor.opponentColor();
        lastMove.UndoMove(board);
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
                move = whiteAI.getMove(this);
            }
            else {
                move = blackAI.getMove(this);
            }
            if (move != null) this.performMove(move);
            return move;
        }
        return null;
    }
    
    /**
     * Perform AI move if next player is AI.
     *
     * If the next player is an AI player, make the player perform a move, and
     * then change turns. Otherwise do nothing.
     *
     * @return The move that was performed or null if no move was performed.
     */
    public Move PerformAIMoveAsync() {
        if (nextPlayerIsAI()) {
            Move move;
            if (nextPlayerColor.isWhite()) {
                move = whiteAI.pollMove(this);
            }
            else {
                move = blackAI.pollMove(this);
            }
            if (move != null) this.performMove(move);
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
    public int getWinner() {
        if (board.checkMate(Color.WHITE)) return -1;
        if (board.checkMate(Color.BLACK)) return 1;
        return 0;
    }

    /**
     * Get a list of all the possible moves that can be made during this turn.
     *
     * @return A list of all the possible moves that can be made by the current
     * player
     */
    public abstract List<Move> allPossibleMoves();

    /**
     * Get a list of all possible moves a given piece can make this turn.
     *
     * @return A list of the possible moves that can be made by the current
     * player using this piece.
     */
    public abstract List<Move> allPossibleMoves(IChessPiece piece);

    /**
     * Force the game to end.
     *
     * This is called by the UI when a player gives up.
     */
    public void forceGameEnd() {
        gameOver = true;
    }

    /**
     * Check whether the piece can move to the given position during the given
     * turn.
     *
     * @param piece The piece to move
     * @param pos The position to move to
     *
     * @return true if the piece can currently move to the position, false
     * otherwise
     */
    public abstract boolean canMoveTo(IChessPiece piece, Position pos);
    
    /**
     * Determine the strength of the board for a given player.
     *
     * @param color The color to determine score for.
     * @return The score of the board for the given player.
     */
    public int evaluateScore(Color color) {
        // Check if we are in an "end state".
        boolean endstate = false;
        boolean whiteQueenExists = false;
        boolean blackQueenExists = false;
        for (IChessPiece p : board.getAllPieces()) {
            if (p instanceof ChessPieceQueen) {
                if (p.getColor().isWhite()) whiteQueenExists = true;
                else blackQueenExists = true;
            }
        }

        // Endstate is when both queens are dead.
        if (!whiteQueenExists && !blackQueenExists) endstate = true;

        int score = ((int)(Math.random()*10)) - 5;
        for (IChessPiece p : board.getAllPieces()) {
            int pscore;
            if (endstate && p instanceof ChessPieceKing) {
                pscore = ((ChessPieceKing)p).getEndgamePositionalScore();
            }
            else pscore = p.getPositionalScore();

            if (p.getColor() == color) score += pscore;
            else score -= pscore;
        }
        return score;
    }

    /**
     * Copy the board.
     * @return A copy of the board.
     */
    public abstract AbstractGame copy();

    protected List<Move> copyMoveHistory() {
        return moveHistory.stream().map(m -> m.copy()).collect(Collectors.toList());
    }

    /**
     * Returns a list of all possible moves for the next player.
     * @return A list of all possible moves for the next player.
     */
	public List<Move> getPossibleMoves() {
		return this.getBoard().getAllPieces().stream()
            .filter(p -> p.getColor() == nextPlayerColor)
            .flatMap(p -> p.allPossibleMoves(board).stream())
            .collect(Collectors.toList());
	}
}
