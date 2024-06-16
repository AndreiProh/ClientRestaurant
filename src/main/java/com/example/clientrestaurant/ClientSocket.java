package com.example.clientrestaurant;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    private Controller controller;
    private SignUpController signUpController;
    private static volatile String messageBuffer;
    private volatile boolean running;

    public ClientSocket(String host, int port, Controller controller) {
        //this.messageLabel = messageLabel;
        this.controller = controller;
        this.messageBuffer = "";
        connect(host, port);
    }

    public void setSignUpController(SignUpController signUpController) {
        this.signUpController = signUpController;
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
                            messageReceivedHandler(finalMessageFromServer);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void messageReceivedHandler(String finalMessageFromServer) throws IOException {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage = new Gson().fromJson(finalMessageFromServer, JsonObject.class);
        String typeOfMessage = jsonMessage.get("type").getAsString();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
        Parent root = loader.load();
        // Получение контроллера

        if (typeOfMessage.equals("logup")) {
            int registeredStatus = jsonMessage.get("status").getAsInt();
            System.out.println("in IF");
            switch (registeredStatus) {
                case 0:
                    controller.setLabelWarningText("Попробуйте позже");
                    signUpController.setLabelWarningText("Попробуйте позже");

                    break;
                case 1:
                    System.out.println("in case 1");
                    controller.setLabelWarningText("Вы успешно зарегистрированы");
                    signUpController.setLabelWarningText("Вы успешно зарегистрированы");
                    break;
                case 2:
                    controller.setLabelWarningText("Пользователь с таким логином уже существует");
                    signUpController.setLabelWarningText("Пользователь с таким логином уже существует");
                    break;
                default:
                    Platform.runLater(() -> controller.setLabelWarningText("Что-то пошло не так"));
            }

        }
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
