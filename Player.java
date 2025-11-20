public class Player extends Character {
    private int money;

    public Player(String name, Room startingRoom, int health, int damage) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.health = health;
        this.damage = damage;
        money = 5;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void takeHit(int damage, Character opponent) {
        if (this.getEffects().contains(Effect.LANGERS)) {
            setHealth(getHealth() - (damage / 2));
            System.out.println(opponent.getName() + " attacked you for " + (damage / 2));
        } else {
            setHealth(getHealth() - damage);
            System.out.println(opponent.getName() + " attacked you for " + damage);
        }
        System.out.println("Your health: " + getHealth());
    }
}
