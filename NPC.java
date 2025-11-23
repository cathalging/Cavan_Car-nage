import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public abstract class NPC extends Character {
    protected String filePath = "dialogue/" + name + ".txt";
    protected boolean invincible = false;
    protected String description;

    protected enum Emotion {
        NEUTRAL,
        HAPPY,
        ANGRY
    }

    protected Emotion state = Emotion.NEUTRAL;

    public NPC(String name, Room startingRoom, int health, int damage, Item... items) {
        super(name, startingRoom, health, damage);

        for (Item item : items) {
           addInventoryItem(item);
        }
    }

    public NPC() {
        super();
    }

    protected void setState(Emotion state) {
        this.state = state;
    }

    protected boolean isInvincible() {
        return invincible;
    }

    abstract public String getDescription();

    abstract public void onDeath();

    abstract protected int getDialogueOption();

    abstract public String getDialogue();

    abstract public String getDeathMessage();

    abstract public void onHit(Character character);
}
