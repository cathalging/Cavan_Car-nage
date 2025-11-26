package game;

public class Car {
    static Item[] parts = new Item[4];

    static public void addPart(Item part) {
        for (int i = 0; i < parts.length; i++) {
            if (parts[i] == null) {
                parts[i] = part;
                return;
            }
        }
    }

    static public boolean fixed() {
        for (Item item : parts) {
            if (item == null) {
                return false;
            }
        }
        return true;
    }
}
