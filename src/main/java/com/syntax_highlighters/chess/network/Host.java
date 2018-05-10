package com.syntax_highlighters.chess.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

/**
 * Extends AbstractNetworkService as a host.
 */
public class Host extends AbstractNetworkService {
    private final ServerSocket serverSocket;

    /**
     * Creates a Host and looks for a connection on the default port.
     * Throws SocketTimeoutException on a timeout.
     * @param timeout Timeout waiting for connection in ms.
     */
    public Host(int timeout) throws IOException {
        this(timeout, HOST_PORT);
    }

    /**
     * Creates a Host and looks for a connection.
     * Throws SocketTimeoutException on a timeout.
     * @param timeout Timeout waiting for connection in ms.
     * @param port The port to use.
     */
    public Host(int timeout, int port) throws IOException {
        // Open the socket
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(timeout);
        serverSocket.setReuseAddress(true);
        
        try {
            socket = serverSocket.accept();
        } catch (SocketTimeoutException ex) {
            serverSocket.close();
            throw ex;
        }

        // Get an input and output stream.
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        status = ConnectionStatus.Connected;
    }

    /** {@inheritDoc} */
	@Override
	public void Disconnect() {
        super.Disconnect();
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException ex) {
            System.out.print("Failed to close serversocket: " + ex.getMessage());
        }
    }
}
