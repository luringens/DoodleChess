package com.syntax_highlighters.chess.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Extends AbstractNetworkService as a client.
 */
public class Client extends AbstractNetworkService {
    /**
     * Creates a Client and attempts to connect to an address on the default port.
     * @param address The address to connect to.
     */
    public Client(String address) throws IOException {
        this(address, HOST_PORT);
    }

    /**
     * Creates a Client and attempts to connect to an address.
     * @param address The address to connect to.
     * @param port The port to use.
     */
    public Client(String address, int port) throws IOException {
        String host = InetAddress.getByName(address).getCanonicalHostName();
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        status = ConnectionStatus.Connected;
    }
}