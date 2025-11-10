public class Consumable extends Item {
    private Character.Effect effect;

    public Consumable(String name, String description, Room location, Character.Effect effect) {
        super(name, description, location);
        this.effect = effect;
    }

    public Character.Effect getEffect() {
        return effect;
    }
}
