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

        if (typeOfMessage.equals("logup")) {
            int registeredStatus = jsonMessage.get("status").getAsInt();
            System.out.println("in IF");
            switch (registeredStatus) {
                case 0:
                    signUpController.setLabelWarningText("Попробуйте позже");
                    break;
                case 1:
                    signUpController.setLabelWarningText("Вы успешно зарегистрированы");
                    break;
                case 2:
                    signUpController.setLabelWarningText("Пользователь с таким логином уже существует");
                    break;
                default:
                     signUpController.setLabelWarningText("Что-то пошло не так");
            }

        }

        if (typeOfMessage.equals(Const.AUTHORIZATION)){
            if (jsonMessage.get(Const.STATUS).getAsInt() == 1) {
                controller.setLabelWarningText("Вы вошли как " + controller.getLogin_field());
            } else
                controller.setLabelWarningText("Неверное имя пользователя или пароль");
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
