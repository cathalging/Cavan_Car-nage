public class OutputController {
    private StringBuilder textBuffer;

    public OutputController() {
        textBuffer = new StringBuilder();
    }

    public void addText(String text) {
        textBuffer.append(text).append("\n");
        clearConsole();
        System.out.println(textBuffer.toString());
    }

    public void clearText() {
        textBuffer.setLength(0);
    }

    public String getText() {
        return textBuffer.toString();
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

