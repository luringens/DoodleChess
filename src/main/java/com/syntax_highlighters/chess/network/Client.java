package com.syntax_highlighters.chess.network;

import java.net.InetAddress;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Extends AbstractNetworkService as a client.
 */
public class Client extends AbstractNetworkService {
    /**
     * Creates a Client and attempts to connect to an address.
     * @param address The address to connect to.
     */
    public Client(String address) throws IOException {
        String host = InetAddress.getByName(address).getCanonicalHostName();
        socket = new Socket(host, HOST_PORT);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}