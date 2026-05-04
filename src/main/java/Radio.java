import MusicPlayer.JavaZoomMusicPlayer;
import MusicPlayer.Song;
import Display.Display;
import Display.InputType;
import Display.LanternaDisplay;
import Utils.FileLoader;

import java.io.IOException;
import java.util.*;

public class Radio {

    @SuppressWarnings("unused")
    static void main(String... args) {
        Radio radio = new Radio();
        radio.startRadio();
    }

    private final JavaZoomMusicPlayer musicPlayer;
    private final List<Channel> channels;
    private int currentCannel = 0;
    private Song currentSong = null;
    private boolean isRunning = true;

    public Radio() {
        Map<String, List<String>> channelFiles = FileLoader.getSubfolders();
        channels = new ArrayList<>();

        for (String channelName : channelFiles.keySet()) {
            try {
                channels.add(new Channel(channelName, channelFiles.get(channelName)));
            } catch (InstantiationError _) {} // ensure all channels have at least one song
        }

        musicPlayer = new JavaZoomMusicPlayer();
    }

    /**
     * set up the display and start the main loop
     */
    private void startRadio() {
        Display display = new LanternaDisplay();

        try {
            display.init();
            musicPlayer.stop();
            String songPath = channels.get(currentCannel).getFirst();
            currentSong = new Song(songPath);
            musicPlayer.play(currentSong);

            mainLoop(display);

            display.destroy();
        } catch (IOException e) {
            processInput(InputType.QUIT);
        }
    }

    /**
     * continuously update the display, fetch & execute input until the user quits
     * @param display the display to write output to and fetch input from
     */
    void mainLoop(Display display) {
        while (isRunning) {

            setDisplay(display);

            InputType input = display.pollInput();
            processInput(input);

            if (musicPlayer.isFinished()) {
                nextSong();
            }
        }
    }

    /**
     * update the information to be displayed
     * @param display the display to output to
     */
    void setDisplay(Display display) {
        display.setTitle(currentSong.title);
        display.setArtist(currentSong.artist);
        display.setTotalTime(currentSong.totalSeconds);
        display.setElapsedTime(musicPlayer.getElapsedSeconds());
        display.setIsPaused(musicPlayer.isPaused());
        display.setChannel(channels.get(currentCannel).name);
        try {
            display.refresh();
        }
        catch (IOException e) {
            processInput(InputType.QUIT);
        }
    }

    /**
     * execute the action associated with the user input
     * @param input the input from the user
     */
    void processInput(InputType input) {
        switch(input) {
            case InputType.PLAY_PAUSE:
                musicPlayer.togglePause();
                break;
            case InputType.SKIP: {
                nextSong();
                break;
            }
            case InputType.BACK: {
                previousSong();
                break;
            }
            case InputType.RESTART: {
                restartSong();
                break;
            }
            case InputType.CHANNEL: {
                nextChannel();
                break;
            }
            case InputType.QUIT: {
                isRunning = false;
                musicPlayer.stop();
                System.exit(0);
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * stop the current song and start playing the next one in the current channel
     */
    private void nextSong() {
        System.out.println("DEBUG: nextSong called");
        musicPlayer.stop();
        String songPath = channels.get(currentCannel).getNext();
        currentSong = new Song(songPath);
        musicPlayer.play(currentSong);
    }

    /**
     * stop the current song and start playing the previous song in the current channel
     */
    private void previousSong() {
        musicPlayer.stop();
        String songPath = channels.get(currentCannel).getPrevious();
        currentSong = new Song(songPath);
        musicPlayer.play(currentSong);
    }

    /**
     * start playing the current song again from the start
     */
    private void restartSong() {
        musicPlayer.stop();
        musicPlayer.play(currentSong);
    }

    /**
     * start playing the next channel in the channel list
     */
    private void nextChannel() {
        currentCannel++;
        if (currentCannel >= channels.size()) {
            currentCannel = 0;
        }

        musicPlayer.stop();
        String songPath = channels.get(currentCannel).getFirst();
        currentSong = new Song(songPath);
        musicPlayer.play(currentSong);
    }
}
