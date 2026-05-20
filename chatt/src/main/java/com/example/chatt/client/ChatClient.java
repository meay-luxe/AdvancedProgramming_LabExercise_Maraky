package com.example.chatt.client;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    private Consumer<String> messageHandler;

    public ChatClient(String username, Consumer<String> messageHandler) {
        this.username = username;
        this.messageHandler = messageHandler;
    }

    public boolean connect() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Send username first
            writer.println(username);

            startListening();
            return true;

        } catch (IOException e) {
            System.out.println("Cannot connect: " + e.getMessage());
            return false;
        }
    }

    private void startListening() {
        Thread listenerThread = new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    final String finalMessage = message;
                    messageHandler.accept(finalMessage);
                }
            } catch (IOException e) {
                messageHandler.accept("❌ Disconnected from server.");
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error disconnecting: " + e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }
}
