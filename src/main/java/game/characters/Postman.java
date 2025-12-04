package game.characters;

import game.items.Item;
import game.rooms.Room;

public class Postman extends NPC implements CanTrade {
    private String itemWantedString;
    private Item itemOffered;

    public Postman(String name, Room startingRoom, int health, int damage, String dialoguePath, Item... items) {
            super(name, startingRoom, health, damage, dialoguePath, items);
    }

    @Override
    public void onDeath() {

    }

    @Override
    public String getDeathMessage() {
        return "";
    }

    @Override
    public void onHit(Character character) {

    }

    @Override
    public void setItemWantedName(String itemWantedName) {
        this.itemWantedString = itemWantedName;
    }

    @Override
    public void setItemOffered(Item item) {
        this.itemOffered = item;
    }

    @Override
    public String getItemWantedName() {
        return itemWantedString;
    }

    @Override
    public Item getItemOffered() {
        return itemOffered;
    }
}
