import java.util.*;

public class Radio {

    private final MusicPlayer musicPlayer;
    private final List<Channel> channels;
    private int currentCannel = 0;
    private Song currentSong = null;

    public Radio() {
        Map<String, List<String>> channelFiles = FileLoader.getSubfolders();
        channels = new ArrayList<>();

        for (String channelName : channelFiles.keySet()) {
            channels.add(new Channel(channelName, channelFiles.get(channelName)));
        }

        musicPlayer = new MusicPlayer();
    }

    @SuppressWarnings("unused") //args is not used
    static void main(String... args) {
        Radio radio = new Radio();
        radio.startRadio();
    }

    private void startRadio() {

        nextSong();

        boolean running = true;
        char input;
        while (running) {
            System.out.println("Artist: " + currentSong.artist);
            System.out.println("Title: " + currentSong.title);
            System.out.println("Duration: " + musicPlayer.getTotalTime());
            System.out.println("Elapsed: " + musicPlayer.getElapsedTime());

            Scanner scanner = new Scanner(System.in);
            input = scanner.next().charAt(0);

            if (musicPlayer.isFinished()) {
                nextSong();
            }

            switch(Character.toLowerCase(input)) {
                case 'p': // play / pause
                    musicPlayer.togglePause();
                    break;
                case 's': { // skip
                    nextSong();
                    break;
                }
                case 'b': { // back
                    previousSong();
                    break;
                }
                case 'r': { // restart
                    restartSong();
                    break;
                }
                case 'c': { // change channel
                    nextChannel();
                    break;
                }
                case 'm': { // mute / unmute
                    //TODO: check if JavaZoom supports volume / muting, otherwise simulate muting with pause / elapsed time tricks
                    break;
                }
                case 'q': { // quit
                    running = false;
                    musicPlayer.stop();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void nextSong() {
        musicPlayer.stop();
        String songPath = channels.get(currentCannel).getNext();
        currentSong = new Song(songPath);
        musicPlayer.play(currentSong);
    }

    private void previousSong() {
        musicPlayer.stop();
        String songPath = channels.get(currentCannel).getPrevious();
        currentSong = new Song(songPath);
        musicPlayer.play(currentSong);
    }

    private void restartSong() {
        musicPlayer.stop();
        musicPlayer.play(currentSong);
    }

    private void nextChannel() {
        currentCannel++;
        if (currentCannel > channels.size()) {
            currentCannel = 0;
        }
        nextSong();
    }
}
