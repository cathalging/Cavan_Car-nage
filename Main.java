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
    private Player player;
    private NPC oldMan, shopKeeper, turfKing, turfMinion1, turfMinion2, squirrel, tightFist, bigBollocks, farmer;

    private Room pubFront, pubBack, mainstreet, postOffice, church, townSquare, farm, shop, park, river, cave, estate, bog, forest, heaven;
    private Item match;
    private Consumable pint;
    private Weapon stick;
    private Part keys, wheels, headlights, sparkPlug;

    private ShopKeeper shopController = new ShopKeeper();
    File file = new File("player.ser");
    private OutputController outputController;

    boolean finished = false;

    public Main() {
        createRooms();
        createCharacters();
        createObjects();
        createTrades();
        parser = new Parser();
        outputController = new OutputController();
    }

    private void createTrades() {
        ((canTrade) tightFist).setItemWantedName("Drink Voucher");
        ((canTrade) squirrel).setItemWantedName("Nuts");
        ((canTrade) farmer).setItemWantedName("Soggy Match");

        ((canTrade) squirrel).setItemOffered(new Item("Drink Voucher", "A drink voucher for the local pub", squirrel));
        ((canTrade) tightFist).setItemOffered(sparkPlug);
        ((canTrade) farmer).setItemOffered(headlights);
    }

    public void createObjects() {
        // Consumables
        pint = new Consumable("Pint", "A creamy pint of Guinness", pubFront, Effect.LANGERS);

        // Weapons
        stick = new Weapon("Stick", "A pointy stick", turfMinion1, 20);

        // Shop Items
        shopController.addShopItems(new Item("Engine Oil", "A bottle of engine oil from 1977", 5),
                new Item("Nuts", "Roasted Peanuts", 2));

        // Parts
        keys = new Part("Keys", "The keys to your car", bigBollocks);
        wheels = new Part("Wheels", "The wheels on your car go around and around", turfKing);
        headlights = new Part("Headlights", "The headlights for your car", farmer);
        sparkPlug = new Part("Spark Plug", "A spark plug for your car", tightFist);

        // Misc
        match = new Item("Soggy Match", "Could still be used if you're determined enough", cave);
    }

    private void createRooms() {
        // create rooms
        postOffice = new Room("Post Office", "inside the town's small post office");
        mainstreet = new Room("Main Street", "walking along the town's main street");
        church = new Room("Church", "standing inside a quiet stone church");
        pubFront = new Room("Pub Front", "at the lively front of the pub");
        pubBack = new Room("Pub Back", "in the dim back room of the pub");
        townSquare = new Room("Town Square", "in the town square with its central fountain");
        farm = new Room("Town Hall", "inside the town hall with tall pillars");
        shop = new Room("Shop", "in the general shop full of supplies");
        park = new Room("Park", "in the peaceful green park");
        estate = new Room("Estate", "on the grounds of an old estate");
        cave = new Room("Cave", "inside a dark echoing cave");
        bog = new Room("Abandoned Cottage", "in a bog land.");
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

        townSquare.setExit(Character.Direction.WEST, farm);
        farm.setExit(Character.Direction.EAST, townSquare);

        townSquare.setExit(Character.Direction.EAST, shop);
        shop.setExit(Character.Direction.WEST, townSquare);

        townSquare.setExit(Character.Direction.SOUTH, park);
        park.setExit(Character.Direction.NORTH, townSquare);

        park.setExit(Character.Direction.WEST, estate);
        estate.setExit(Character.Direction.EAST, park);

        estate.setExit(Character.Direction.SOUTH, bog);
        bog.setExit(Character.Direction.NORTH, estate);

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
                player = (Player) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            player = new Player("player", townSquare, 100, 20);
        }

        // NPCS
        oldMan = new OldMan("Old Man", townSquare, 100, 10);
        townSquare.addCharacter(oldMan);

        shopKeeper = new ShopKeeper("Shop Keeper", shop, 100000, 10000);
        shop.addCharacter(shopKeeper);

        turfKing = new TurfKing("Turf King", bog, 200, 5);
        turfMinion1 = new TurfMinion("Turf Minion 1", bog, 50, 10, "The first stands wielding a stick.", (TurfKing) turfKing);
        turfMinion2 = new TurfMinion("Turf Minion 2", bog, 50, 10, "The second raises his fists as you approach.", (TurfKing) turfKing);
        bog.addCharacter(turfKing);
        bog.addCharacter(turfMinion1);
        bog.addCharacter(turfMinion2);
        
        squirrel = new Squirrel("Squirrel", forest);
        forest.addCharacter(squirrel);

        tightFist = new TightFist("Tommy Tight Fist", pubFront, 150, 20, "A man sits at the bar with his hand tightly wrapped around your cars spark plug.");
        pubFront.addCharacter(tightFist);

        bigBollocks = new BigBollocks("Billy Big Bollocks", pubBack, 300, 15, "A man flexes his muscles at you as you walk in. He is wearing your car key on the chain around his neck.");
        pubBack.addCharacter(bigBollocks);

        farmer = new Farmer("Farmer", farm, 170, 15, "A farmer is replacing a lantern with your cars headlights.");
    }

    public void play() {
        printWelcome();

        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        outputController.addText("Thank you for playing. Goodbye.");
    }

    private void savePlayer() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("player.ser"))) {
            out.writeObject(player);
            outputController.addText("Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printWelcome() {
        System.out.println();
        outputController.addText("Your car splutters to a stop in the middle of Cavan Town. You look at the dash and see a light to change engine oil. Thankfully you see a shop to the east.");
        outputController.addText("Type 'help' if you need help.");
        System.out.println();
        outputController.addText(player.getCurrentRoom().getLongDescription());
    }

    private boolean processCommand(Command command) {
        String commandWord = command.getCommandWord();

        if (commandWord == null) {
            outputController.addText("I don't understand your command... ");
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
                attack(command);
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
            case "trade", "offer":
                trade(command);
                break;
            case "save":
                savePlayer();
                break;
            case "quit":
                if (command.hasSecondWord()) {
                    outputController.addText("Quit what?");
                    return false;
                } else {
                    return true; // signal to quit
                }
            default:
                outputController.addText("I don't know what you mean...");
                break;
        }
        return false;
    }

    private void trade(Command command) {
        if (!command.hasSecondWord()) {
            outputController.addText("Trade what?");
            return;
        }

        ArrayList<Character> roomCharacters = player.getCurrentRoom().getCharacters();
        canTrade trader = null;
        for (Character character : roomCharacters) {
            if ((character instanceof NPC) && (character instanceof canTrade)) {
                trader = (canTrade) character;
                break;
            }
        }

        if (trader == null) {
            outputController.addText("There is no one to trade with in this room.");
            return;
        }

        String itemName = command.getSecondWord();
        Item tradeItem = null;
        for (Item item : player.getInventory()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                tradeItem = item;
                break;
            }
        }

        if (tradeItem == null) {
            outputController.addText("You do not have an item called " + itemName);
            return;
        }

        if (trader.getItemWantedName().equalsIgnoreCase(itemName)) {
            player.removeInventoryItem(tradeItem);
            player.addInventoryItem(trader.getItemOffered());
            outputController.addText("You traded " + tradeItem.getName() + " for " + trader.getItemOffered().getName());
            return;
        }
        outputController.addText("The item you offered is not wanted.");
    }

    private void buy(Command command) {
        if (!player.getCurrentRoom().equals(shop)) {
            outputController.addText("You are not in the shop.");
            return;
        }

        if (!command.hasSecondWord()) {
            outputController.addText("Buy what?");
            return;
        }

        String itemName = command.getSecondWord();
        ArrayList<Item> items = shopController.getShopItems();

        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                if (player.getMoney() >= item.getValue()) {
                    player.setMoney(player.getMoney() - item.getValue());
                    player.addInventoryItem(item);
                    outputController.addText("You bought " + item.getName() + " for " + item.getValue());
                } else {
                    outputController.addText("You don't have enough money.");
                }
                return;
            }
        }
        outputController.addText("Item does not exist.");
    }

    private void shop() {
        if (player.getCurrentRoom().equals(shop)) {
            outputController.addText("Item:\t\tPrice:");
            for (Item item : ((ShopKeeper) shopKeeper).getShopItems()) {
                outputController.addText(item.getName() + "\t" + item.getValue());
            }
        } else {
            outputController.addText("You are not in the shop.");
        }
    }

    private void drive(Command command) {
        if (!player.getCurrentRoom().equals(townSquare)) {
            outputController.addText("Your car is in the town square.");
            return;
        }
        if (!Car.fixed()) {
            outputController.addText("Your car is still missing parts.");
            return;
        }
        outputController.addText("You start your car, and escape from Cavan. You win.");
    }

    private void use(Command command) {
        if (!command.hasSecondWord()) {
            outputController.addText("Use what?");
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

        switch (item) {
            case null -> {
                outputController.addText("Item not found");
                break;
            }
            case Consumable consumable -> {
                consumeItem(command);
                break;
            }
            case Part part -> {
                if (player.getCurrentRoom().equals(townSquare)) {
                    Car.addPart(item);
                    outputController.addText("You put the " + item.getName() + " in your car.");
                } else {
                    outputController.addText("Your car is in the town square");
                }
                break;
            }
            default -> {
                outputController.addText("Invalid item");
            }
        }
    }

    private void attack(Command command) {
        if (!command.hasSecondWord()) {
            outputController.addText("Attack what?");
            return;
        }

        String opponentName = command.getSecondWord().toLowerCase();
        ArrayList<Character> roomCharacters = player.getCurrentRoom().getCharacters();

        Character opponent = null;
        for (Character roomCharacter : roomCharacters) {
            if (roomCharacter.getName().toLowerCase().equals(opponentName)) {
                opponent = roomCharacter;
            }
        }
        if (opponent == null) {
            outputController.addText("Character " + opponentName + " does not exist");
            return;
        }

        int playerDamage = player.getDamage();
        for (Item item : player.getInventory()) {
            if (item instanceof Weapon) {
                playerDamage += ((Weapon) item).getDamage();
            }
        }


        if (opponent instanceof NPC npc) {
            if (!npc.isInvincible()) {
                npc.takeHit(playerDamage);
                outputController.addText("You attacked " + npc.getName() + " for " + playerDamage + ".");
            }
            npc.onHit(player);
        } else {
            opponent.takeHit(playerDamage);
            outputController.addText("You attacked " + opponent.getName() + " for " + playerDamage + ".");
        }

        if (opponent.getHealth() <= 0) {
            opponent.dropInventory();

            if (opponent instanceof NPC) {
                outputController.addText(opponent.getName() + ": " + ((NPC) opponent).getDeathMessage());
                opponent.setCurrentRoom(heaven);
            }
            outputController.addText(opponent.getName() + " has died.");
            player.getCurrentRoom().removeCharacter(opponent);
            return;
        }

        outputController.addText(opponent.getName() + " Health: " + opponent.getHealth());


        if (player.getHealth() <= 0) {
            outputController.addText("You have died.");
            finished = true;
        }
    }

    private void talk(Command command) {
        if (!command.hasSecondWord()) {
            outputController.addText("Talk to who?");
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
            outputController.addText(npcName + " does not exist");
            return;
        }

        if (npc instanceof NPC) {
            outputController.addText(((NPC) npc).getDialogue());
        }
    }

    private void consumeItem(Command command) {
        if (!command.hasSecondWord()) {
            outputController.addText("Consume what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item[] items = player.getInventory().toArray(new Item[0]);

        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName) && item instanceof Consumable consumable) {
                if (player.addEffect(consumable.getEffect())) {
                    player.removeInventoryItem(item);
                    outputController.addText("You now have the following effect:");
                    outputController.addText(Character.effectDescription.get(consumable.getEffect()));
                    return;
                }
                outputController.addText("You already have this items effect.");
                return;
            }
        }
        outputController.addText("Item not found.\n");
    }

    private void showInventory() {
        ArrayList<Item> items = player.getInventory();
        outputController.addText("Inventory:");
        for (Item item : items.toArray(new Item[0])) {
            outputController.addText(item.getName() + ":\n" + item.getDescription());
        }
    }

    private void placeItem(Command command) {
        if (!command.hasSecondWord()) {
            outputController.addText("Place what?");
            return;
        }
        String itemName = command.getSecondWord();
        Room currentRoom = player.getCurrentRoom();

        ArrayList<Item> items = player.getInventory();

        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                player.removeInventoryItem(item);
                currentRoom.addItem(item);
                outputController.addText("The " + itemName + " was placed " + currentRoom.getDescription());
                return;
            }
        }
        outputController.addText("Item not found.");
    }

    private void takeItem(Command command) {
        if (!command.hasSecondWord()) {
            outputController.addText("Take what?");
            return;
        }
        String itemName = command.getSecondWord();

        ArrayList<Item> items = player.getCurrentRoom().getItems();

        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                player.addInventoryItem(item);
                player.getCurrentRoom().removeItem(item);
                outputController.addText("You took the " + item.getName());
                return;
            }
        }
        outputController.addText("Item not found.");
    }

    private void printHelp() {
        outputController.addText("You are lost. You are alone. You wander around the university.");
        System.out.print("Your command words are: ");
        parser.showCommands();
        System.out.println();
    }

    private void goRoom(Command command, Character character) {
        String directionStr;

        if (!command.hasSecondWord()) {
            outputController.addText("Go where?");
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
            outputController.addText("There is no door!\n");
        } else {
            character.move(direction);

            // Room Info
            outputController.addText(character.getCurrentRoom().getLongDescription());

            // Player Info
            if (!character.getEffects().isEmpty()) {
                outputController.addText("Your Current Effects:");
                for (Effect effect : character.getEffects()) {
                    outputController.addText(Character.effectDescription.get(effect));
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
