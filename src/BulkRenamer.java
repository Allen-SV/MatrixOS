import java.io.File;

public class BulkRenamer {

    public void addNumber(File file,
                          String baseName,
                          int number) {

        String name = file.getName();
        int dot = name.lastIndexOf(".");

        String ext =
            (dot != -1)
            ? name.substring(dot)
            : "";

        File parent = file.getParentFile();

        File renamed =
            new File(parent,
                     baseName + "_" +
                     number + ext);

        file.renameTo(renamed);
    }
}