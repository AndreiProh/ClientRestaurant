package com.example.clientrestaurant;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 830, 500);
        Controller controller = fxmlLoader.getController();
        stage.setTitle("Ресторан");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("assets/rest.png")));
        stage.setScene(scene);
        stage.show();
        ClientSocket socket = new ClientSocket("localhost",3345, controller);
        controller.setClientSocket(socket);
        controller.updateListOfDish();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                socket.close();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}