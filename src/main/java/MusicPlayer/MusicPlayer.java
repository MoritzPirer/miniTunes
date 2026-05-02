package MusicPlayer;

public interface MusicPlayer {

    /**
     * start playing the given song in a way that doesn't block the calling thread
     * @param song the song to play
     */
    void play(Song song);

    /**
     * if the MusicPlayer is currently playing, pause it. If it is currently paused,
     * resume playing where it was paused
     */
    void togglePause();

    /**
     * @return true if the playback is paused but the song is not yet over,
     * false if it is playing or finished
     */
    boolean isPaused();

    /**
     * @return true if the playback of the given song has finished
     */
    boolean isFinished();

    /**
     * stop the current playback regardless of state and bring the MusicPlayer into a state where
     * a new song can be started
     */
    void stop();

    /**
     * @return how many seconds of the song have been played
     */
    int getElapsedSeconds();

}
