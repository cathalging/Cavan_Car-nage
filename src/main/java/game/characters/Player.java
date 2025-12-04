package game.characters;

import game.rooms.Room;

public class Player extends Character {
    private int money;
    private int maxHealth;

    public Player(String name, Room startingRoom, int health, int damage) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.health = health;
        this.damage = damage;
        this.maxHealth = health;
        money = 5;
    }

    public float getHealthPercentage() {
        return (float) health / (float) maxHealth;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void takeHit(int damage, game.characters.Character opponent) {
        if (this.getEffects().contains(Effect.LANGERS)) {
            setHealth(getHealth() - (damage / 2));
            outputController.addText(opponent.getName() + " attacked you for " + (damage / 2));
        } else {
            setHealth(getHealth() - damage);
            outputController.addText(opponent.getName() + " attacked you for " + damage);
        }
        outputController.addText("Your health: " + getHealth());
    }
}
