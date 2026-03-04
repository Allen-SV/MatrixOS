import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtility {

    public static String getDateModified(File file) {

        Date date = new Date(file.lastModified());
        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM");

        return sdf.format(date);
    }

    public static String getSizeCategory(File file) {

        long size = file.length()/(1024*1024);

        if(size < 5)
            return "Small Files";
        else if(size < 50)
            return "Medium Files";
        else
            return "Large Files";
    }
}

