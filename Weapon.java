public class Weapon extends Item {
    private int damage;

    public Weapon(String name, String description, Character character, int damage) {
        super(name, description, character);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
