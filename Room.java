import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Room implements Serializable {
    private String description;
    private String name;
    private Map<Character.Direction, Room> exits; // Map direction to neighboring Room
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Character> characters = new ArrayList<>();

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

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public void removeCharacter(Character character) {
        characters.remove(character);
    }

    public ArrayList<Character> getCharacters() {
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

    public Room getExit(Character.Direction direction) {
        return exits.get(direction);
    }

    public String getExitString() {
        StringBuilder sb = new StringBuilder();
        for (Character.Direction direction : exits.keySet()) {
            sb.append(direction).append(" ");
        }
        return sb.toString().trim();
    }

    public String getLongDescription() {
        StringBuilder msg = new StringBuilder("You are " + description + ".\nExits: " + getExitString());
        if (!items.isEmpty()) {
            msg.append("\nItems: ");
            for (Item item : items) {
                msg.append(item.getName() + " ");
            }
        }
        if (!characters.isEmpty()) {
            msg.append("\nCharacters: ");
            for (Character character : characters) {
                msg.append(character.getName() + " ");
            }
        }
        return msg.toString();
    }
}
