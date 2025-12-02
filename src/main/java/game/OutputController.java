package game;

import java.io.Serializable;

public class OutputController implements Serializable {
    private static StringBuilder textBuffer = new StringBuilder();



    public void addText(String text) {
        textBuffer.append(text).append("\n");
        //System.out.println(textBuffer.toString());
    }

    public void clearText() {
        textBuffer.setLength(0);
    }

    public String getText() {
        return textBuffer.toString();
    }

    public void clearConsole() {
        textBuffer = new StringBuilder();
    }
}

