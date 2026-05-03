package MusicPlayer;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaZoomMusicPlayer extends PlaybackListener implements MusicPlayer{
    public enum PlayerState { RUNNING, PAUSED, FINISHED }
    private volatile PlayerState state = PlayerState.RUNNING;

    private volatile AdvancedPlayer player = null;
    private volatile Song song = null;
    private long lastStartTime = 0;
    private long totalTimeMillis = 0;
    private Thread playbackThread = null;
    private final AtomicInteger playbackId;
    public JavaZoomMusicPlayer() {playbackId = new AtomicInteger(0);}

    @Override
    public void play(Song song) {
        state = PlayerState.RUNNING;
        this.song = song;
        lastStartTime = 0;
        totalTimeMillis = 0;

        startThread();
    }

    /**
     * start a background thread to run the AdvancedPlayer
     */
    private void startThread() {
        state = PlayerState.RUNNING;
        lastStartTime = System.currentTimeMillis();
        int threadId = playbackId.getAndIncrement();

        playbackThread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(song.filePath)) {
                player = new AdvancedPlayer(fis);
                player.setPlayBackListener(this);

                int startFrame = (int) (totalTimeMillis / song.msPerFrame);
                player.play(startFrame, Integer.MAX_VALUE);

                // playback ended without interruption
                if (threadId == playbackId.get() && state != PlayerState.PAUSED) {
                    state = PlayerState.FINISHED;
                }
            }
            catch (Exception _) {
                state = PlayerState.FINISHED;
            }
        });
        playbackThread.start();
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
            playbackThread.interrupt();
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
        if (playbackThread != null && playbackThread.isAlive()) {
            playbackThread.interrupt();
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
