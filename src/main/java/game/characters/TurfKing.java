package game.characters;

import game.Room;

public class TurfKing extends NPC {

    public TurfKing(String name, Room startingRoom, int health, int damage, String fileName) {
        super(name, startingRoom, health, damage, fileName);
    }

    public void increaseDamage(int amount) {
        this.damage += amount;
        outputController.addText("I get angrier when you hit my minions.");
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
