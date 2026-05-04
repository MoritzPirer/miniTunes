package Display;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import Utils.Format;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.awt.*;
import java.io.IOException;

public class LanternaDisplay implements Display {

    private Screen screen = null;
    private boolean isPaused = false;
    private String title = "unknown";
    private String artist = "unknown";
    private int elapsedSeconds = 0;
    private int totalSeconds = 0;
    private String channel = "";


    public LanternaDisplay() {}

    @Override
    public void init() throws IOException {
        if (screen != null) {
            throw new IOException("Double initialized!");
        }

        Font font = new Font("Courier New", Font.BOLD, 20);
        SwingTerminalFontConfiguration c = SwingTerminalFontConfiguration.newInstance(font);
        this.screen = new DefaultTerminalFactory()
            .setInitialTerminalSize(new TerminalSize(60, 11))
            .setTerminalEmulatorTitle("miniTunes")
            .setTerminalEmulatorFontConfiguration(c)
            .createScreen();

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
    public void setIsPaused(boolean isPaused) { this.isPaused = isPaused; }

    @Override
    public void setChannel(String channel) { this.channel = channel; }
    @Override
    public void refresh() throws IOException {
        screen.clear();
        screen.doResizeIfNecessary();
        int width = screen.getTerminalSize().getColumns() - 2; // -2 to have padding to the left / right edge

        TextGraphics textGraphics = screen.newTextGraphics()
            .setBackgroundColor(new TextColor.RGB(26,25,24))
            .setForegroundColor(new TextColor.RGB(244,224,216))
            .fill(' ');

        if (width < 20) {
            renderTooSmall(textGraphics, width);
        }
        else {
            renderContent(textGraphics, width);
        }

        screen.refresh();
    }

    final String verticalBorder = "║";
    final char horizontalBorder = '=';
    final String cornerBorder = "#";
    final String name = " miniTunes ";

    /**
     * renders a message telling the user that the screen is too narrow to properly display the interface.
     * Prevents math errors in display calculations
     * @param textGraphics the TextGraphics to write to
     * @param width the width of the display
     */
    private void renderTooSmall(TextGraphics textGraphics, int width) {
        int line = 0;
        textGraphics.putString(1,line++, Format.centerWithBorder(name, width, horizontalBorder, cornerBorder));
        textGraphics.putString(1,line++, Format.centerWithBorder("TOO NARROW!", width, ' ', verticalBorder));
        textGraphics.putString(1,line, Format.centerWithBorder("", width, horizontalBorder, cornerBorder));
    }

    /**
     * render the current meta information
     * @param textGraphics the TextGraphics to write to
     * @param width the width of the display
     */
    private void renderContent(TextGraphics textGraphics, int width) {
        int line = 1;
        textGraphics.putString(1,line++, Format.centerWithBorder(name, width, horizontalBorder, cornerBorder));
        textGraphics.putString(1,line++, Format.centerWithBorder(Format.rightAlign(channel, width - 2), width, ' ', verticalBorder));

        textGraphics.putString(1,line++, Format.centerWithBorder(title, width, ' ', verticalBorder));
        textGraphics.putString(1,line++, Format.centerWithBorder(artist, width, ' ', verticalBorder));
        textGraphics.putString(1,line++, Format.centerWithBorder(progressBar(width - 2), width, ' ', verticalBorder));

        String symbols =  "↩︎ |<<  " + (isPaused? "▶" : "⏸")+ "  >>|  ⇌";
        String controls = "[R] [B] [P] [S] [C]";
        textGraphics.putString(1,line++, Format.centerWithBorder(symbols, width, ' ', verticalBorder));
        textGraphics.putString(1,line++, Format.centerWithBorder(controls, width, ' ', verticalBorder));

        textGraphics.putString(1,line++, Format.centerWithBorder("", width, ' ', verticalBorder));
        textGraphics.putString(1,line, Format.centerWithBorder("", width, horizontalBorder, cornerBorder));
    }

    /**
     * Renders the progress bar based on the elapsed and total time of the song
     * @param width the width the progress bar can have including the timestamps
     * @return the formatted progress bar
     */
    private String progressBar(int width) {
        final char progressBarFill = '▉';
        final char progressBarBlank = '▁';//'│';
        final char progressBarStart = '[';
        final char progressBarEnd = ']';

        String total = Format.time(totalSeconds);
        String elapsed = Format.rightAlign(Format.time(elapsedSeconds), total.length());

        int barWidth = width - elapsed.length() - total.length() - 6; // 2 for padding, 2 for [ and ]
        float progressPercent = elapsedSeconds / (float) totalSeconds;

        int filledAmount = (int) (progressPercent * barWidth);
        String filled = Character.toString(progressBarFill).repeat(filledAmount);

        int blankAmount = Math.max(barWidth - filled.length(), 0);
        String blank = Character.toString(progressBarBlank).repeat(blankAmount);

        return elapsed +
            ' ' +
            progressBarStart +
            filled +
            blank +
            progressBarEnd +
            ' ' +
            total;
    }

    @Override
    public InputType pollInput() {
        try {
            KeyStroke input = screen.pollInput();
            if (input == null) {
                return InputType.INVALID;
            }
            if (input.getKeyType() == KeyType.EOF) {
                return InputType.QUIT;
            }
            Character inputChar = input.getCharacter();
            if (inputChar == null) {
                return InputType.INVALID;
            }
            return switch (Character.toLowerCase(inputChar)) {
                case 'r' -> InputType.RESTART;
                case 'b' -> InputType.BACK;
                case 'p' -> InputType.PLAY_PAUSE;
                case 'c' -> InputType.CHANNEL;
                case 'q' -> InputType.QUIT;
                case 's' -> InputType.SKIP;
                default -> InputType.INVALID;
            };
        } catch (IOException e) {
            return InputType.INVALID;
        }
    }
}
