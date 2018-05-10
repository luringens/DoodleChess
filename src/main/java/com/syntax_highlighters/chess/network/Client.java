package com.syntax_highlighters.chess.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        status = ConnectionStatus.Connected;
    }
}