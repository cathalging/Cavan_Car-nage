package game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.characters.NPC;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        loadDescription();
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
        for (Direction direction: exits.keySet()) {
            sb.append("\nIn the ").append(direction.name()).append(", ");
            sb.append(exits.get(direction).getShortDescription().toLowerCase()).append("\n");
        }
        return sb.toString();
    }

    public void loadDescription() {
        JsonNode roomsNode;
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream in = getClass().getClassLoader()
                    .getResourceAsStream("game/descriptions/rooms.json");

            if (in == null) {
                throw new FileNotFoundException("rooms.json not found on classpath");
            }

            JsonNode root = mapper.readTree(in);
            roomsNode = root.get("rooms");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        JsonNode section = roomsNode.get(name);

        if (section != null) {
            longDescription = section.get("longDesc").asText();
        shortDescription = section.get("shortDesc").asText();
        } else {
            System.err.println("Room not found in JSON: " + name);
        }
    }

    public String getLongDescription() {
        StringBuilder msg = new StringBuilder(longDescription + "\n");
        if (!items.isEmpty()) {
            for (Item item : items) {
                msg.append(item.getRoomDescription() + "\n");
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
        msg.append(getExitString());
        return msg.toString();
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
