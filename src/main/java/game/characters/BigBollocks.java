package game.characters;

import game.Room;

public class BigBollocks extends NPC {
    public BigBollocks(String name, Room startingRoom, int health, int damage, String description, String fileName) {
        super(name, startingRoom, health, damage, fileName);
        this.description = description;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void onDeath() {
        dropInventory();
    }



    @Override
    public String getDeathMessage() {
        return "";
    }

    @Override
    public void onHit(Character character) {
        character.takeHit(damage);
    }
}
