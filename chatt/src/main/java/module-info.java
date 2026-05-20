module com.example.chatt {
    requires javafx.controls;
    requires javafx.fxml;

    // Add all your packages here
    opens com.example.chatt.client to javafx.fxml;
    exports com.example.chatt.server;
    exports com.example.chatt.client;
}