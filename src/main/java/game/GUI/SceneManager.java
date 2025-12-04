package game.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage mainStage;

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void switchTo(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent root = loader.load();

        StackPane container = new StackPane(root);
        container.setStyle("-fx-background-color: #181B9C;");


        double BASE_WIDTH = 600;
        double BASE_HEIGHT = 400;

        Scene scene = new Scene(container, BASE_WIDTH, BASE_HEIGHT);

        root.scaleXProperty().bind(scene.widthProperty().divide(BASE_WIDTH));
        root.scaleYProperty().bind(scene.heightProperty().divide(BASE_HEIGHT));

        mainStage.setScene(scene);
        mainStage.show();
    }
}

