package game;

import game.characters.NPC;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room implements Serializable {
    private String longDescription;
    private String shortDescription;
    private String name;

    private Map<Direction, String> exitIds; // Map direction to neighboring game.Room
    private HashMap<Direction, Room> exits;

    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<game.characters.Character> characters = new ArrayList<>();

    public Room(String name) {
        this.name = name;
        this.exitIds = new HashMap<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void addCharacter(game.characters.Character character) {
        characters.add(character);
    }

    public void removeCharacter(game.characters.Character character) {
        characters.remove(character);
    }

    public ArrayList<game.characters.Character> getCharacters() {
        return characters;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getDescription() {
        return longDescription;
    }

    public void addExit(Direction direction, String neighborName) {
        exitIds.put(direction, neighborName);
    }
    
    public Room getExit(Direction direction) {
        return exits.get(direction);
    }

    public void resolveExits(Map<String, Room> allRooms) {
        exits = new HashMap<>();
        for (var entry : exitIds.entrySet()) {
            exits.put(entry.getKey(), allRooms.get(entry.getValue()));
        }
    }

    public String getExitString() {
        StringBuilder sb = new StringBuilder();
        for (Direction direction : exitIds.keySet()) {
            sb.append(direction).append(" ");
        }
        return sb.toString().trim();
    }

    public String getLongDescription() {
        StringBuilder msg = new StringBuilder("You are " + longDescription + "\n");
        if (!items.isEmpty()) {
            for (Item item : items) {
                msg.append(item.getDescription() + "\n");
            }
        }
        if (!characters.isEmpty()) {
            for (game.characters.Character character : characters) {
                if (character instanceof NPC npc)
                    if (npc.getDescription() != null) {
                        msg.append("\n" + npc.getDescription());
                    }
            }
        }
        msg.append("\nExits:\n" + getExitString());
        return msg.toString();
    }
}
