import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.*;

public class DuplicateDetector {

    Map<Long, List<File>> sizeMap =
            new HashMap<>();

    Map<String, List<File>> hashMap =
            new HashMap<>();

    public void processFile(File file) {

        long size = file.length();

        if(!sizeMap.containsKey(size)) {

            sizeMap.put(size,
                    new ArrayList<>());
        }

        sizeMap.get(size).add(file);
    }

    public void finishProcess(File folder) {

        for(List<File> sameSizeFiles :
                sizeMap.values()) {

            if(sameSizeFiles.size() > 1) {

                for(File file :
                        sameSizeFiles) {

                    try {

                        String hash =
                                getMD5Hash(file);

                        if(!hashMap.containsKey(
                                hash)) {

                            hashMap.put(hash,
                                    new ArrayList<>());
                        }

                        hashMap.get(hash)
                                .add(file);

                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        File duplicateFolder =
                new File(folder,
                        "Duplicates");

        if(!duplicateFolder.exists())
            duplicateFolder.mkdir();

        for(List<File> duplicateFiles :
                hashMap.values()) {

            if(duplicateFiles.size() > 1) {

                File mainFile =
                        getMainFile(
                                duplicateFiles);

                String subFolderName =
                        mainFile.getName()
                                +"_duplicates";

                File subFolder =
                        new File(
                                duplicateFolder,
                                subFolderName);

                if(!subFolder.exists())
                    subFolder.mkdir();

                for(File dupFile :
                        duplicateFiles) {

                    if(!dupFile.equals(
                            mainFile)) {

                        try {

                            Files.move(
                                    dupFile.toPath(),
                                    new File(
                                            subFolder,
                                            dupFile.getName()
                                    ).toPath(),
                                    StandardCopyOption.REPLACE_EXISTING
                            );

                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private File getMainFile(
            List<File> files) {

        for(File f : files) {

            String name =
                    f.getName()
                            .toLowerCase();

            if(!name.contains("copy")
                    && !name.matches(
                    ".*\\(\\d+\\).*")) {

                return f;
            }
        }

        return files.get(0);
    }

    private String getMD5Hash(
            File file)
            throws Exception {

        MessageDigest md =
                MessageDigest
                        .getInstance("MD5");

        FileInputStream fis =
                new FileInputStream(file);

        byte[] byteArray =
                new byte[1024];

        int bytesCount;

        while((bytesCount =
                fis.read(byteArray))
                != -1) {

            md.update(byteArray,
                    0,
                    bytesCount);
        }

        fis.close();

        byte[] bytes =
                md.digest();

        StringBuilder sb =
                new StringBuilder();

        for(byte b : bytes) {

            sb.append(
                    String.format(
                            "%02x", b));
        }

        return sb.toString();
    }
}