package MusicPlayer;

import com.mpatric.mp3agic.*;

import java.io.File;

public class Song
{
    public String title;
    public String artist;
    public int totalSeconds;
    public int totalFrames;
    public String filePath;
    public double msPerFrame;

    public Song(String filePath) {
        this.filePath = filePath;

        this.title = new File(filePath).getName();
        this.artist = "Unknown artist";
        this.totalSeconds= 1;
        this.totalFrames = 1;
        this.msPerFrame = 26.12;

        try {
           Mp3File mp3File = new Mp3File(filePath);
           this.totalFrames = mp3File.getFrameCount();
           this.totalSeconds = (int) mp3File.getLengthInSeconds();
           this.msPerFrame = (1152.0 / mp3File.getSampleRate()) * 1000.0;
           parseMetadata(mp3File);
       } catch (Exception ignored) {}
    }

    /**
     * Read title and artist from metadata if present
     * @param mp3File the file to read from
     */
    private void parseMetadata(Mp3File mp3File) {
       if (mp3File.hasId3v1Tag()) {
           ID3v1 tag = mp3File.getId3v1Tag();
           if (tag.getTitle() != null && !tag.getTitle().isBlank()) {
               this.title = tag.getTitle().trim();
           }
           if (tag.getArtist() != null && !tag.getArtist().isBlank()) {
               this.artist = tag.getArtist().trim();
           }
       }
       else if (mp3File.hasId3v2Tag()) {
           ID3v2 tag = mp3File.getId3v2Tag();
           if (tag.getTitle() != null && !tag.getTitle().isBlank()) {
               this.title = tag.getTitle().trim();
           }
           if (tag.getArtist() != null && !tag.getArtist().isBlank()) {
               this.artist = tag.getArtist().trim();
           }
       }
    }
}
