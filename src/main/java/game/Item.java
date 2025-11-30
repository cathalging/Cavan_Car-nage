package game;

import game.characters.*;
import game.characters.Character;

import java.io.Serializable;

public class Item implements Serializable {
    private String description;
    private String name;
    private int value;

    public Item(String name, String description, Room location) {
        this.name = name;
        this.description = description;
        location.addItem(this);
    }

    public Item(String name, String description, Character character) {
        this.name = name;
        this.description = description;
        character.addInventoryItem(this);
    }

    public Item(String name, String description,  int value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
