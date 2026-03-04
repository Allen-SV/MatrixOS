import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileSorter {

    private boolean moveFile(File folder, File file, String category) {

        File categoryFolder =
                new File(folder + "/" + category);

        if(!categoryFolder.exists())
            categoryFolder.mkdir();

        File movedFile =
                new File(categoryFolder +
                         "/" + file.getName());

        try {
            Files.move(file.toPath(),
                       movedFile.toPath(),
                       StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public boolean sortByTypeSingle(File folder, File file) {

        String category =
                CategoryManager.getCategory(file);

        return moveFile(folder, file, category);
    }

    public boolean sortByDateSingle(File folder, File file) {

        String date =
                FileUtility.getDateModified(file);

        return moveFile(folder, file, date);
    }

    public boolean sortBySizeSingle(File folder, File file) {

        String size =
                FileUtility.getSizeCategory(file);

        return moveFile(folder, file, size);
    }

    public boolean sortByCustomTypeSingle(File folder,
                                          File file,
                                          String customFolderName) {

        return moveFile(folder, file, customFolderName);
    }
}
