// Old code
/*package game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {

    private OutputController outputController;
    private Stage primaryStage;
    private TextArea textArea;
    private Parser parser;
    private Main game;

    public GUI() {
        parser = new Parser();
        outputController = new OutputController();
        game = Main.getGame();
    }


    public void run() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Text Game GUI Test");

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        TextField inputField = new TextField();
        inputField.setOnAction(e -> {
            String line = inputField.getText();
            inputField.clear();

            Command command = parser.getCommand(line);
            game.processCommand(command);

            refresh();
        });

        VBox root = new VBox(textArea, inputField);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        refresh();
    }

    public void refresh() {
        if (textArea != null && outputController!= null) {
            Platform.runLater(() -> textArea.setText(outputController.getText()));
        }
    }
}
*/
package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/game/gui.fxml")
        );

        Scene scene = new Scene(loader.load(), 600, 400);

        stage.setTitle("Cavan Car-nage");
        stage.setScene(scene);
        stage.show();
    }

    public void run() {
        launch();
    }
}
