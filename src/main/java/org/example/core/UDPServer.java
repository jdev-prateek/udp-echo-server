package org.example.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Optional;

public class UDPServer {
    private static final String ENV_UDP_ECHO_PORT = "UDP_ECHO_PORT";
    private static final int DEFAULT_PORT = 2000;
    private static final Logger logger = LoggerFactory.getLogger(UDPServer.class);
    private static final int PORT = gerPort();
    private static DatagramSocket socket;


    private static void init(){
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            logger.error("Failed to start UDP server", e);
        }
    }

    public static int gerPort(){
        return Optional.ofNullable(System.getenv(ENV_UDP_ECHO_PORT))
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
    }

    public static void start(){
        logger.info("Starting UDP ECHO Server");
        init();
    }
}
