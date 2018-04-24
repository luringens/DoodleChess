package com.syntax_highlighters.chess.network;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

public abstract class AbstractNetworkService implements INetworkService {
    protected static final int HOST_PORT = 6666;
    protected Socket socket;
    protected BufferedReader inputReader;
    protected DataOutputStream outputStream;
    protected ConnectionStatus status = ConnectionStatus.NotConnected;
    protected String lastError = null;

    /** {@inheritDoc} */
	@Override
	public void SendMove(Move move) {
        // TODO: Convert into string.
        String stringrep = "Nf3 Nc6";
        try {
            outputStream.writeBytes(stringrep + "\n");
        } catch (IOException ex) {
            lastError = "Failed to send move: " + ex.getMessage();
            if (socket.isClosed()) status = ConnectionStatus.ConnectionLost;
        }
	}

    /** {@inheritDoc} */
	@Override
	public Move ReceiveMove(Board board, int timeout) {
        try {
            socket.setSoTimeout(timeout);
            String stringrep = inputReader.readLine();
            if (stringrep.equals("BYE")) {
                status = ConnectionStatus.OpponentLeft;
                return null;
            }
            
            // TODO: Convert into move.
            return new Move(new Position(1, 1), new Position(2, 2), board);
        } catch (SocketTimeoutException ex) {
            // No response
            return null;
        } catch (Exception ex) {
            lastError = "Failed to send move: " + ex.getMessage();
            if (socket.isClosed()) status = ConnectionStatus.ConnectionLost;
            return null;
        }
	}

    /** {@inheritDoc} */
	@Override
	public void Disconnect() {
        try {
            if (outputStream != null)
                outputStream.writeBytes("BYE\n");
        } catch (Exception ex) { /* Doesn't matter if this fails. */ }

        try {
            if (inputReader != null) inputReader.close();
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
