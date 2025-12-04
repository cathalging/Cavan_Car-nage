package game.GUI;

import game.*;
import game.characters.Character;
import game.characters.*;
import game.io.Command;
import game.io.OutputController;
import game.io.Parser;
import game.items.Item;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Set;

public class GUIController {

    @FXML private TextArea effectArea;
    @FXML private ProgressBar healthBar;
    @FXML private TextArea inventoryArea;
    @FXML private VBox charactersBox;
    @FXML private Button northButton;
    @FXML private Button eastButton;
    @FXML private Button southButton;
    @FXML private Button westButton;
    @FXML private TextArea textArea;
    @FXML private TextField inputField;

    private final Parser parser = new Parser();
    private final OutputController outputController = new OutputController();
    private final Main game = Main.getGame();
    private final Player player = game.getPlayer();

    static int refreshes = 0;

    @FXML
    public void initialize() {
        inputField.setOnAction(e -> {
            String line = inputField.getText();
            inputField.clear();

            Command command = parser.getCommand(line);
            game.processCommand(command);

            refresh();
        });

        northButton.setOnAction(e -> {
            Command command = parser.getCommand("go north");
            game.processCommand(command);

            refresh();
        });

        eastButton.setOnAction(e -> {
            Command command = parser.getCommand("go east");
            game.processCommand(command);

            refresh();
        });

        southButton.setOnAction(e -> {
            Command command = parser.getCommand("go south");
            game.processCommand(command);

            refresh();
        });

        westButton.setOnAction(e -> {
            Command command = parser.getCommand("go west");
            game.processCommand(command);

            refresh();
        });

        refresh();
    }

    private void refresh() {
        textArea.setText(outputController.getText());

        if (refreshes >= 1)
            textArea.positionCaret(textArea.getText().length());

        refreshes++;


        // Character Box
        charactersBox.getChildren().clear();
        for (Character character: player.getCurrentRoom().getCharacters()) {
            charactersBox.getChildren().add(new Button(character.getName()));
        }

        for (Node node: charactersBox.getChildren()) {
            if (node instanceof Button button) {
                button.setOnAction(e -> {
                   inputField.appendText(button.getText());
                });
            }
        }

        // Inventory
        inventoryArea.clear();
        for (Item item: player.getInventory()) {
            inventoryArea.appendText(item.getName() + "\n");
        }

        // Health Bar
        healthBar.setProgress(player.getHealthPercentage());

        // Effects Box
        effectArea.clear();
        Set<Effect> effects = player.getEffects();
        for (Effect effect: effects) {
            effectArea.appendText(effect.name() + "\n");
        }
    }
}
