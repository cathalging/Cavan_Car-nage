package game.characters;

import game.Item;
import game.Room;

public class OldMan extends NPC {
    public OldMan(String name, Room startingRoom, int health, int damage, String fileName, Item... items) {
        super(name, startingRoom, health, damage, fileName, items);
    }

    @Override
    public String getDescription() {
        return "An old man stands on the footpath.";
    }



    @Override
    public void onDeath() {
        dropInventory();
    }

    @Override
    public String getDeathMessage() {
        return "Ah you've killed me aye. An Old Man.";
    }

    @Override
    public void onHit(Character character) {
        setState(Emotion.ANGRY);
        if (character instanceof Player) {
            ((Player) character).takeHit(damage, this);
        } else {
            character.takeHit(damage);
        }
    }
}
