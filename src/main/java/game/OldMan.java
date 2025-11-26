package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class OldMan extends NPC{
    public OldMan(String name, Room startingRoom, int health, int damage, Item... items) {
        super(name, startingRoom, health, damage, items);
    }

    @Override
    public String getDescription() {
        return "An old man stands on the footpath.";
    }

    @Override
    protected int getDialogueOption() {
        if (state == Emotion.ANGRY)
            return ThreadLocalRandom.current().nextInt(6, 9);
        else if (state == Emotion.HAPPY)
            return ThreadLocalRandom.current().nextInt(11, 13);
        else //Neutral
            return ThreadLocalRandom.current().nextInt(2, 4);

    }

    @Override
    public String getDialogue() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int current = 1;
            int option = getDialogueOption();

            while ((line = reader.readLine()) != null) {
                if (current == option) {
                    return line;
                }
                current++;
            }

            return "[Error: No dialog found]";

        } catch (IOException e) {
            return "[Error: Failed to read file: " + filePath + "]";
        }
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
