package com.example.clientrestaurant;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button authLogInButton;

    @FXML
    private Text authorizationText;

    @FXML
    private Button dishAddButton1;

    @FXML
    private Button buttonPay;

    @FXML
    private Button dishAddButton10;

    @FXML
    private Button dishAddButton11;

    @FXML
    private Button dishAddButton12;

    @FXML
    private Button dishAddButton2;

    @FXML
    private Button dishAddButton3;

    @FXML
    private Button dishAddButton4;

    @FXML
    private Button dishAddButton5;

    @FXML
    private Button dishAddButton6;

    @FXML
    private Button dishAddButton7;

    @FXML
    private Button dishAddButton8;

    @FXML
    private Button dishAddButton9;

    @FXML
    private Label dishLabel1;

    @FXML
    private Label dishLabel10;

    @FXML
    private Label dishLabel11;

    @FXML
    private Label dishLabel12;

    @FXML
    private Label dishLabel2;

    @FXML
    private Label dishLabel3;

    @FXML
    private Label dishLabel4;

    @FXML
    private Label dishLabel5;

    @FXML
    private Label dishLabel6;

    @FXML
    private Label dishLabel7;

    @FXML
    private Label dishLabel8;

    @FXML
    private Label dishLabel9;

    @FXML
    private TextField dishQuantityText1;

    @FXML
    private TextField dishQuantityText10;

    @FXML
    private TextField dishQuantityText11;

    @FXML
    private TextField dishQuantityText12;

    @FXML
    private TextField dishQuantityText2;

    @FXML
    private TextField dishQuantityText3;

    @FXML
    private TextField dishQuantityText4;

    @FXML
    private TextField dishQuantityText5;

    @FXML
    private TextField dishQuantityText6;

    @FXML
    private TextField dishQuantityText7;

    @FXML
    private TextField dishQuantityText8;

    @FXML
    private TextField dishQuantityText9;

    @FXML
    private Button dishSubButton1;

    @FXML
    private Button dishSubButton10;

    @FXML
    private Button dishSubButton11;

    @FXML
    private Button dishSubButton12;

    @FXML
    private Button dishSubButton2;

    @FXML
    private Button dishSubButton3;

    @FXML
    private Button dishSubButton4;

    @FXML
    private Button dishSubButton5;

    @FXML
    private Button dishSubButton6;

    @FXML
    private Button dishSubButton7;

    @FXML
    private Button dishSubButton8;

    @FXML
    private Button dishSubButton9;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private TextField login_field;

    @FXML
    private TextArea orderTextArea;

    @FXML
    private PasswordField password_field;

    @FXML
    private ScrollPane scrollPaneOrder;

    @FXML
    private VBox vBoxOrder;

    private Map<String, Integer> order;

    private ClientSocket clientSocket;

    public String getLogin_field() {
        return this.login_field.getText();
    }

    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public  void setLabelWarningText(String warningText) {
        Platform.runLater(() ->this.authorizationText.setText(warningText));
    }

    @FXML
    void initialize() {
        //Подключение обработчика нажатия кнопок "+" и "-"
        for (var node:anchorPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                System.out.println(button.getId());
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleButtonAction);
            }
        }
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

            buttonPay.setOnAction(actionEvent -> {
                payOrder();
            });



    }

    private void subtractDishFromOrderOnServer(int id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", Const.ORDER);
        jsonObject.addProperty(Const.STATUS, Const.SUBTRACT);
        jsonObject.addProperty(Const.ID_DISH, id);
        sendMsg(jsonObject);
    }

    private void addDishInOrderOnServer(int id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", Const.ORDER);
        jsonObject.addProperty(Const.STATUS, Const.ADD);
        jsonObject.addProperty(Const.ID_DISH, id);
        sendMsg(jsonObject);
    }

    private void addDishInOrderOnScreen(int currentValue, Label targetLabel) {
        String nameOfDish = targetLabel.getText();

    }

    private void payOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", Const.ORDER);
        jsonObject.addProperty(Const.STATUS, Const.CONFIRM);
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

    private void handleButtonAction(MouseEvent event) {

        Button sourceButton = (Button)event.getSource();
        String buttonId = sourceButton.getId();
        int numberCell = Integer.parseInt(buttonId.replaceAll("[^\\d]", ""));
        TextField targetTextField = (TextField)anchorPane.lookup("#dishQuantityText" + numberCell);
        Label targetLabel = (Label)anchorPane.lookup("#dishLabel" + numberCell);
        System.out.println("pressed " + numberCell);
        System.out.println(targetTextField.getId());
        if (targetTextField != null) {
            int currentValue = Integer.parseInt(targetTextField.getText());
            if (buttonId.contains("Add")) {
                addDishInOrderOnServer(numberCell + 1);
                addDishInOrderOnScreen(currentValue, targetLabel);
                targetTextField.setText(String.valueOf(currentValue + 1));

            } else if (currentValue != 0) {
                subtractDishFromOrderOnServer(numberCell + 1);
                targetTextField.setText(String.valueOf(currentValue - 1));
            }
        }
    }



}
