public class TurfKing extends NPC {

    public TurfKing(String name, Room startingRoom, int health, int damage) {
        super(name, startingRoom, health, damage);
    }

    public void increaseDamage(int amount) {
        this.damage += amount;
        System.out.println("I get angrier when you hit my minions.");
    }

    @Override
    public String getDescription() {
        return "A shirtless man sits on top of a throne of turf alongside your cars wheels." +
                " He is covered in peat, and either side of him stand two men also covered in peat.";
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
        return "I am the High King of Turf. Thou shall feck off fairly lively.";
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
