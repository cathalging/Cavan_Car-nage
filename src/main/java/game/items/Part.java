package game.items;

import game.rooms.Room;
import game.characters.Character;

public class Part extends Item {
    public Part(String name, Room room) {
        super(name, room);
    }

    public Part(String name, Character character) {
        super(name, character);
    }
}
