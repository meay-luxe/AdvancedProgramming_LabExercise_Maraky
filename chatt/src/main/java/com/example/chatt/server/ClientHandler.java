package com.example.chatt.server;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error setting up client handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try{
            username = reader.readLine();
            if (username == null || username.isBlank()) {
                disconnect();
                return;
            }
            System.out.println(username + " has joined!");

            ChatServer.broadcastMessage(
                    "🟢 " + username + " has joined! (" +
                            ChatServer.getClientCount() + " users online)"
            );

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("[" + username + "]: " + message);
                ChatServer.broadcastMessage("[" + username + "]: " + message);
            }

        } catch (IOException e) {
            System.out.println((username != null ? username : "Unknown client") + " disconnected.");
        } finally {
            disconnect();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    private void disconnect() {
        try {
            ChatServer.removeClient(this);
            if (username != null) {
                ChatServer.broadcastMessage(
                        "🔴 " + username + " has left! (" +
                                ChatServer.getClientCount() + " users online)"
                );
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing socket: " + e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }
}