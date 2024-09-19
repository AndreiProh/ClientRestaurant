package com.example.clientrestaurant;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
                Platform.runLater(() -> controller.setLabelWarningText("Сервер недоступен"));
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
            int statusOfUser = jsonMessage.get(Const.STATUS).getAsInt();
            if (statusOfUser != 0) {
                if (statusOfUser == 1) {
                    controller.setLabelWarningText("Вы вошли как " + controller.getLogin_field());
                }
                if (statusOfUser == 2) {
                    controller.setLabelWarningText("Вы вошли как Администратор(" + controller.getLogin_field() + ")");
                    JsonArray jsonArray = jsonMessage.getAsJsonArray("orders");
                    Type orderListType = new TypeToken<ArrayList<OrderDTO>>() {}.getType();
                    Platform.runLater(() ->
                            controller.populateScrollPaneWithOrders(new Gson().fromJson(jsonArray, orderListType)));
                }
                if (statusOfUser == 3) {
                    controller.setLabelWarningText("Вы вошли как Курьер (" + controller.getLogin_field() + ")");
                    JsonArray jsonArray = jsonMessage.getAsJsonArray("deliveries");
                    Type deliveryListType = new TypeToken<ArrayList<Delivery>>() {}.getType();
                    Platform.runLater(() ->
                            controller.populateScrollPaneWithDeliveries(new Gson().fromJson(jsonArray, deliveryListType)));
                }
                Platform.runLater(()->controller.authLogInButton.setText("Выйти"));
            } else
                controller.setLabelWarningText("Неверное имя пользователя или пароль");
        }
        if (typeOfMessage.equals(Const.ORDER)) {
            if (jsonMessage.get("status").getAsInt() == 1) {
                Platform.runLater(() -> controller.orderConfirmed());
            }
        }
        if (typeOfMessage.equals("update")) {
            System.out.println("IN UPDATE");
            controller.listOfDishes = new ArrayList<>();
            JsonArray jsonArray = jsonMessage.getAsJsonArray("dishes");
            Type productListType = new TypeToken<ArrayList<Dish>>() {}.getType();
            controller.listOfDishes = new Gson().fromJson(jsonArray,productListType);
            for (Dish dish: controller.listOfDishes) {
                System.out.println(dish);
            }
            Platform.runLater(() -> controller.populateScrollPaneWithDishes(controller.listOfDishes));
        }
        if (typeOfMessage.equals("exit")) {
            Platform.runLater(() -> controller.exitConfirmed());
        }
    }


    public static synchronized void sendMessage(String message) {
        System.out.println(message);
        messageBuffer = message;
    }

//    private void orderConfirmed() {
//        controller.vBoxOrder.getChildren().clear();
//        controller.vBoxOrder.getChildren().add(new Label("Ваш заказ принят"));
//        controller.buttonPay.setText("Оплатить");
//        controller.buttonPay.setDisable(false);
//        controller.buttonPay.setText("Оплатить");
//        controller.buttonPay.setDisable(false);
//    }


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
