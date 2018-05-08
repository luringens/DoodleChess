package com.syntax_highlighters.chess.network;

import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
        socket = serverSocket.accept();

        // Get an input and output stream.
        inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new DataOutputStream(socket.getOutputStream());
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
