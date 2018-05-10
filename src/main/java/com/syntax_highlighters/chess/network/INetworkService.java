package com.syntax_highlighters.chess.network;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.IBlockingPlayer;
import com.syntax_highlighters.chess.Move;

/**
 * Represents a service that abstracts communicating to another player.
 * Is implemented by host and client alike.
 */
interface INetworkService extends IBlockingPlayer {
    /**
     * Sends a move to the opponent.
     * @param move The move to send.
     */
    void SendMove(Move move);

    /**
     * Receive a move from the opponent.
     * @param board The board for the move to apply to.
     * @param timeout How many milliseconds to wait.
     * @return The move that was performed by the opponent, or null.
     */
    Move ReceiveMove(Board board, int timeout);

    /**
     * Disconnect the online session.
     */
    void Disconnect();

    /**
     * Get the status of the connection.
     * @return The status of the connection.
     */
    ConnectionStatus GetStatus();

    /**
     * Gets the last failure description.
     * @return the last failure description.
     */
    String GetLastFailureDescription();
}
