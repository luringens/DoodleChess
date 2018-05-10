package com.syntax_highlighters.chess.speedtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.network.Client;
import com.syntax_highlighters.chess.network.ConnectionStatus;
import com.syntax_highlighters.chess.network.Host;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests pertaining to the networking code.
 */
class NetworkTest {
    private static final int TIMEOUT = 250;

    private Host host;
    private Client client;

    @BeforeEach
    void setup() throws Exception {
        int port = ((int)(Math.random()*5000)) + 2000;

        // Set up the host and client
        HostWrapper hostWrapper = new HostWrapper(TIMEOUT, port);
        Thread.sleep(TIMEOUT/4);
        client = new Client("localhost", port);

        // Make sure the host is read, then unwrap it.
        Thread.sleep(TIMEOUT);
        if (hostWrapper.getEx() != null) {
            throw hostWrapper.getEx();
        }
        host = hostWrapper.getHost();
    }

    @AfterEach
    void teardown() {
        System.out.println("TEARDOWN");
        if (client != null) client.Disconnect();
        if (host != null) host.Disconnect();
    }

    /**
     * Assert that the game can connect to another instance of itself.
     */
    @Test
    void canEstablishConnectionToSelf() throws Exception {
        ConnectionStatus hostStatus = host.GetStatus();
        ConnectionStatus clientStatus = client.GetStatus();   
        
        assertEquals(ConnectionStatus.Connected, hostStatus);
        assertEquals(ConnectionStatus.Connected, clientStatus);

        Thread.sleep(TIMEOUT);
    }

    /**
     * Assert that the game can (de)serialize moves between instances.
     */
    @Test
    void canTransferMovesToSelf() {
        // Send a move
        Board hostBoard = new Board();
        hostBoard.setupNewGame();
        Move sendMove = new Move(new Position(1, 2), new Position(1, 3), hostBoard);
        host.SendMove(sendMove);

        // Receive a move
        Board clientBoard = new Board();
        clientBoard.setupNewGame();
        Move receiveMove = client.ReceiveMove(clientBoard, 1000);

        assertEquals(sendMove, receiveMove);
    }
}

class HostWrapper {
    private Host host = null;
    private Exception ex;

    public HostWrapper(int timeout, int port) {
        new Thread(() -> {
            try {
                host = new Host(timeout, port);
            } catch (IOException e) {
                ex = e;
            }
        }).start();
    }

    public Host getHost() {
        return host;
    }
    public Exception getEx() {
        return ex;
    }
}