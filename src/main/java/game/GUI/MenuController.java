package game.GUI;

import game.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class MenuController {
    @FXML private Button newGameButton;
    @FXML private Button loadGameButton;
    @FXML private Button exitButton;

    public void onStartGame() throws IOException {
        Main.getGame().newGame();
        SceneManager.switchTo("/game/gui.fxml");
    }

    public void onLoadGame() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        // Optional: set extension filters
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extFilter);

        Window ownerWindow = loadGameButton.getScene().getWindow();

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(ownerWindow);
        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("No file selected");
        }

        Main.getGame().loadGame(selectedFile);
        SceneManager.switchTo("/game/gui.fxml");
    }

    @FXML
    public void initialize() {
        newGameButton.setOnAction(event -> {
            try {
                onStartGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        loadGameButton.setOnAction(event -> {
            try {
                onLoadGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        exitButton.setOnAction(event -> {
            Platform.exit();
        });
    }
}
