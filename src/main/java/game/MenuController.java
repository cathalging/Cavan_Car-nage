package game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MenuController {
    @FXML private Button newGameButton;
    @FXML private Button loadGameButton;
    @FXML private Button exitButton;

    public void onStartGame() throws IOException {
        SceneManager.switchTo("/game/gui.fxml");
    }

    public void onLoadGame() throws IOException {
        SceneManager.switchTo("/game/load.fxml");
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
