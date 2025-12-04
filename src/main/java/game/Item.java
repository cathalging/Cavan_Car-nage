package game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.characters.Character;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

public class Item implements Serializable {
    private String roomDescription;
    private String inventoryDescription;
    private String name;
    private int value;

    public Item(String name, Room location) {
        this.name = name;
        loadDescriptions();
        location.addItem(this);
    }

    public Item(String name, Character character) {
        this.name = name;
        loadDescriptions();
        character.addInventoryItem(this);
    }

    public Item(String name, int value) {
        this.name = name;
        loadDescriptions();
        this.value = value;
    }

    public void loadDescriptions() {
        JsonNode itemsNode;
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream in = getClass().getClassLoader()
                    .getResourceAsStream("game/descriptions/items.json");

            if (in == null) {
                throw new FileNotFoundException("items.json not found on classpath");
            }

            JsonNode root = mapper.readTree(in);
            itemsNode= root.get("items");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        JsonNode section = itemsNode.get(name.toLowerCase());

        if (section != null) {
            roomDescription = section.get("roomDescription").asText();
            inventoryDescription = section.get("inventoryDescription").asText();

        } else {
            System.err.println("Item not found in JSON: " + name);
        }
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public String getInventoryDescription() {
        return inventoryDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
