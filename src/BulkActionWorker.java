import java.io.File;
import javax.swing.*;

public class BulkActionWorker
        extends SwingWorker<Void, Void> {

    private File folder;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private String baseName;

    public BulkActionWorker(File folder,
                            JProgressBar progressBar,
                            JLabel statusLabel,
                            String baseName) {

        this.folder = folder;
        this.progressBar = progressBar;
        this.statusLabel = statusLabel;
        this.baseName = baseName;

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

        if(files == null)
            return null;

        int total = files.length;
        int count = 0;
        int number = 1;

        BulkRenamer renamer = new BulkRenamer();

        for(File file : files) {

            if(file.isFile()) {

                renamer.addNumber(
                    file,
                    baseName,
                    number
                );

                number++;
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
            "Bulk Rename Completed!");
    }
}