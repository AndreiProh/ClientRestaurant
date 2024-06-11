package com.example.clientrestaurant;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    //private Label messageLabel;
    private static volatile String messageBuffer;
    private volatile boolean running;

    public ClientSocket(String host, int port) {
        //this.messageLabel = messageLabel;
        this.messageBuffer = "";
        connect(host, port);
    }

    private void connect(String host, int port) {
        running = true;
        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                OutputStream outputStream = socket.getOutputStream();
                writer = new PrintWriter(outputStream, true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (running) {
                    synchronized (this) {
                        if (!messageBuffer.isEmpty()) {
                            writer.println(messageBuffer);
                            messageBuffer = "";
                        }
                    }

                    if (reader.ready()) {
                        String messageFromServer = reader.readLine();
                        if (messageFromServer != null) {
                            String finalMessageFromServer = messageFromServer;
                            //Platform.runLater(() -> messageLabel.setText("Received: " + finalMessageFromServer));
                            System.out.println(finalMessageFromServer);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }



    public static synchronized void sendMessage(String message) {
        messageBuffer = message;
    }

    public void close() {
        running = false;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
