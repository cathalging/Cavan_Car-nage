package game.characters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.items.Item;
import game.rooms.Room;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class NPC extends Character {
    protected String dialoguePath;
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

    public NPC(String name, Room startingRoom, int health, int damage, String dialoguePath, Item... items) {
        super(name, startingRoom, health, damage);

        for (Item item : items) {
           addInventoryItem(item);
        }

        this.dialoguePath = "/game/dialogue/" + dialoguePath + ".json";
        loadDialog();
        loadDescription();
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

    private void loadDescription() {
        JsonNode npcsNode;
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream in = getClass().getClassLoader()
                    .getResourceAsStream("game/descriptions/npcs.json");

            if (in == null) {
                throw new FileNotFoundException("npcs.json not found on classpath");
            }

            JsonNode root = mapper.readTree(in);
            npcsNode = root.get("npcs");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        JsonNode section = npcsNode.get(name.toLowerCase());

        if (section != null) {
            description = section.get("description").asText();
        } else {
            System.err.println("Description not found in JSON: " + name);
        }
    }

    public String getDescription() {
        return description;
    }

    abstract public void onDeath();

    private void loadDialog() {
        try (InputStream is = getClass().getResourceAsStream(dialoguePath)) {
            if (is == null) {
                throw new RuntimeException("Dialog file not found: " + dialoguePath);
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
