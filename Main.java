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

import java.io.*;
import java.util.ArrayList;

public class Main {
    private Parser parser;
    private Character player;
    private NPC oldMan, shopKeeper;

    private Room pubFront, pubBack, mainstreet, postOffice, church, townSquare, townHall, shop, park, river, cave, estate, abandonedCottage, forest, heaven;
    private Item engineOil;
    private Consumable pint;
    private Part keys, wheels, headlights, sparkPlug;

    private ShopKeeper shopController = new ShopKeeper();
    File file = new File("player.ser");

    public Main() {
        createRooms();
        createCharacters();
        createObjects();
        parser = new Parser();
    }

    public void createObjects() {
        // Consumables
        pint = new Consumable("Pint", "A creamy pint of Guinness", pubFront, Effect.LANGERS);

        // Shop Items
        shopController.addShopItems(new Item("Engine Oil", "A bottle of engine oil from 1977", 5));

        // Parts
        keys = new Part("Keys", "The keys to your car", shop);
        wheels = new Part("Wheels", "The wheels on your car go around and around", shop);
        headlights = new Part("Headlights", "The headlights for your car", shop);
        sparkPlug = new Part("Spark Plug", "A spark plug for your car", shop);
    }

    private void createRooms() {
        // create rooms
        postOffice = new Room("Post Office", "inside the town's small post office");
        mainstreet = new Room("Main Street", "walking along the town's main street");
        church = new Room("Church", "standing inside a quiet stone church");
        pubFront = new Room("Pub Front", "at the lively front of the pub");
        pubBack = new Room("Pub Back", "in the dim back room of the pub");
        townSquare = new Room("Town Square", "in the town square with its central fountain");
        townHall = new Room("Town Hall", "inside the town hall with tall pillars");
        shop = new Room("Shop", "in the general shop full of supplies");
        park = new Room("Park", "in the peaceful green park");
        estate = new Room("Estate", "on the grounds of an old estate");
        cave = new Room("Cave", "inside a dark echoing cave");
        abandonedCottage = new Room("Abandoned Cottage", "near an abandoned, vine-covered cottage");
        river = new Room("River", "beside a flowing river and footbridge");
        forest = new Room("Forest", "within a dense, towering forest");


        // Set Exits
        postOffice.setExit(Character.Direction.SOUTH, mainstreet);
        mainstreet.setExit(Character.Direction.NORTH, postOffice);

        mainstreet.setExit(Character.Direction.WEST, church);
        church.setExit(Character.Direction.EAST, mainstreet);

        mainstreet.setExit(Character.Direction.SOUTH, townSquare);
        townSquare.setExit(Character.Direction.NORTH, mainstreet);

        mainstreet.setExit(Character.Direction.EAST, pubFront);
        pubFront.setExit(Character.Direction.WEST, mainstreet);

        pubFront.setExit(Character.Direction.EAST, pubBack);
        pubBack.setExit(Character.Direction.WEST, pubFront);

        townSquare.setExit(Character.Direction.WEST, townHall);
        townHall.setExit(Character.Direction.EAST, townSquare);

        townSquare.setExit(Character.Direction.EAST, shop);
        shop.setExit(Character.Direction.WEST, townSquare);

        townSquare.setExit(Character.Direction.SOUTH, park);
        park.setExit(Character.Direction.NORTH, townSquare);

        park.setExit(Character.Direction.WEST, estate);
        estate.setExit(Character.Direction.EAST, park);

        estate.setExit(Character.Direction.SOUTH, abandonedCottage);
        abandonedCottage.setExit(Character.Direction.NORTH, estate);

        park.setExit(Character.Direction.SOUTH, river);
        river.setExit(Character.Direction.NORTH, park);

        park.setExit(Character.Direction.EAST, cave);
        cave.setExit(Character.Direction.WEST, park);

        cave.setExit(Character.Direction.SOUTH, forest);
        forest.setExit(Character.Direction.NORTH, cave);

        river.setExit(Character.Direction.EAST, forest);
        forest.setExit(Character.Direction.WEST, river);
    }

    public void createCharacters() {
        // Player
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("player.ser"))) {
                player = (Character) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            player = new Character("player", townSquare, 100, 50);
        }

        // NPCS
        oldMan = new OldMan("Old Man", townSquare, 100, 10);
        townSquare.addCharacter(oldMan);

        shopKeeper = new ShopKeeper("Shop Keeper", shop, 100000, 10000);
        shop.addCharacter(shopKeeper);
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

    private void savePlayer() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("player.ser"))) {
            out.writeObject(player);
            System.out.println("Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Your car splutters to a stop in the middle of Cavan Town. You look at the dash and see a light to change engine oil. Thankfully you see a shop to the east.");
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
            case "take", "pickup":
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
            case "talk", "speak":
                talk(command);
                break;
            case "use":
                use(command);
                break;
            case "drive":
                drive(command);
                break;
            case "shop":
                shop();
                break;
            case "buy":
                buy(command);
                break;
            case "save":
                savePlayer();
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

    private void buy(Command command) {
        if (!player.getCurrentRoom().equals(shop)) {
            System.out.println("You are not in the shop.");
            return;
        }

        if (!command.hasSecondWord()) {
            System.out.println("Buy what?");
            return;
        }

        String itemName = command.getSecondWord();
        ArrayList<Item> items = shopController.getShopItems();

        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                if (player.getMoney() >= item.getValue()) {
                    player.setMoney(player.getMoney() - item.getValue());
                    player.addInventoryItem(item);
                    System.out.println("You bought " + item.getName() + " for " + item.getValue());
                } else {
                    System.out.println("You don't have enough money.");
                }
            } else {
                System.out.println("Item does not exist.");
            }
        }
    }

    private void shop() {
        if (player.getCurrentRoom().equals(shop)) {
            System.out.println("Item:\t\tPrice:");
            for (Item item: ((ShopKeeper) shopKeeper).getShopItems()) {
                System.out.println(item.getName() + "\t" + item.getValue());
            }
        } else {
            System.out.println("You are not in the shop.");
        }
    }

    private void drive(Command command) {
        if (!player.getCurrentRoom().equals(townSquare)) {
            System.out.println("Your car is in the town square.");
            return;
        }
        if (!Car.fixed()) {
            System.out.println("Your car is still missing parts.");
            return;
        }
        System.out.println("You start your car, and escape from Cavan. You win.");
    }

    private void use(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Use what?");
            return;
        }

        String itemName = command.getSecondWord().toLowerCase();
        ArrayList<Item> playerItems = player.getInventory();

        Item item = null;
        for (Item invItem : playerItems) {
            if (invItem.getName().toLowerCase().equals(itemName)) {
                item = invItem;
            }
        }

        if (item == null) {
            System.out.println("Item not found");
        } else if (item instanceof Consumable) {
            consumeItem(command);
        } else if (item instanceof Part) {
            if (player.getCurrentRoom().equals(townSquare)) {
                Car.addPart(item);
                System.out.println("You put the " + item.getName() + " in your car.");
            } else {
                System.out.println("Your car is in the town square");
            }
        }
    }

    private void attack(Command command, Character character) {
        if (!command.hasSecondWord()) {
            System.out.println("Attack what?");
            return;
        }

        String opponentName = command.getSecondWord().toLowerCase();
        ArrayList<Character> roomCharacters = character.getCurrentRoom().getCharacters();

        Character opponent = null;
        for (Character roomCharacter : roomCharacters) {
            if (roomCharacter.getName().toLowerCase().equals(opponentName)) {
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

            if (opponent instanceof NPC) {
                System.out.println(opponent.getName() + ": " + ((NPC) opponent).getDeathMessage());
                opponent.setCurrentRoom(heaven);
            }
            System.out.println(opponent.getName() + " has died.");
            character.getCurrentRoom().removeCharacter(opponent);
            return;
        }

        System.out.println("Opponent Health: " + opponent.getHealth());

        if (opponent instanceof NPC) {
            ((NPC) opponent).onHit(character);
            System.out.println(opponent.getName() + " attacked you for " + opponent.getDamage());
        }
        System.out.println("Your health: " + character.getHealth());
    }

    private void talk(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Talk to who?");
            return;
        }

        String npcName = command.getSecondWord().toLowerCase();
        ArrayList<Character> roomCharacters = player.getCurrentRoom().getCharacters();

        Character npc = null;
        for (Character roomCharacter : roomCharacters) {
            if (roomCharacter.getName().toLowerCase().equals(npcName)) {
                npc = roomCharacter;
            }
        }
        if (npc == null) {
            System.out.println(npcName + " does not exist");
            return;
        }

        if (npc instanceof NPC) {
            System.out.println(((NPC) npc).getDialogue());
        }
    }

    private void consumeItem(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Consume what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item[] items = player.getInventory().toArray(new Item[0]);

        for (Item item : items) {
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
        for (Item item : items.toArray(new Item[0])) {
            System.out.println(item.getName() + ":\n" + item.getDescription());
        }
    }

    private void placeItem(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Place what?");
            return;
        }
        String itemName = command.getSecondWord();
        Room currentRoom = player.getCurrentRoom();

        ArrayList<Item> items = player.getInventory();

        for (Item item : items) {
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

        for (Item item : items) {
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

        // Enhanced Switch
        direction = switch (directionStr) {
            case "north" -> Character.Direction.NORTH;
            case "south" -> Character.Direction.SOUTH;
            case "east" -> Character.Direction.EAST;
            case "west" -> Character.Direction.WEST;
            default -> null;
        };

        if (direction == null) {
            System.out.println("There is no door!\n");
        } else {
            character.move(direction);

            // Room Info
            System.out.println(character.getCurrentRoom().getLongDescription());

            // Player Info
            if (!character.getEffects().isEmpty()) {
                System.out.println("Your Current Effects:");
                for (Effect effect : character.getEffects()) {
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
