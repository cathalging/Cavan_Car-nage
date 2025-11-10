import java.util.HashMap;
import java.util.Map;

public class CommandWords {
    private Map<String, String> validCommands;

    public CommandWords() {
        validCommands = new HashMap<>();
        validCommands.put("go", "Move to another room");
        validCommands.put("quit", "End the game");
        validCommands.put("help", "Show help");
        validCommands.put("consume", "Consume an Item");
        validCommands.put("take", "Pick up an item");
        validCommands.put("place", "Place an item in current room");
        validCommands.put("inventory", "Show your inventory");
        validCommands.put("hit", "Hit a fella");
    }

    public boolean isCommand(String commandWord) {
        return validCommands.containsKey(commandWord);
    }

    public void showAll() {
        System.out.print("Valid commands are: ");
        for (String command : validCommands.keySet()) {
            System.out.print(command + " ");
        }
        System.out.println();
    }
}
