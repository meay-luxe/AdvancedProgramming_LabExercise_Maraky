package com.example.chatt.client;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ChatClientUI extends Application {

    private ChatClient chatClient;
    private TextArea chatArea;
    private TextField messageField;
    private TextField usernameField;
    private Button connectButton;
    private Button sendButton;
    private Label statusLabel;
    private boolean isConnected = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("💬 JavaFX Chat App");


        Label titleLabel = new Label("💬 Chat Application");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);

        HBox topBar = new HBox(titleLabel);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2C3E50;");


        usernameField = new TextField();
        usernameField.setPromptText("Enter your username...");
        usernameField.setPrefWidth(200);

        connectButton = new Button("Connect");
        connectButton.setStyle(
                "-fx-background-color: #27AE60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 8px 15px;" +
                        "-fx-cursor: hand;"
        );

        statusLabel = new Label("● Not Connected");
        statusLabel.setTextFill(Color.RED);
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        HBox connectionArea = new HBox(10,
                new Label("Username:"),
                usernameField,
                connectButton,
                statusLabel
        );
        connectionArea.setAlignment(Pos.CENTER_LEFT);
        connectionArea.setPadding(new Insets(10, 15, 10, 15));
        connectionArea.setStyle("-fx-background-color: #ECF0F1;");


        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setStyle("-fx-font-size: 13px;");
        VBox.setVgrow(chatArea, Priority.ALWAYS);


        messageField = new TextField();
        messageField.setPromptText("Type your message...");
        messageField.setDisable(true);
        HBox.setHgrow(messageField, Priority.ALWAYS);

        sendButton = new Button("Send ➤");
        sendButton.setDisable(true);
        sendButton.setStyle(
                "-fx-background-color: #2980B9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );

        HBox inputArea = new HBox(10, messageField, sendButton);
        inputArea.setPadding(new Insets(10, 15, 10, 15));
        inputArea.setAlignment(Pos.CENTER);
        inputArea.setStyle("-fx-background-color: #ECF0F1;");


        VBox mainLayout = new VBox(0, topBar, connectionArea, chatArea, inputArea);
        mainLayout.setPrefSize(600, 500);


        connectButton.setOnAction(e -> handleConnect());
        sendButton.setOnAction(e -> handleSendMessage());
        messageField.setOnAction(e -> handleSendMessage());

        primaryStage.setOnCloseRequest(e -> {
            if (chatClient != null) chatClient.disconnect();
            Platform.exit();
        });

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(400);
        primaryStage.show();
    }

    private void handleConnect() {
        if (!isConnected) {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                showAlert("Please enter a username!");
                return;
            }

            chatClient = new ChatClient(username, this::onMessageReceived);

            if (chatClient.connect()) {
                isConnected = true;
                statusLabel.setText("● Connected as: " + username);
                statusLabel.setTextFill(Color.GREEN);
                connectButton.setText("Disconnect");
                connectButton.setStyle(
                        "-fx-background-color: #E74C3C;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 13px;" +
                                "-fx-padding: 8px 15px;" +
                                "-fx-cursor: hand;"
                );
                usernameField.setDisable(true);
                messageField.setDisable(false);
                sendButton.setDisable(false);
                messageField.requestFocus();
            } else {
                showAlert("Cannot connect to server!\nMake sure ChatServer is running first.");
            }
        } else {
            chatClient.disconnect();
            isConnected = false;
            statusLabel.setText("● Not Connected");
            statusLabel.setTextFill(Color.RED);
            connectButton.setText("Connect");
            connectButton.setStyle(
                    "-fx-background-color: #27AE60;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 13px;" +
                            "-fx-padding: 8px 15px;" +
                            "-fx-cursor: hand;"
            );
            usernameField.setDisable(false);
            messageField.setDisable(true);
            sendButton.setDisable(true);
            appendMessage("❌ You have disconnected.");
        }
    }

    private void handleSendMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty() || !isConnected) return;
        chatClient.sendMessage(message);
        messageField.clear();
    }

    private void onMessageReceived(String message) {
        Platform.runLater(() -> appendMessage(message));
    }

    private void appendMessage(String message) {
        chatArea.appendText(message + "\n");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}