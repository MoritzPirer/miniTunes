package display;

import java.io.IOException;

public interface Display {

    /**
     * Prepares the display for usage. Should be the first function to be called on the object
     * @throws IOException if the display was already initialized or if something went wrong during setup
     */
    void init() throws IOException;

    /**
     * Releases any resources the Display had in use. Should be the last function called on the object
     * @throws IOException if the display was already destroyed or if something went wrong during cleanup
     */
    void destroy() throws IOException;

    /**
     * Sets the song title that should be displayed
     */
    void setTitle(String title);

    /**
     * Sets the song artist that should be displayed
     */
    void setArtist(String artist);

    /**
     * Sets the number of seconds played in the current song
     */
    void setElapsedTime(int elapsedSeconds);

    /**
     * Sets the total number of seconds of the current song
     */
    void setTotalTime(int totalSeconds);

    /**
     * Sets whether the player is paused so that the UI reflects the audio state
     */
    void setIsPaused(boolean isPaused);

    /**
     * Refresh the display. Values that were set since the last call to refresh are not guaranteed to
     * be visible until this method is called again
     */
    void refresh() throws IOException;

    /**
     * check for new input
     * @return an InputType value corresponding to the user input.
     * If no input has been detected, INVALID is returned.
     * closing the display is equivalent to entering the quit shortcut
     */
    InputType pollInput();
}
