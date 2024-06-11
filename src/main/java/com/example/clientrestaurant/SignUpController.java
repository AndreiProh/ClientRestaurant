package com.example.clientrestaurant;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONObject;


public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Label labelWarningText;

    @FXML
    private TextField signUpTextField;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpLastName;

    @FXML
    private PasswordField signupPasswordField;

    @FXML
    private Button signupSendButton;

    @FXML
    void initialize() {
        //Действие для кнопки "Отправить"
        signupSendButton.setOnAction(actionEvent -> {
            signUpNewUser();
        });

    }

    private void signUpNewUser() {
        //DatabaseHandler dbHandler = new DatabaseHandler();
        //Считываем данные с окна регистрации
        String userName = signUpTextField.getText();
        String lastName = signUpLastName.getText();
        String firstName = signUpFirstName.getText();
        String password = signupPasswordField.getText();

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", "authorization");
        jsonObject.addProperty("username", userName);
        jsonObject.addProperty("lastname", lastName);
        jsonObject.addProperty("firstname", firstName);
        jsonObject.addProperty("password", password);
        ClientSocket.sendMessage(new Gson().toJson(jsonObject));



    }


}
