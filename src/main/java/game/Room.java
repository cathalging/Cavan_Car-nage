package game;

import game.characters.Character;
import game.characters.NPC;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room implements Serializable {
    private String description;
    private String name;
    private Map<game.characters.Character.Direction, Room> exits; // Map direction to neighboring game.Room
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<game.characters.Character> characters = new ArrayList<>();

    private int state = 0;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        exits = new HashMap<>();
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
        return description;
    }

    public void setExit(Character.Direction direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public Room getExit(game.characters.Character.Direction direction) {
        return exits.get(direction);
    }

    public String getExitString() {
        StringBuilder sb = new StringBuilder();
        for (game.characters.Character.Direction direction : exits.keySet()) {
            sb.append(direction).append(" ");
        }
        return sb.toString().trim();
    }

    public String getLongDescription() {
        StringBuilder msg = new StringBuilder("You are " + description + "\n");
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
