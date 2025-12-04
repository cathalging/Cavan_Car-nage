package game.characters;

import game.Item;
import game.Room;

import java.util.ArrayList;
import java.util.Collections;

public class ShopKeeper extends NPC {
    static private ArrayList<Item> shopItems = new ArrayList<>();

    public ShopKeeper(String name, Room startingRoom, int health, int damage, String fileName, Item... items) {
        super(name, startingRoom, health, damage, fileName, items);
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
    public void onDeath() {

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
