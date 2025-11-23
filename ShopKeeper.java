import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class ShopKeeper extends NPC {
    static private ArrayList<Item> shopItems = new ArrayList<>();

    public ShopKeeper(String name, Room startingRoom, int health, int damage, Item... items) {
        super(name, startingRoom, health, damage, items);
    }

    public ShopKeeper() {
        super();
    }

    public ArrayList<Item> getShopItems() {
        return shopItems;
    }

    public void addShopItems(Item... items) {
        Collections.addAll(shopItems, items);
    }

    public void removeShopItem(Item item) {
        shopItems.remove(item);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void onDeath() {

    }

    @Override
    protected int getDialogueOption() {
        if (state == Emotion.ANGRY)
            return ThreadLocalRandom.current().nextInt(6, 8);
        //else if (state == Emotion.HAPPY)
        //  return ThreadLocalRandom.current().nextInt(11, 13);
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
    public String getDeathMessage() {
        return "How did you beat me?";
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
