package com.syntax_highlighters.chess.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.syntax_highlighters.chess.AbstractGame;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;

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
        try {
            // Serialize move into array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(move);
            byte[] byteArray = baos.toByteArray();
            oos.close();
            baos.close();
            
            // Send it to the other client.
            outputStream.write(byteArray);
            outputStream.writeBytes("\n");
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
            String stringrep = inputReader.readLine();
            if (stringrep.equals("BYE")) {
                status = ConnectionStatus.OpponentLeft;
                return null;
            }
            byte[] bytes = stringrep.getBytes();

            // Deserialize
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(bis);
            Move move = (Move) in.readObject();
            in.close();
            bis.close();
            
            return move;
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
