import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import javax.swing.*;
import java.io.IOException;

public class LanternaDisplay implements Display {

    private Screen screen = null;
    private boolean isPaused = false;
    private String title = "unknown";
    private String artist = "unknown";
    private int elapsedSeconds = 0;
    private int totalSeconds = 0;

    public LanternaDisplay() {}

    @Override
    public void init() throws IOException {
        if (screen == null) {
            throw new IOException("Double initialized!");
        }

        this.screen = new DefaultTerminalFactory().createScreen();
        this.screen.startScreen();
        this.screen.setCursorPosition(null);
    }

    @Override
    public void destroy() throws IOException {
        if (screen == null) {
            throw new IOException("Double destroyed!");
        }

        screen.stopScreen();
        screen.close();
        screen = null;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public void setElapsedTime(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    @Override
    public void setTotalTime(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    @Override
    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    @Override
    public void refresh() {
        //TODO
    }

    @Override
    public InputType pollInput() {
        try {
            KeyStroke input = screen.pollInput();
            if (input == null) {
                return InputType.INVALID;
            }

            return switch (input.getCharacter()) {
                case 'r' -> InputType.RESTART;
                case 'b' -> InputType.BACK;
                case 'p' -> InputType.PLAY_PAUSE;
                case 'c' -> InputType.CHANNEL;
                case 'q' -> InputType.QUIT;
                default -> InputType.INVALID;
            };
        } catch (IOException e) {
            return InputType.INVALID;
        }
    }
}
