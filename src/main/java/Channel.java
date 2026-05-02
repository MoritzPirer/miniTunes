import java.util.List;

public class Channel {
    public  final String name;
    private final List<String> songPaths;
    private int currentSongIndex;

    public Channel(String name, List<String> songPaths) throws InstantiationError {
        this.name = name;
        this.songPaths = songPaths;
        currentSongIndex = 0;
    }

    /**
     * @return the file path to the first song in the channel
     */
    public String getFirst() {
        currentSongIndex = 0;
        return songPaths.get(currentSongIndex);
    }

    /**
     * @return the file path the next song in the channel, looping if necessary
     */
    public String getNext() {
        if (currentSongIndex == songPaths.size() - 1) {
            currentSongIndex = 0;
        }
        else {
            currentSongIndex++;
        }

        return songPaths.get(currentSongIndex);
    }

    /**
     * @return the file path to the previous song in the channel, looping if necessary
     */
    public String getPrevious() {
        if (currentSongIndex == 0) {
            currentSongIndex = songPaths.size() - 1;
        }
        else {
            currentSongIndex--;
        }

        return songPaths.get(currentSongIndex);
    }
}
