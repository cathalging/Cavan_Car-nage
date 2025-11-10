public class NPC extends Character {
    public NPC(String name, Room startingRoom, int health, int damage, Item... items) {
        super(name, startingRoom, health, damage);

        for (Item item : items) {
           addInventoryItem(item);
        }
    }

    public void death() {
        dropInventory();
    }

    public void onHit() {
        System.out.println("Oof");
    }

    public String getDeathMessage() {
        return "I am dead.";
    }
}
