package game;

import game.characters.Effect;

public class Consumable extends Item {
    private Effect effect;

    public Consumable(String name, String description, Room location, Effect effect) {
        super(name, description, location);
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }
}
