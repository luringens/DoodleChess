package com.syntax_highlighters.chess.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.game.AbstractGame;
import com.syntax_highlighters.chess.move.Move;

public abstract class AbstractNetworkService implements INetworkService {
    static final int HOST_PORT = 6666;
    Socket socket;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    ConnectionStatus status = ConnectionStatus.NotConnected;
    private String lastError = null;

    /** {@inheritDoc} */
	@Override
	public void SendMove(Move move) {
        try {
            outputStream.writeObject(move);
        } catch (IOException ex) {
            lastError = "Failed to send move: " + ex.getMessage();
            ex.printStackTrace();
            if (socket.isClosed()) status = ConnectionStatus.ConnectionLost;
        }
    }
    
    /** {@inheritDoc} */
    public Move GetMove(AbstractGame game) {
        return ReceiveMove(game.getBoard(), 0);
    }

    /** {@inheritDoc} */
	@Override
	public Move ReceiveMove(Board board, int timeout) {
        try {
            socket.setSoTimeout(timeout);
            return (Move) inputStream.readObject();
        }
        catch (ClassNotFoundException ex) {
            lastError = "Failed to deserialize - other client "
                + "may be running a different version of the game" + ex.getMessage();
            Disconnect();
            System.out.println("Host/client version mismatch.");
            return null;
        }
        catch (SocketTimeoutException ex) {
            // No response
            return null;
        }
        catch (Exception ex) {
            lastError = "Failed to send move: " + ex.getMessage();
            if (socket.isClosed()) status = ConnectionStatus.ConnectionLost;
            ex.printStackTrace();
            return null;
        }
	}

    /** {@inheritDoc} */
	@Override
	public void Disconnect() {
        status = ConnectionStatus.Closed;

        try {
            if (inputStream != null) inputStream.close();
        } catch (IOException ex) {
            System.out.print("Failed to close inputreader: " + ex.getMessage());
        }
        try {
            if (outputStream != null) outputStream.close();
        } catch (IOException ex) {
            System.out.print("Failed to close outputstream: " + ex.getMessage());
        }
        try {
            if (socket != null) socket.close();
        } catch (IOException ex) {
            System.out.print("Failed to close socket: " + ex.getMessage());
        }
    }

    /** {@inheritDoc} */
	@Override
	public ConnectionStatus GetStatus() {
		return status;
	}

    /** {@inheritDoc} */
	@Override
	public String GetLastFailureDescription() {
		return lastError;
	}
}
