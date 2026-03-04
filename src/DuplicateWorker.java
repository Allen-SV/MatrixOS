import java.io.File;
import javax.swing.*;

public class DuplicateWorker
        extends SwingWorker<Void, Void> {

    private File folder;
    private JProgressBar progressBar;
    private JLabel statusLabel;

    public DuplicateWorker(File folder,
                           JProgressBar progressBar,
                           JLabel statusLabel) {

        this.folder = folder;
        this.progressBar = progressBar;
        this.statusLabel = statusLabel;

        addPropertyChangeListener(evt -> {

            if("progress".equals(
                    evt.getPropertyName())) {

                progressBar.setValue(
                        (Integer)evt.getNewValue());
            }
        });
    }

    @Override
    protected Void doInBackground()
            throws Exception {

        DuplicateDetector detector =
                new DuplicateDetector();

        File[] files = folder.listFiles();

        if(files == null)
            return null;

        int total = files.length;
        int count = 0;

        for(File file : files) {

            if(file.isFile()) {

                detector.processFile(file);
            }

            count++;

            int progress =
                    (int)(((double)count
                            / total) * 100);

            setProgress(progress);
        }

        detector.finishProcess(folder);

        return null;
    }

    @Override
    protected void done() {

        statusLabel.setText(
                "Duplicate Detection Completed!");
    }
}
