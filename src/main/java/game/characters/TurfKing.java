package game.characters;

import game.Room;

public class TurfKing extends NPC {

    public TurfKing(String name, Room startingRoom, int health, int damage, String fileName) {
        super(name, startingRoom, health, damage, fileName);
    }

    public void increaseDamage(int amount) {
        this.damage += amount;
        outputController.addText("I get angrier when you hit my minions.");
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
        if (character instanceof Player) {
            ((Player) character).takeHit(damage, this);
        } else {
            character.takeHit(damage);
        }
    }
}
