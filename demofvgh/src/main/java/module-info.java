module com.example.demofvgh {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demofvgh to javafx.fxml;
    exports com.example.demofvgh;
}