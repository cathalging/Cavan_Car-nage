package game.characters;

import game.items.Item;
import game.rooms.Room;

public class TightFist extends NPC implements CanTrade {
    protected Item itemOffered;
    protected String itemWantedName;

    public TightFist(String name, Room startingRoom, int health, int damage, String fileName) {
        super(name, startingRoom, health, damage, fileName);
        this.description = description;
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
        this.itemWantedName = itemWantedName;
    }

    @Override
    public void setItemOffered(Item item) {
        this.itemOffered = item;
    }

    @Override
    public String getItemWantedName() {
        return this.itemWantedName;
    }

    @Override
    public Item getItemOffered() {
        return this.itemOffered;
    }
}
