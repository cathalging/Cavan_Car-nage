package game;

import game.characters.Effect;

public class Consumable extends Item {
    private Effect effect;

    public Consumable(String name, Room location, Effect effect) {
        super(name, location);
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }
}
