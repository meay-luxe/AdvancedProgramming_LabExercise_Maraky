package com.example.poker_game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("poker.fxml")
        );
        Scene scene = new Scene(loader.load(), 1200, 800);

        // Optional: add CSS
        scene.getStylesheets().add(
                getClass().getResource("poker.css").toExternalForm()
        );

        primaryStage.setTitle("Poker Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}