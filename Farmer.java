public class Farmer extends NPC implements canTrade{
    String itemWantedName;
    Item itemOffered;

    public Farmer(String name, Room startingRoom, int health, int damage, String description) {
        super(name, startingRoom, health, damage);
        this.description = description;
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
        return 0;
    }

    @Override
    public String getDialogue() {
        return "";
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
