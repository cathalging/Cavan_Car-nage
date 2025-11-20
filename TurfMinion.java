public class TurfMinion extends NPC{
    String description;
    TurfKing king;

    public TurfMinion(String name, Room startingRoom, int health, int damage, String description, TurfKing king) {
        super(name, startingRoom, health, damage);
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
    protected int getDialogueOption() {
        return 0;
    }

    @Override
    public String getDialogue() {
        return "We serve the High King of Turf";
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
