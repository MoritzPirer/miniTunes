import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileLoader {
    private static final String baseFolder = "src/main/resources"; // TODO: maybe folder outside of project

    /**
     * collects all subfolders of baseFolder along with the .mp3 files in them
     * @return a map where the keys are the names of the subfolders and the values are
     *   lists of filepaths to .mp3 files in that subfolder
     */
    public static Map<String, List<String>> getSubfolders() {
        List<String> subfolderNames = findSubfolders();
        Map<String, List<String>> subfolders = new HashMap<>();

        for (String current : subfolderNames) {
            List<String> subfolderContents = getFolderContents(current);
            if (!subfolderContents.isEmpty()) { // disregard empty subfolders
                subfolders.put(current, getFolderContents(current));
            }
        }

        return subfolders;
    }

    /**
     * @return a list of all folders in baseFolder
     */
    private static List<String> findSubfolders() {
        List<String> subfolders = new ArrayList<>();
        File folder = new File(baseFolder);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            return subfolders;
        }

        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                subfolders.add(file.getName());
            }
        }

        return subfolders;
    }

    /**
     * collects the file paths of all .mp3 files in a folder in a list
     * @param subfolderName the folder to search through
     * @return a list of file paths to .mp3 files
     */
    private static List<String> getFolderContents(String subfolderName) {
        List<String> subfolderContents = new ArrayList<>();
        File folder = new File(baseFolder + "/" + subfolderName);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            return subfolderContents;
        }
        for (File file : listOfFiles) {
            if (!file.isFile()) {
                continue;
            }
            if (!file.getName().endsWith(".mp3")) {
                continue;
            }
            subfolderContents.add(file.getAbsolutePath());
        }

        subfolderContents.sort(String::compareTo);
        return subfolderContents;
    }
}
