import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class Channel {
    public  final String name;
    private final List<String> songPaths;
    private int currentSongIndex;

    public Channel(String name, List<String> songPaths) {
        this.name = name;
        this.songPaths = songPaths;
        currentSongIndex = songPaths.size() - 1;
    }

    public String getNext() {
        if (currentSongIndex == songPaths.size() - 1) {
            currentSongIndex = 0;
        }
        else {
            currentSongIndex++;
        }

        return songPaths.get(currentSongIndex);
    }

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
