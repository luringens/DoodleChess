package com.syntax_highlighters.chess.network;

/**
 * An enum representing the status of a connection to another computer.
 */
public enum ConnectionStatus {
    /** The game has not yet connected. */
    NotConnected,
    
    /** The game is currently connected. */
    Connected,
    
    /** Connection to the other party has been lost. */
    ConnectionLost,
    
    /** The opponent left the game. */
    OpponentLeft,

    /** This client has closed the connection. */
    Closed
}