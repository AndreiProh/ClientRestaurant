package com.example.clientrestaurant;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    public Button authLogInButton;

    @FXML
    private Text authorizationText;

    @FXML
    private Button dishAddButton1;

    @FXML
    public Button buttonPay;

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
    public VBox vBoxOrder;

    private double totalCostOrder;

    private Map<String,Integer> order;

    public List<Dish> listOfDishes;

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
        order = new HashMap<String,Integer>();

        //Кнопка "Войти":
            authLogInButton.setOnAction(actionEvent -> {
                String userName = login_field.getText().trim();
                String password = password_field.getText().trim();
                setLabelWarningText("");
                if (checkAllStringFilled(userName, password)) {
                    JsonObject jsonObject = new JsonObject();
                    if (authLogInButton.getText().equals("Войти")) {
                        jsonObject.addProperty("type", "authorization");
                    } else {
                        jsonObject.addProperty("type", "exit");
                    }
                    jsonObject.addProperty("username", userName);
                    jsonObject.addProperty("password", password);
                    ClientSocket.sendMessage(new Gson().toJson(jsonObject));
                } else
                    setLabelWarningText("Заполните все поля");

            });
        // Кнопка "Зарегистрироваться":
            loginSignUpButton.setOnAction(actionEvent -> {
                Stage mainStage = (Stage)loginSignUpButton.getScene().getWindow();
                mainStage.hide();
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
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        mainStage.show();
                    }
                });
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

    public void addDishInOrderOnScreen(int currentValue, Label targetLabel) {
        Label nameOfDish = new Label(targetLabel.getText());
        String name = nameOfDish.getText();
        nameOfDish.setFont(new Font("Arial",10));
        nameOfDish.setStyle("-fx-font-weight: bold;");
        nameOfDish.setStyle("-fx-text-fill: #FF4500;");
        boolean nameExist = false;
        int newValue = currentValue + 1;
        double price = getDishFromList(name).getPrice();
        String itemOfOrder = name + "       x" + newValue + "     " + price * newValue + "р.";
        for (Label label : vBoxOrder.getChildren().filtered(node -> node instanceof Label).toArray(new Label[0])) {
            if (label.getText().startsWith(nameOfDish.getText())) {
                label.setText(itemOfOrder);
                order.put(name, newValue);
                nameExist = true;
                break;
            }
        }
        if (!nameExist) {
            nameOfDish.setText(itemOfOrder);
            vBoxOrder.getChildren().add(nameOfDish);
            order.put(name, newValue);
        }
        totalCostOrder +=price;
        buttonPay.setText("Оплатить " + totalCostOrder + " руб.");

    }

    private Dish getDishFromList(String name) {
        Dish dish = new Dish();
        for (Dish item : listOfDishes) {
            if (item.getName().equals(name)) {
                dish = item;
                break;
            }
        }
        return dish;
    }

    private void subtractDishFromOrderOnScreen(Label targetLabel) {
        String dishName = targetLabel.getText();
        double price = getDishFromList(dishName).getPrice();
        for (Label label : vBoxOrder.getChildren().filtered(node -> node instanceof Label).toArray(new Label[0])) {
            if (label.getText().startsWith(dishName)) {
                // Извлекаем текущее количество
                int currentValue = order.get(dishName);
                // Уменьшаем количество на 1
                currentValue--;

                if (currentValue > 0) {
                    // Обновляем текст с новым количеством
                    label.setText(dishName + "      x" + currentValue + "     " + price * currentValue + " р.");
                    order.put(dishName, currentValue);
                } else {
                    // Удаляем элемент, если количество равно нулю
                    vBoxOrder.getChildren().remove(label);
                    order.remove(dishName);
                }
                break;
            }
        }
        totalCostOrder -= price;
        buttonPay.setText("Оплатить " + totalCostOrder + " руб.");
    }


    private void payOrder() {

        if (!order.isEmpty()) {
            for (Map.Entry<String,Integer> entry: order.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue() + "  ");
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", Const.ORDER);
            jsonObject.addProperty(Const.STATUS, Const.CONFIRM);
            JsonElement jsonElementOrder = new Gson().toJsonTree(order);
            jsonObject.add("dishes",jsonElementOrder);
            sendMsg(jsonObject);
            totalCostOrder = 0;
            buttonPay.setDisable(true);
        }

    }

    private void sendMsg(JsonObject jsonObject) {
        System.out.println(jsonObject);
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
                //addDishInOrderOnServer(numberCell + 1);
                addDishInOrderOnScreen(currentValue, targetLabel);
                targetTextField.setText(String.valueOf(currentValue + 1));

            } else if (currentValue != 0) {
                //subtractDishFromOrderOnServer(numberCell + 1);
                subtractDishFromOrderOnScreen(targetLabel);
                targetTextField.setText(String.valueOf(currentValue - 1));
            }
        }
    }
    public  void updateListOfDish() {
        System.out.println("updateListOfDish");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "update");
        sendMsg(jsonObject);
    }

    public void populateScrollPaneWithDishes(List<Dish> dishes) {
        // Очистка AnchorPane
        anchorPane.getChildren().clear();

        // Создание вертикального ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);

        // Установка размера ScrollPane
        scrollPane.setPrefSize(anchorPane.getPrefWidth(), anchorPane.getPrefHeight());

        // Создание GridPane для размещения контейнеров
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Размеры контейнера (1/4 от ширины AnchorPane, с учетом отступов)
        double containerSize = anchorPane.getPrefWidth() / 4 - 20; // Учитывая отступы

        // Создание и добавление контейнеров на GridPane
        int col = 0;
        int row = 0;
        for (Dish dish : dishes) {
            VBox dishContainer = createDishContainer(dish);

            // Установка размера контейнера
            dishContainer.setPrefSize(containerSize, containerSize);

            // Добавление границ к контейнеру
            //dishContainer.setStyle("-fx-border-color: #FF4500; -fx-border-width: 1px;");

            // Добавление контейнера на GridPane
            gridPane.add(dishContainer, col, row);

            // Обновление координат для размещения контейнеров по 4 в ряд
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        // Добавление GridPane в ScrollPane
        scrollPane.setContent(gridPane);

        // Добавление ScrollPane на AnchorPane
        anchorPane.getChildren().add(scrollPane);
        anchorPane.layout();
    }


    public VBox createDishContainer(Dish dish) {
        // Создание Label с названием блюда
        Label dishLabel = new Label(dish.getName());
        dishLabel.setId("dishLabel" + dish.getId());
        dishLabel.getStyleClass().add("dishLabel");
        dishLabel.applyCss();
        dishLabel.setWrapText(true);  // Включаем перенос текста
        dishLabel.setMinHeight(60);

        Tooltip tooltip = new Tooltip(Double.toString(dish.getPrice()));
        Tooltip.install(dishLabel, tooltip);

        // Создание кнопок и текстового поля
        Button leftButton = new Button("-");
        leftButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleButtonAction);
        leftButton.setId("dishSubButton" + dish.getId());
        leftButton.getStyleClass().add("leftButton");

        TextField textField = new TextField("0"); // Здесь можно установить начальное значение
        textField.setId("dishQuantityText" + dish.getId());
        textField.setEditable(false);
        textField.getStyleClass().add("textField");

        Button rightButton = new Button("+");
        rightButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleButtonAction);
        rightButton.setId("dishAddButton" + dish.getId());
        rightButton.getStyleClass().add("rightButton");

        // Создание HBox, который будет содержать кнопки и текстовое поле
        HBox hBox = new HBox(leftButton, textField, rightButton);
        hBox.getStyleClass().add("container");
        hBox.setSpacing(0);
        hBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Создание VBox, который будет содержать Label и HBox
        VBox vBox = new VBox(dishLabel, hBox);
        vBox.setSpacing(5);
        vBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Установка стилей для VBox
        vBox.setPrefHeight(50);
        vBox.setPrefWidth(120);

        return vBox;
    }

    public void exitConfirmed() {
        this.login_field.setText("");
        this.password_field.setText("");
        this.authLogInButton.setText("Войти");
        this.vBoxOrder.getChildren().clear();
    }
    public void resetTextFields(Parent parent) {
        System.out.println("IN RESET TEXT");
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                if (textField.getId().startsWith("dishQuantityText")) {
                    textField.setText("0");
                }
            } else if (node instanceof Parent) {
                resetTextFields((Parent) node);
            }
        }
    }
    public void orderConfirmed() {
        vBoxOrder.getChildren().clear();
        vBoxOrder.getChildren().add(new Label("Ваш заказ принят"));
        buttonPay.setText("Оплатить");
        buttonPay.setDisable(false);
        order.clear();
        resetTextFields(anchorPane);
    }




}
