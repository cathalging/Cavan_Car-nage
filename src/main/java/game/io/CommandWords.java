package game.io;

import java.util.HashMap;
import java.util.Map;

public class CommandWords {
    private static Map<String, String> validCommands;

    public CommandWords() {
        validCommands = new HashMap<>();
        validCommands.put("go", "Move to another room");
        validCommands.put("move", "Move to another room");

        validCommands.put("quit", "End the game");
        validCommands.put("help", "Show help");

        validCommands.put("consume", "Consume an item");
        validCommands.put("drink", "Consume an item");
        validCommands.put("eat", "Consume an item");

        validCommands.put("take", "Pick up an item");
        validCommands.put("pickup", "Pick up an item");

        validCommands.put("place", "Place an item in current room");
        validCommands.put("drop", "Place an item in current room");

        validCommands.put("inventory", "Show your inventory");

        validCommands.put("hit", "Hit a fella");
        validCommands.put("fight", "Hit a fella");

        validCommands.put("talk", "Talk to someone");
        validCommands.put("speak", "Talk to someone");

        validCommands.put("use", "Use something");

        validCommands.put("drive", "Drive your car");

        validCommands.put("shop", "Shows the shop items");
        validCommands.put("buy", "Buy an item");

        validCommands.put("trade", "Trade an item");
        validCommands.put("Offer", "Offer an item for trade");

        validCommands.put("save", "Saves the game");
    }

    public boolean isCommand(String commandWord) {
        return validCommands.containsKey(commandWord);
    }

    public void showAll() {
        OutputController outputController = new OutputController();
        outputController.addText("Valid commands are: \n");
        for (String command : validCommands.keySet()) {
            outputController.addText(command + ": " + validCommands.get(command));
        }
    }

    public static void removeWords() {
        validCommands.clear();
        validCommands.put("quit", "Exit the game");
    }
}
