module com.example.clientrestaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.json;


    opens com.example.clientrestaurant to javafx.fxml;
    exports com.example.clientrestaurant;
}