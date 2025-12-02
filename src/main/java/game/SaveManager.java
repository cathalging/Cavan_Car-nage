package game;

import java.io.*;

public class SaveManager {
    private static String fileName;

    public static void saveGame(GameState gameState, String fileName) {
        fileName = fileName + ".dat";

        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
            objectOutputStream.writeObject(gameState);
            objectOutputStream.close();

            new OutputController().addText("Game Saved Successfully.");
        } catch (Exception e) {
            new OutputController().addText("Game Save Failed.");
            e.printStackTrace();
        }
    }

    public static GameState loadGame(File fileIn) {
        if (fileIn == null) {
            return null;
        }

        try {
            FileInputStream file = new FileInputStream(fileIn);
            ObjectInputStream objectInputStream = new ObjectInputStream(file);

            return (GameState) objectInputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
