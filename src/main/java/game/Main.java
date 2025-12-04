package game;

import game.GUI.AudioManager;
import game.GUI.GUI;
import game.characters.*;
import game.characters.Character;
import game.characters.Effect;
import game.io.Command;
import game.io.CommandWords;
import game.io.OutputController;
import game.io.Parser;
import game.items.*;
import game.rooms.Direction;
import game.rooms.Room;
import game.save.GameState;
import game.save.SaveManager;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static private Parser parser;
    private Player player;
    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, NPC> npcs = new HashMap<>();
    private NPC oldMan, shopKeeper, turfKing, turfMinion1, turfMinion2, squirrel, tightFist, bigBollocks, farmer, postman;

    private Item match;
    private Consumable pint;
    private Weapon stick;
    private Part keys, wheels, headlights, sparkPlug;

    private ShopKeeper shopController = new ShopKeeper();
    File file = new File("player.ser");
    private OutputController outputController = new OutputController();

    private GUI gui;
    private static Main game;

    boolean finished = false;

    public Main() {
        parser = new Parser();
        gui = new GUI();
        URL url = getClass().getResource("/game/music/background.mp3");
        System.out.println(url);

    }

    public void newGame() {
        createRooms();
        createCharacters();
        createObjects();
        createTrades();
        printWelcome();
    }

    public static Main getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    private void createTrades() {
        ((CanTrade) tightFist).setItemWantedName("Drink Voucher");
        ((CanTrade) squirrel).setItemWantedName("Nuts");
        ((CanTrade) farmer).setItemWantedName("Soggy Match");
        ((CanTrade) postman).setItemWantedName("Stamp");

        ((CanTrade) squirrel).setItemOffered(new Item("Drink Voucher", squirrel));
        ((CanTrade) tightFist).setItemOffered(sparkPlug);
        ((CanTrade) farmer).setItemOffered(headlights);
    }

    public void createObjects() {
        // Consumables
        pint = new Consumable("Pint", rooms.get("pubFront"), Effect.LANGERS);

        // Weapons
        stick = new Weapon("Stick", turfMinion1, 20);

        // Shop Items
        shopController.addShopItems(new Item("Engine Oil", 5),
                new Item("Nuts", 2));

        // Parts
        keys = new Part("Keys", bigBollocks);
        wheels = new Part("Wheels", turfKing);
        headlights = new Part("Headlights", farmer);
        sparkPlug = new Part("Spark Plug", tightFist);

        // Misc
        match = new Item("Soggy Match", rooms.get("cave"));

        new Item("Stamp", rooms.get("pubBack"));
        new Item("Stamp", rooms.get("estate"));
        new Item("Stamp", rooms.get("park"));
    }

    private void createRooms() {

        String[] roomIds = {
                "postOffice",
                "mainStreet",
                "church",
                "pubFront",
                "pubBack",
                "townSquare",
                "farm",
                "shop",
                "park",
                "estate",
                "cave",
                "bog",
                "river",
                "forest",
                "heaven"
        };

        for (String id : roomIds) {
            rooms.put(id, new Room(id));
        }

        rooms.get("postOffice").addExit(Direction.SOUTH, "mainStreet");

        rooms.get("mainStreet").addExit(Direction.NORTH, "postOffice");
        rooms.get("mainStreet").addExit(Direction.WEST, "church");
        rooms.get("mainStreet").addExit(Direction.SOUTH, "townSquare");
        rooms.get("mainStreet").addExit(Direction.EAST, "pubFront");

        rooms.get("church").addExit(Direction.EAST, "mainStreet");

        rooms.get("pubFront").addExit(Direction.WEST, "mainStreet");
        rooms.get("pubFront").addExit(Direction.EAST, "pubBack");

        rooms.get("pubBack").addExit(Direction.WEST, "pubFront");

        rooms.get("townSquare").addExit(Direction.NORTH, "mainStreet");
        rooms.get("townSquare").addExit(Direction.WEST, "farm");
        rooms.get("townSquare").addExit(Direction.EAST, "shop");
        rooms.get("townSquare").addExit(Direction.SOUTH, "park");

        rooms.get("farm").addExit(Direction.EAST, "townSquare");

        rooms.get("shop").addExit(Direction.WEST, "townSquare");

        rooms.get("park").addExit(Direction.NORTH, "townSquare");
        rooms.get("park").addExit(Direction.WEST, "estate");
        rooms.get("park").addExit(Direction.SOUTH, "river");
        rooms.get("park").addExit(Direction.EAST, "cave");

        rooms.get("estate").addExit(Direction.EAST, "park");
        rooms.get("estate").addExit(Direction.SOUTH, "bog");

        rooms.get("bog").addExit(Direction.NORTH, "estate");

        rooms.get("river").addExit(Direction.NORTH, "park");
        rooms.get("river").addExit(Direction.EAST, "forest");

        rooms.get("cave").addExit(Direction.WEST, "park");
        rooms.get("cave").addExit(Direction.SOUTH, "forest");

        rooms.get("forest").addExit(Direction.NORTH, "cave");
        rooms.get("forest").addExit(Direction.WEST, "river");

        for (Room r : rooms.values()) {
            r.resolveExits(rooms);
        }

    }

    public void createCharacters() {

        player = new Player("player", rooms.get("townSquare"), 100, 20);

        // NPCS
        oldMan = new OldMan("Old Man", rooms.get("townSquare"), 100, 10, "old_man");
        rooms.get("townSquare").add(oldMan);

        shopKeeper = new ShopKeeper("Shop Keeper", rooms.get("shop"), 100000, 10000, "shopkeeper");
        rooms.get("shop").add(shopKeeper);

        turfKing = new TurfKing("Turf King", rooms.get("bog"), 200, 5, "turf_king");
        turfMinion1 = new TurfMinion("Turf Minion 1", rooms.get("bog"), 50, 10, "turf_minion", (TurfKing) turfKing);
        turfMinion2 = new TurfMinion("Turf Minion 2", rooms.get("bog"), 50, 10, "turf_minion", (TurfKing) turfKing);
        rooms.get("bog").add(turfKing);
        rooms.get("bog").add(turfMinion1);
        rooms.get("bog").add(turfMinion2);

        squirrel = new Squirrel("game.npc.Squirrel", rooms.get("forest"));
        rooms.get("forest").add(squirrel);

        tightFist = new TightFist("Tommy Tight Fist", rooms.get("pubFront"), 150, 20, "tight_fist");
        rooms.get("pubFront").add(tightFist);

        bigBollocks = new BigBollocks("Billy Big Bollocks", rooms.get("pubBack"), 300, 15, "big_bollocks");
        rooms.get("pubBack").add(bigBollocks);

        farmer = new Farmer("Farmer", rooms.get("farm"), 170, 15, "farmer");
        rooms.get("farm").add(farmer);

        postman = new Postman("Postman", rooms.get("postOffice"), 1000, 100, "postman");
        rooms.get("postOffice").add(postman);

        npcs.put("oldMan", oldMan);
        npcs.put("turfKing", turfKing);
        npcs.put("turfMinion1", turfMinion1);
        npcs.put("turfMinion2", turfMinion2);
        npcs.put("squirrel", squirrel);
        npcs.put("tightFist", tightFist);
        npcs.put("farmer", farmer);
        npcs.put("bigBollocks", bigBollocks);
        npcs.put("shopKeeper", shopKeeper);
    }


    public void play() {
        gui.run();
    }

    private void printWelcome() {
        outputController.addText("You decide to pull over for a snooze in Cavan Town to break up your long journey home."
                + "You close your eyes and have a brilliant sleep. You open them a short while late to find your car stripped of its headlights, wheels, keys and a spark plug.");
        outputController.addText(player.getCurrentRoom().getLongDescription());
    }

    public boolean processCommand(Command command) {
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
                saveGame(command);
                break;
            case "quit":
                if (command.hasSecondWord()) {
                    outputController.addText("Quit what?");
                    return false;
                } else {
                    System.exit(0);
                }
            default:
                outputController.addText("I don't know what you mean...");
                break;
        }
        return false;
    }

    private void saveGame(Command command) {
        if (!command.hasSecondWord()) {
            outputController.addText("Enter save file name.");
            return;
        }

        String fileName = command.getSecondWord();

        GameState gameState = new GameState(player, rooms, npcs);

        SaveManager.saveGame(gameState, fileName);
    }

    public void loadGame(File file) {
        GameState gameState = SaveManager.loadGame(file);

        player = gameState.getPlayer();
        rooms = gameState.getRooms();
        npcs = gameState.getNpcs();

        oldMan = npcs.get("oldMan");
        turfKing = npcs.get("turfKing");
        turfMinion1 = npcs.get("turfMinion1");
        turfMinion2 = npcs.get("turfMinion2");
        squirrel = npcs.get("squirrel");
        tightFist = npcs.get("tightFist");
        bigBollocks = npcs.get("bigBollocks");
        farmer = npcs.get("farmer");
        shopKeeper = npcs.get("shopKeeper");
    }

    private void trade(Command command) {
        if (!command.hasSecondWord()) {
            outputController.addText("Trade what?");
            return;
        }

        ArrayList<Character> roomCharacters = player.getCurrentRoom().getCharacters();
        CanTrade trader = null;
        for (Character character : roomCharacters) {
            if ((character instanceof NPC) && (character instanceof CanTrade)) {
                trader = (CanTrade) character;
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
            if (trader instanceof Postman) {
                player.setMoney(player.getMoney() + 5);
                outputController.addText("You traded a stamp for 5 euro.");
                return;
            } else {
                player.removeInventoryItem(tradeItem);
                player.addInventoryItem(trader.getItemOffered());
                outputController.addText("You traded " + tradeItem.getName() + " for " + trader.getItemOffered().getName());
                return;
            }
        }
        outputController.addText("The item you offered is not wanted.");
    }

    private void buy(Command command) {
        if (!player.getCurrentRoom().equals(rooms.get("shop"))) {
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
        outputController.addText("game.items.Item does not exist.");
    }

    private void shop() {
        if (player.getCurrentRoom().equals(rooms.get("shop"))) {
            outputController.addText("game.items.Item:\t\tPrice:");
            for (Item item : ((ShopKeeper) shopKeeper).getShopItems()) {
                outputController.addText(item.getName() + "\t" + item.getValue());
            }
        } else {
            outputController.addText("You are not in the shop.");
        }
    }

    private void drive(Command command) {
        boolean win = false;
        if (command.hasSecondWord()) {
            if (command.getSecondWord().equalsIgnoreCase("now")) {
                win = true;
            }
        } else {
            if (!player.getCurrentRoom().equals(rooms.get("townSquare"))) {
                outputController.addText("Your car is in the town square.");
                return;
            }
            if (!Car.fixed()) {
                outputController.addText("Your car is still missing parts.");
                return;
            }
            win = true;
        }

        if (win) {
            outputController.addText("You start your car, and escape from Cavan. You win.\nType 'quit' to exit the game.");
            CommandWords.removeWords();
        }

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
                outputController.addText("game.items.Item not found");
                break;
            }
            case Consumable consumable -> {
                consumeItem(command);
                break;
            }
            case Part part -> {
                if (player.getCurrentRoom().equals(rooms.get("townSquare"))) {
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
            outputController.addText("game.characters.Character " + opponentName + " does not exist");
            return;
        }

        int playerDamage = player.getDamage();
        for (Item item : player.getInventory()) {
            if (item instanceof Weapon) {
                playerDamage += ((Weapon) item).getDamage();
            }
        }

        // Sound Effect
        if (gui != null) {
            AudioManager.playSfx("/game/music/hitSound.wav");
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
                opponent.setCurrentRoom(rooms.get("heaven"));
            }
            outputController.addText(opponent.getName() + " has died.");
            player.getCurrentRoom().remove(opponent);
            return;
        }

        outputController.addText(opponent.getName() + " Health: " + opponent.getHealth());


        if (player.getHealth() <= 0) {
            outputController.addText("You have died.");
            CommandWords.removeWords();
            outputController.addText("Enter quit to exit the game.");
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
            outputController.addText(((NPC) npc).getDialog());
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
        outputController.addText("game.items.Item not found.\n");
    }

    private void showInventory() {
        ArrayList<Item> items = player.getInventory();
        outputController.addText("Inventory:");
        for (Item item : items.toArray(new Item[0])) {
            outputController.addText(item.getName() + ":\n" + item.getRoomDescription());
        }

        outputController.addText("\nMoney: " + player.getMoney());
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
                currentRoom.add(item);
                outputController.addText("The " + itemName + " was placed");
                return;
            }
        }
        outputController.addText("game.items.Item not found.");
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
                player.getCurrentRoom().remove(item);
                outputController.addText("You took the " + item.getName());
                return;
            }
        }
        outputController.addText("game.items.Item not found.");
    }

    private void printHelp() {
        parser.showCommands();
    }

    private void goRoom(Command command, Character character) {
        String directionStr;

        if (!command.hasSecondWord()) {
            outputController.addText("Go where?");
            return;
        }

        Direction direction;
        directionStr = command.getSecondWord().toLowerCase();

        // Enhanced Switch
        direction = switch (directionStr) {
            case "north" -> Direction.NORTH;
            case "south" -> Direction.SOUTH;
            case "east" -> Direction.EAST;
            case "west" -> Direction.WEST;
            default -> null;
        };

        if (direction == null) {
            outputController.addText("There is no door!\n");
        } else {
            character.move(direction);
            outputController.clearConsole();

            // game.rooms.Room Info
            outputController.addText(character.getCurrentRoom().getLongDescription());

            // game.characters.Player Info
            if (!character.getEffects().isEmpty()) {
                outputController.addText("Your Current Effects:");
                for (Effect effect : character.getEffects()) {
                    outputController.addText(Character.effectDescription.get(effect));
                }
            }
        }
    }

    public static void main(String[] args) {
        game = new Main();
        game.play();
    }
}
