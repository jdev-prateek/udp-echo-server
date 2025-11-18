package org.example.core;

import org.example.util.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Optional;

public class UDPServer {
    private static final String ENV_UDP_ECHO_PORT = "UDP_ECHO_PORT";
    private static final int DEFAULT_PORT = 2000;
    private static final Logger logger = LoggerFactory.getLogger(UDPServer.class);
    private static final int PORT = gerPort();
    private static DatagramSocket socket;


    private static void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("\nUDP Server is shutting down...");
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }));

        try {
            byte[] bytes = new byte[256];
            DatagramPacket packet = new DatagramPacket(bytes, 256);
            socket = new DatagramSocket(PORT);

            while (true) {
                socket.receive(packet);
                DatagramPacket datagramPacket = Common.copyDatagram(packet);

                new Thread(() -> {
                    try {
                        handleRequest(datagramPacket);
                    } catch (IOException e) {
                        logger.error("Error handling request", e);
                    }
                }).start();
            }
        } catch (Exception e) {
            logger.error("UDP server error ", e);
        }
    }

    private static void handleRequest(DatagramPacket packet) throws IOException {
        logger.info("#".repeat(10));
        logger.info("Received packet from " + packet.getAddress() + ":" + packet.getPort());
        String message = new String(packet.getData(), 0, packet.getLength());
        logger.info("Message: " + message.trim());

        byte[] responseData = ("Echo: " + message).getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(responseData, responseData.length, packet.getAddress(),
                packet.getPort());
        socket.send(datagramPacket);
        logger.info("Sent echo response to " + packet.getAddress() + ":" + packet.getPort());
        logger.info("#".repeat(10));
    }

    public static int gerPort() {
        return Optional.ofNullable(System.getenv(ENV_UDP_ECHO_PORT))
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
    }

    public static void start() {
        logger.info("Starting UDP ECHO Server");
        init();
    }
}
