import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.FileInputStream;

public class MusicPlayer extends PlaybackListener {
    public enum PlayerState { RUNNING, PAUSED, FINISHED };
    private volatile PlayerState state = PlayerState.RUNNING;

    private AdvancedPlayer player = null;
    private Song song = null;
    private long lastStartTime = 0;
    private long totalTimeMillis = 0;

    MusicPlayer() {}

    /**
     * start playing the given song
     * @param song the song to play
     */
    public void play(Song song) {
        if (song != null) {
            stop();
        }

        this.song = song;
        lastStartTime = 0;
        totalTimeMillis = 0;
        state = PlayerState.RUNNING;

        startThread();
    }

    /**
     * if currently playing a song, the song is paused. If currently paused, the song is resumed
     */
    public void togglePause() {
        if (state == PlayerState.PAUSED) {
            resume();
        }
        else if (state == PlayerState.RUNNING) {
            pause();
        }
    }

    public void pause() {
        if (state == PlayerState.RUNNING && player != null) {
            state = PlayerState.PAUSED;
            player.stop(); // triggers playbackFinished()
        }
    }

    public void resume() {
        if (state == PlayerState.PAUSED && player != null) {
            startThread();
        }
    }

    /**
     * start a background thread to run the AdvancedPlayer
     */
    private void startThread() {
        System.out.println("starting or resuming play of: " + song.filePath);
        state = PlayerState.RUNNING;
        lastStartTime = System.currentTimeMillis();

        new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(song.filePath)) {
                player = new AdvancedPlayer(fis);
                player.setPlayBackListener(this);

                int startFrame = (int) (totalTimeMillis / song.msPerFrame);
                player.play(startFrame, Integer.MAX_VALUE);

                // playback ended without interruption
                if (state != PlayerState.PAUSED) {
                    state = PlayerState.FINISHED;
                }
            }
            catch (Exception _) {
                state = PlayerState.FINISHED;
            }
        }).start();
    }


    /**
     * stops playback and resets the MusicPlayer
     */
    public void stop() {
        state = PlayerState.FINISHED;
        if (player != null) {
            player.close();
        }
        this.totalTimeMillis = 0;
        this.lastStartTime = 0;
        this.song = null;
    }

    public boolean isFinished() {
        return state == PlayerState.FINISHED;
    }

    /**
     * called by AdvancedPlayer when playback ends or is interrupted
     */
    @Override
    public void playbackFinished(PlaybackEvent event) {
        if (state == PlayerState.PAUSED) {
            totalTimeMillis += (System.currentTimeMillis() - lastStartTime);
        }
    }

    /**
     * @return the already elapsed time of the current song formatted as H:MM:SS or MM:SS
     */
    public String getElapsedTime() {
        long currentElapsed = totalTimeMillis;
        if (state == PlayerState.RUNNING) {
            currentElapsed += (System.currentTimeMillis() - lastStartTime);
        }

        return formatTime(currentElapsed / 1000);
    }

    /**
     * @return the total duration of the current song formatted as H:MM:SS or MM:SS
     */
    public String getTotalTime() {
        return formatTime(song.totalSeconds);
    }

    private String formatTime(long totalSeconds) {
        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) (totalSeconds / 60) % 60;
        int seconds = (int) (totalSeconds % 60);

        if (hours > 0) {
           return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }
}
