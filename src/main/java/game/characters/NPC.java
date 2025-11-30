package game.characters;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.Item;
import game.Room;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class NPC extends Character {
    protected String filePath;
    protected boolean invincible = false;
    protected String description;
    protected Map<String, List<String>> dialog;   // loaded JSON
    protected static final ObjectMapper mapper = new ObjectMapper();
    protected static final Random random = new Random();

    protected enum Emotion {
        NEUTRAL,
        HAPPY,
        ANGRY
    }

    protected Emotion state = Emotion.NEUTRAL;

    public NPC(String name, Room startingRoom, int health, int damage, String fileName, Item... items) {
        super(name, startingRoom, health, damage);

        for (Item item : items) {
           addInventoryItem(item);
        }
        this.filePath = "/game/dialogue/" + fileName + ".json";
        loadDialog();
    }

    public NPC() {
        super();
    }

    protected void setState(Emotion state) {
        this.state = state;
    }

    public boolean isInvincible() {
        return invincible;
    }

    abstract public String getDescription();

    abstract public void onDeath();

    private void loadDialog() {
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                throw new RuntimeException("Dialog file not found: " + filePath);
            }

            dialog = mapper.readValue(
                    is,
                    mapper.getTypeFactory().constructMapType(
                            Map.class, String.class, List.class
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to load dialog for NPC: " + name, e);
        }
    }

    public String getDialog() {
        String key = state.name().toLowerCase();
        List<String> lines = dialog.get(key);

        if (lines == null || lines.isEmpty()) {
            return "[No dialog for " + key + "]";
        }
        return lines.get(random.nextInt(lines.size()));
    }

    abstract public String getDeathMessage();

    abstract public void onHit(Character character);
}
