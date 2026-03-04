import java.io.File;
import java.util.HashMap;

public class CategoryManager {

    private static HashMap<String, String> extensionMap =
            new HashMap<String, String>();

    static {

        extensionMap.put(".pdf", "Documents");
        extensionMap.put(".docx", "Documents");
        extensionMap.put(".txt", "Documents");

        extensionMap.put(".jpg", "Images");
        extensionMap.put(".png", "Images");

        extensionMap.put(".mp4", "Videos");

        extensionMap.put(".mp3", "Music");

        extensionMap.put(".zip", "Archives");
    }

    public static String getCategory(File file) {

        String name = file.getName().toLowerCase();

        int dotIndex = name.lastIndexOf(".");

        if(dotIndex == -1)
            return "Others";

        String ext = name.substring(dotIndex);

        if(extensionMap.containsKey(ext))
            return extensionMap.get(ext);
        else
            return "Others";
    }
}

