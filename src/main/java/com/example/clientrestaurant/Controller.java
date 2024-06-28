package com.example.clientrestaurant;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button authLogInButton;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Label warningText;

    @FXML
    private TextArea orderTextArea;

    @FXML
    private Text authorizationText;

    @FXML
    private Button dish1AddButton;

    @FXML
    private TextField dish1QuantityText;

    @FXML
    private Button dish1SubButton;

    @FXML
    private Button dish2AddButton;

    @FXML
    private TextField dish2QuantityText;

    @FXML
    private Button dish2SubButton;

    private ClientSocket clientSocket;

    public String getLogin_field() {
        return this.login_field.getText();
    }

    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public  void setLabelWarningText(String warningText) {
        Platform.runLater(() ->this.warningText.setText(warningText));
    }

    @FXML
    void initialize() {
        //Кнопка "Войти":
            authLogInButton.setOnAction(actionEvent -> {
                String userName = login_field.getText().trim();
                String password = password_field.getText().trim();
                setLabelWarningText("");

                if (checkAllStringFilled(userName, password)) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("type", "authorization");
                    jsonObject.addProperty("username", userName);
                    jsonObject.addProperty("password", password);
                    ClientSocket.sendMessage(new Gson().toJson(jsonObject));
                } else
                    setLabelWarningText("Заполните все поля");
            });
        // Кнопка "Зарегистрироваться":
            loginSignUpButton.setOnAction(actionEvent -> {
                loginSignUpButton.getScene().getWindow().hide();
                FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("signup.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                scene.setRoot(root);
                stage.setScene(scene);
                stage.setTitle("Регистрация");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("assets/rest.png")));
                SignUpController signUpController = loader.getController();
                signUpController.setController(this);
                clientSocket.setSignUpController(signUpController);
                stage.showAndWait();
            });

            dish1AddButton.setOnAction(actionEvent -> {
                addDishInOrder(1);
                int quantity = Integer.parseInt(dish1QuantityText.getText());
                quantity++;
                dish1QuantityText.setText("" + quantity);
            });
            dish1SubButton.setOnAction(actionEvent -> {
                subtractDishFromOrder(1);
                int quantity = Integer.parseInt(dish1QuantityText.getText());
                if (quantity != 0) {
                    quantity--;
                    dish1QuantityText.setText("" + quantity);
                }
            });

    }

    private void subtractDishFromOrder(int id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", Const.ORDER);
        jsonObject.addProperty(Const.STATUS, Const.SUBTRACT);
        jsonObject.addProperty(Const.ID_DISH, id);
        sendMsg(jsonObject);
    }

    private void addDishInOrder(int id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", Const.ORDER);
        jsonObject.addProperty(Const.STATUS, Const.ADD);
        jsonObject.addProperty(Const.ID_DISH, id);
        sendMsg(jsonObject);
    }

    private void sendMsg(JsonObject jsonObject) {
        ClientSocket.sendMessage(new Gson().toJson(jsonObject));
    }

    private boolean checkAllStringFilled(String... strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty())
                return false;
        }
        return true;
    }

}
