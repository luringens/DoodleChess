package com.syntax_highlighters.chess;

import java.util.ArrayList;
import java.util.List;

import com.syntax_highlighters.chess.entities.*;
import com.syntax_highlighters.chess.network.AbstractNetworkService;
import com.syntax_highlighters.chess.network.ConnectionStatus;

/**
 * Game class keeping track of the game state and an opponent on the internet.
 */
public class NetworkChessGame extends ChessGame {
    protected Color opponentColor;
    AbstractNetworkService opponent;
    AsyncPlayer opponentAsync;

    /**
     * Creates a new, blank game and sets up the board.
     *
     * @param opponentColor The color of the opponent.
     * @param opponent The AbstractNetworkService representing the opponent.
     */
    public NetworkChessGame(Color opponentColor, AbstractNetworkService opponent) {
        super(null, null);
        this.opponentColor = opponentColor;
        this.opponent = opponent;
        this.opponentAsync = new AsyncPlayer(opponent);
    }

    /** {@inheritDoc} */
    @Override
    public Move PerformAIMove() {
        if (nextPlayerIsAI()) {
            Move move = opponentAsync.getMove(this);
            if (move != null) {
                performMove(move);
                return move;
            }
        }
        
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public Move PerformAIMoveAsync() {
        if (nextPlayerIsAI()) {
            Move move = opponentAsync.pollMove(this);
            if (move != null) {
                performMove(move);
                return move;
            }
        }
        
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean nextPlayerIsAI() {
        return nextPlayerColor == opponentColor;
    }

    /** {@inheritDoc} */
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
    
    /** {@inheritDoc} */
    @Override
    public void performMove(Move move) {
        if (!nextPlayerIsAI()) {
            opponent.SendMove(move.copy());
        }
        super.performMove(move);
    }
    
    /**
     * Get the network state.
     */
    public ConnectionStatus getNetworkState() {
        return opponent.GetStatus();
    }
    
    /** 
     * Get a description of the last error the network client ran into.
     */
    public String getNetworkError() {
        return opponent.GetLastFailureDescription();
    }
}
