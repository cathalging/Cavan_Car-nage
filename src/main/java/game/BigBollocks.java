package game;

public class BigBollocks extends NPC {
    public BigBollocks(String name, Room startingRoom, int health, int damage, String description) {
        super(name, startingRoom, health, damage);
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
        character.takeHit(damage);
    }
}
