module com.example.poker_game {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.poker_game to javafx.fxml;
    exports com.example.poker_game;
}