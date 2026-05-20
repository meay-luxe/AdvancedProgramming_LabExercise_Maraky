package com.example.demofvgh;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;

public class Controller {


    @FXML
    private TextArea textArea;


    private File currentFile = null;


    @FXML
    public void handleOpen() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );


        Stage stage = (Stage) textArea.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {

                String content = Files.readString(file.toPath());

                textArea.setText(content);

                currentFile = file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleSave() {

        if (currentFile != null) {
            saveToFile(currentFile);
        } else {

            handleSaveAs();
        }
    }

    @FXML
    public void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        Stage stage = (Stage) textArea.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            saveToFile(file);

            currentFile = file;
        }
    }



    @FXML
    public void handleCopy() {
        textArea.copy();
    }

    @FXML
    public void handlePaste() {
        textArea.paste();
    }

    @FXML
    public void handleCut() {
        textArea.cut();
    }

    @FXML
    public void handleDelete() {

        textArea.clear();
    }



    private void saveToFile(File file) {
        try {

            FileWriter writer = new FileWriter(file);
            writer.write(textArea.getText());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}