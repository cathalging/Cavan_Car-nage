package game.characters;

import game.Room;

public class TurfMinion extends NPC {
    String description;
    TurfKing king;

    public TurfMinion(String name, Room startingRoom, int health, int damage, String description, String fileName, TurfKing king) {
        super(name, startingRoom, health, damage, fileName);
        this.description = description;
        this.king = king;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void onDeath() {

    }


    @Override
    public String getDeathMessage() {
        return "I dedicated my life to the King, now I may rest in peace.";
    }

    @Override
    public void onHit(Character character) {
        if (character instanceof Player) {
            ((Player) character).takeHit(damage, this);
        } else {
            character.takeHit(damage);
        }
        king.increaseDamage(5);
        king.onHit(character);
    }
}
