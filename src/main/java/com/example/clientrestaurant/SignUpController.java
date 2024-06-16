package com.example.clientrestaurant;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
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
    private  Label labelWarningText;

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

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public  void setLabelWarningText(String warningText) {
        Platform.runLater(() ->labelWarningText.setText(warningText));
    }

    @FXML
    void initialize() {
        //Действие для кнопки "Отправить"
        signupSendButton.setOnAction(actionEvent -> {
            //labelWarningText.setText("нажата");
            signUpNewUser();
        });

    }

    private void signUpNewUser() {
        //DatabaseHandler dbHandler = new DatabaseHandler();
        //Считываем данные с окна регистрации
        String userName = signUpTextField.getText().trim();
        String lastName = signUpLastName.getText().trim();
        String firstName = signUpFirstName.getText().trim();
        String password = signupPasswordField.getText().trim();
        //Проверяем все ли поля заполнены.
        if(checkAllStringFilled(userName, lastName, firstName, password)) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("type", "logup");
            jsonObject.addProperty("username", userName);
            jsonObject.addProperty("lastname", lastName);
            jsonObject.addProperty("firstname", firstName);
            jsonObject.addProperty("password", password);

            ClientSocket.sendMessage(new Gson().toJson(jsonObject));
        } else
            labelWarningText.setText("Заполните все поля");
    }

    private boolean checkAllStringFilled(String... strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty())
                return false;
        }
        return true;
    }


}
