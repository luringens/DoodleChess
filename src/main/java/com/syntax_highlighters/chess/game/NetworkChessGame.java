package com.syntax_highlighters.chess.game;

import com.syntax_highlighters.chess.AsyncPlayer;
import com.syntax_highlighters.chess.Color;
import com.syntax_highlighters.chess.move.Move;
import com.syntax_highlighters.chess.network.AbstractNetworkService;
import com.syntax_highlighters.chess.network.ConnectionStatus;

/**
 * Game class keeping track of the game state and an opponent on the internet.
 */
public class NetworkChessGame extends ChessGame {
    private final Color opponentColor;
    private final AbstractNetworkService opponent;
    private final AsyncPlayer opponentAsync;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void PerformAIMove() {
        if (nextPlayerIsAI() && !gameOver) {
            Move move = opponentAsync.getMove(this);
            if (move != null) {
                performMove(move);
            }
            else if (opponent.GetStatus() != ConnectionStatus.Connected) {
                forceGameEnd();
            }
        }

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Move PerformAIMoveAsync() {
        if (nextPlayerIsAI() && !gameOver) {
            Move move = opponentAsync.pollMove(this);
            if (move != null) {
                performMove(move);
                return move;
            }
            else if (opponent.GetStatus() != ConnectionStatus.Connected) {
                forceGameEnd();
            }
        }
        
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean nextPlayerIsAI() {
        return nextPlayerColor == opponentColor;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void performMove(Move move) {
        if (!nextPlayerIsAI()) {
            opponent.SendMove(move.copy());
        }
        super.performMove(move);
    }
    
    /**
     * Get the network state.
     *
     * @return The current connection status
     */
    public ConnectionStatus getNetworkState() {
        return opponent.GetStatus();
    }
    
    /** 
     * Get a description of the last error the network client ran into.
     *
     * @return A string describing what caused the last error
     */
    public String getNetworkError() {
        return opponent.GetLastFailureDescription();
    }

    /**
     * Disconnect the network session in order to free the network resources.
     */
	public void disconnect() {
        opponent.Disconnect();
	}
}
