package game.items;

import game.characters.Character;

public class Weapon extends Item {
    private int damage;

    public Weapon(String name, Character character, int damage) {
        super(name, character);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
