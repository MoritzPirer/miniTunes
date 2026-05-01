import java.io.IOException;

public interface Display {
    void init() throws IOException;
    void destroy() throws IOException;

    void setTitle(String title);
    void setArtist(String artist);
    void setElapsedTime(int elapsedSeconds);
    void setTotalTime(int totalSeconds);
    void setIsPaused(boolean isPaused);
    void refresh();
    InputType pollInput();
}
