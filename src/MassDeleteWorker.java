import java.io.File;
import javax.swing.*;

public class MassDeleteWorker
        extends SwingWorker<Void, Void> {

    private File folder;
    private JProgressBar progressBar;
    private JLabel statusLabel;

    private String nameContains;
    private String fileType;
    private String sizeCondition;
    private long sizeBytes;
    private String dateCondition;
    private long dateValue;

    public MassDeleteWorker(File folder,
                            JProgressBar progressBar,
                            JLabel statusLabel,
                            String nameContains,
                            String fileType,
                            String sizeCondition,
                            long sizeBytes,
                            String dateCondition,
                            long dateValue) {

        this.folder = folder;
        this.progressBar = progressBar;
        this.statusLabel = statusLabel;
        this.nameContains = nameContains;
        this.fileType = fileType;
        this.sizeCondition = sizeCondition;
        this.sizeBytes = sizeBytes;
        this.dateCondition = dateCondition;
        this.dateValue = dateValue;

        addPropertyChangeListener(evt -> {
            if ("progress".equals(
                evt.getPropertyName())) {

                progressBar.setValue(
                    (Integer)evt.getNewValue());
            }
        });
    }

    @Override
    protected Void doInBackground()
            throws Exception {

        File[] files = folder.listFiles();
        if(files == null) return null;

        int total = files.length;
        int count = 0;

        for(File file : files) {

            if(file.isFile()) {

                boolean delete = true;

                // NAME CHECK
                if(!nameContains.isEmpty()) {

                    if(!file.getName()
                        .toLowerCase()
                        .contains(
                        nameContains.toLowerCase()))
                        delete = false;
                }

                // TYPE CHECK
                if(!fileType.isEmpty()) {

                    String name =
                        file.getName();
                    int dot =
                        name.lastIndexOf(".");

                    if(dot != -1) {

                        String ext =
                          name.substring(dot+1);

                        if(!ext.equalsIgnoreCase(
                            fileType))
                            delete = false;
                    }
                }

                // SIZE CHECK
                if(sizeBytes != 0) {

                    long fSize =
                        file.length();

                    if(sizeCondition.equals("GT")
                       && !(fSize > sizeBytes))
                        delete = false;

                    if(sizeCondition.equals("LT")
                       && !(fSize < sizeBytes))
                        delete = false;
                }

                // DATE CHECK
                if(dateValue != 0) {

                    long fDate =
                        file.lastModified();

                    if(dateCondition.equals("BEFORE")
                       && !(fDate < dateValue))
                        delete = false;

                    if(dateCondition.equals("AFTER")
                       && !(fDate > dateValue))
                        delete = false;
                }

                if(delete)
                    file.delete();
            }

            count++;
            int progress =
              (int)(((double)count
                     / total) * 100);

            setProgress(progress);
        }

        return null;
    }

    @Override
    protected void done() {
        statusLabel.setText(
            "Mass Delete Completed!");
    }
}