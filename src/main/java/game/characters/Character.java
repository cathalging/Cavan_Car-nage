package game.characters;

import game.rooms.Direction;
import game.items.Item;
import game.io.OutputController;
import game.rooms.Room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Character implements Serializable {
    protected String name;
    protected int health;
    protected int damage;

    protected Room currentRoom;
    protected ArrayList<Item> inventory = new ArrayList<>();

    protected OutputController outputController = new OutputController();

    protected HashMap<Effect, Integer> effects = new HashMap<>();
    public static HashMap<Effect, String> effectDescription = new HashMap<>();
    static {
        effectDescription.put(Effect.LANGERS, "Langers: You are out of your mind, what a lightweight.");
        effectDescription.put(Effect.NOTIONS, "Notions: You have an air of confidence about you.");
    }

    public Character(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
    }

    public Character(String name, Room startingRoom, int health, int damage) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.health = health;
        this.damage = damage;
    }

    public Character() {}

    public void addInventoryItem(Item item) {
        inventory.add(item);
    }

    public void removeInventoryItem(Item item) {
        inventory.remove(item);
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public Set<Effect> getEffects() {return effects.keySet();}

    public boolean addEffect(Effect effect) {
        if (!effects.keySet().contains(effect)) {
            effects.put(effect, 3);
            return true;
        }
        return false;
    }

    public void removeEffect(Effect effect) {
        if (effects.keySet().contains(effect)) {
            effects.remove(effect);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }


    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void move(Direction direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
        } else {
            outputController.addText("You can't go that way!");
        }
    }

    public void dropInventory() {
        for (Item item : inventory) {
            removeInventoryItem(item);
            currentRoom.add(item);
            break;
        }
    }

    public void takeHit(int damage) {
        setHealth(getHealth() - damage);
    }
}
