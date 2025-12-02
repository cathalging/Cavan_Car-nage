package game;

import java.util.concurrent.ThreadLocalRandom;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public static Direction random() {
        Direction[] values = Direction.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
