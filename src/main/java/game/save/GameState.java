package game.save;

import game.rooms.Room;
import game.characters.NPC;
import game.characters.Player;

import java.io.Serializable;
import java.util.Map;

public class GameState implements Serializable {
    private Player player;
    private Map<String, Room> rooms;
    private Map<String, NPC> npcs;

    public GameState(Player player, Map<String, Room> rooms, Map<String, NPC> npcs) {
        this.player = player;
        this.rooms = rooms;
        this.npcs = npcs;
    }

    public Player getPlayer() { return player; }
    public Map<String, Room> getRooms() { return rooms; }
    public Map<String, NPC> getNpcs() { return npcs; }
}
