/* This game is a classic text-based adventure set in a university environment.
   The player starts outside the main entrance and can navigate through different rooms like a 
   lecture theatre, campus pub, computing lab, and admin office using simple text commands (e.g., "go east", "go west").
    The game provides descriptions of each location and lists possible exits.

Key features include:
Room navigation: Moving among interconnected rooms with named exits.
Simple command parser: Recognizes a limited set of commands like "go", "help", and "quit".
Player character: Tracks current location and handles moving between rooms.
Text descriptions: Provides immersive text output describing the player's surroundings and available options.
Help system: Lists valid commands to guide the player.
Overall, it recreates the classic Zork interactive fiction experience with a university-themed setting, 
emphasizing exploration and simple command-driven gameplay
*/
import java.util.ArrayList;

public class Main {
    private Parser parser;
    private Character player;
    private NPC npc;

    private Room pub, mainstreet, heaven;
    private Item stick;
    private Consumable pint;

    public Main() {
        createRooms();
        createCharacters();
        createObjects();
        parser = new Parser();
    }

    public void createObjects() {
        pint = new Consumable("Pint", "A creamy pint of Guinness", pub, Character.Effect.LANGERS);
        stick = new Item("Stick", "Brown and sticky", npc);
    }

    private void createRooms() {
        // create rooms
        mainstreet = new Room("outside the main entrance of the university");
        pub = new Room("in the campus pub");
        heaven = new Room("in heaven");

        // initialise room exits
        mainstreet.setExit(Character.Direction.WEST, pub);

        pub.setExit(Character.Direction.EAST, mainstreet);
    }

    public void createCharacters() {
        // create the player character and start outside
        player = new Character("player", mainstreet, 100, 10);

        npc = new NPC("npc", pub, 100, 10, stick);
        pub.addCharacter(npc);
    }
    public void play() {
        printWelcome();

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Goodbye.");
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the University adventure!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    private boolean processCommand(Command command) {
        String commandWord = command.getCommandWord();

        if (commandWord == null) {
            System.out.println("I don't understand your command... ");
            return false;
        }

        switch (commandWord) {
            case "help":
                printHelp();
                break;
            case "go", "move":
                goRoom(command, player);
                break;
            case "take":
                takeItem(command);
                break;
            case "place", "drop":
                placeItem(command);
                break;
            case "inventory":
                showInventory();
                break;
            case "consume", "drink", "eat":
                consumeItem(command);
                break;
            case "hit", "fight":
                attack(command, player);
                break;
            case "quit":
                if (command.hasSecondWord()) {
                    System.out.println("Quit what?");
                    return false;
                } else {
                    return true; // signal to quit
                }
            default:
                System.out.println("I don't know what you mean...");
                break;
        }
        return false;
    }

    private void attack(Command command, Character character) {
        if (!command.hasSecondWord()) {
            System.out.println("Attack what?");
            return;
        }

        String opponentName = command.getSecondWord();
        ArrayList<Character> roomCharacters = character.getCurrentRoom().getCharacters();

        Character opponent = null;
        for (Character roomCharacter : roomCharacters) {
            if (roomCharacter.getName().equals(opponentName)) {
                opponent = roomCharacter;
            }
        }
        if (opponent == null) {
            System.out.println("Character " + opponentName + " does not exist");
            return;
        }
        opponent.takeHit(character.getDamage());
        if (opponent.getHealth() <= 0) {
            opponent.dropInventory();

            if (opponent instanceof  NPC) {
                System.out.println(opponent.getName() + ": " + ((NPC) opponent).getDeathMessage());
                opponent.setCurrentRoom(heaven);
            }
            System.out.println(opponent.getName() + " has died.");
            return;
        }
        System.out.println("Opponent Health: " + opponent.getHealth());

        if (opponent instanceof NPC) {
            ((NPC) opponent).onHit(character);
            System.out.println(opponent.getName() + " attacked you for " + opponent.getDamage());
        }
        System.out.println("Your health: " + character.getHealth());
    }

    private void consumeItem(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Consume what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item[] items = player.getInventory().toArray(new Item[0]);

        for (Item item: items) {
            if (item.getName().equalsIgnoreCase(itemName) && item instanceof Consumable consumable) {
                if (player.addEffect(consumable.getEffect())) {
                    player.removeInventoryItem(item);
                    System.out.println("You now have the following effect:");
                    System.out.println(Character.effectDescription.get(consumable.getEffect()));
                    return;
                }
                System.out.println("You already have this items effect.");
                return;
            }
        }
        System.out.println("Item not found.\n");
    }

    private void showInventory() {
        ArrayList<Item> items = player.getInventory();
        System.out.println("Inventory:");
        for (Item item: items.toArray(new Item[0])) {
            System.out.println(item.getName() + ":\n" + item.getDescription());
        };
    }

    private void placeItem(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Place what?");
            return;
        }
        String itemName = command.getSecondWord();
        Room currentRoom = player.getCurrentRoom();

        ArrayList<Item> items = player.getInventory();

        for (Item item: items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                player.removeInventoryItem(item);
                currentRoom.addItem(item);
                System.out.println("The " + itemName + " was placed " + currentRoom.getDescription());
                return;
            }
        }
        System.out.println("Item not found.");
    }

    private void takeItem(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        }
        String itemName = command.getSecondWord();

        ArrayList<Item> items = player.getCurrentRoom().getItems();

        for (Item item: items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                player.addInventoryItem(item);
                player.getCurrentRoom().removeItem(item);
                System.out.println("You took the " + item.getName());
                return;
            }
        }
        System.out.println("Item not found.");
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander around the university.");
        System.out.print("Your command words are: ");
        parser.showCommands();
        System.out.println();
    }

    private void goRoom(Command command, Character character) {
        String directionStr;

        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        Character.Direction direction;
        directionStr = command.getSecondWord().toLowerCase();

        switch (directionStr) {
            case "north":
                direction = Character.Direction.NORTH;
                break;
            case "south":
                direction = Character.Direction.SOUTH;
                break;
            case "east":
                direction = Character.Direction.EAST;
                break;
            case "west":
                direction = Character.Direction.WEST;
                break;
            default:
                direction = null;
        }

        if (direction == null) {
            System.out.println("There is no door!\n");
        } else {
            character.move(direction);
            System.out.println(character.getCurrentRoom().getLongDescription());
            if (!character.getEffects().isEmpty()) {
                System.out.println("Your Current Effects:");
                for (Character.Effect effect: character.getEffects()) {
                    System.out.println(Character.effectDescription.get(effect));
                    System.out.println();
                }

            }
        }
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.play();
    }
}
