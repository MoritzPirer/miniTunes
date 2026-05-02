package MusicPlayer;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.FileInputStream;

public class JavaZoomMusicPlayer extends PlaybackListener implements MusicPlayer{
    public enum PlayerState { RUNNING, PAUSED, FINISHED }
    private volatile PlayerState state = PlayerState.RUNNING;

    private AdvancedPlayer player = null;
    private Song song = null;
    private long lastStartTime = 0;
    private long totalTimeMillis = 0;

    public JavaZoomMusicPlayer() {}

    @Override
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
     * start a background thread to run the AdvancedPlayer
     */
    private void startThread() {
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

    @Override
    public void togglePause() {
        if (state == PlayerState.PAUSED) {
            resume();
        }
        else if (state == PlayerState.RUNNING) {
            pause();
        }
    }

    private void pause() {
        if (state == PlayerState.RUNNING && player != null) {
            state = PlayerState.PAUSED;
            player.stop(); // triggers playbackFinished()
        }
    }

    private void resume() {
        if (state == PlayerState.PAUSED && player != null) {
            startThread();
        }
    }

    @Override
    public boolean isPaused() {
        return state == PlayerState.PAUSED;
    }

    @Override
    public boolean isFinished() {
        return state == PlayerState.FINISHED;
    }

    @Override
    public void stop() {
        state = PlayerState.FINISHED;
        if (player != null) {
            player.close();
        }
        this.totalTimeMillis = 0;
        this.lastStartTime = 0;
        this.song = null;
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

    @Override
    public int getElapsedSeconds() {
        long currentElapsed = totalTimeMillis;
        if (state == PlayerState.RUNNING) {
            currentElapsed += (System.currentTimeMillis() - lastStartTime);
        }

        return (int) currentElapsed / 1000;
    }
}
