import java.io.File;
import javax.swing.*;

public class SortWorker extends SwingWorker<Void, Void> {

    private File folder;
    private SortType method;
    private JLabel statusLabel;

    private boolean dateFilterEnabled;
    private int lastMinutes;
    private String timeUnit;
    private String filterMode;
    private long specificDate;

    private boolean customTypeEnabled;
    private String customFolderName;
    private String[] selectedExtensions;

    private String sizeMode;
    private String thresholdCondition;
    private long thresholdBytes;
    private long rangeFromBytes;
    private long rangeToBytes;
    private String thresholdFolderName;

    private int movedCount = 0;
    private int eligibleFiles = 0;

    public SortWorker(File folder, SortType method,
                      JProgressBar progressBar,
                      JLabel statusLabel,
                      boolean dateFilterEnabled,
                      int lastValue,
                      String timeUnit,
                      String filterMode,
                      long specificDate,
                      long fromDate,
                      long toDate,
                      boolean customTypeEnabled,
                      String customFolderName,
                      String[] selectedExtensions,
                      String sizeMode,
                      String thresholdCondition,
                      long thresholdBytes,
                      long rangeFromBytes,
                      long rangeToBytes,
                      String thresholdFolderName) {

        this.folder = folder;
        this.method = method;
        this.statusLabel = statusLabel;
        this.dateFilterEnabled = dateFilterEnabled;
        this.lastMinutes = lastValue;
        this.timeUnit = timeUnit;
        this.filterMode = filterMode;
        this.specificDate = specificDate;

        this.customTypeEnabled = customTypeEnabled;
        this.customFolderName = customFolderName;
        this.selectedExtensions = selectedExtensions;

        this.sizeMode = sizeMode;
        this.thresholdCondition = thresholdCondition;
        this.thresholdBytes = thresholdBytes;
        this.rangeFromBytes = rangeFromBytes;
        this.rangeToBytes = rangeToBytes;
        this.thresholdFolderName = thresholdFolderName;

        addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {

        File[] files = folder.listFiles();
        if(files == null) return null;

        FileSorter sorter = new FileSorter();

        for(File file : files) {

            if(file.isFile()) {

                boolean moved = false;
                boolean eligible = false;

                switch(method) {

                    case TYPE:

                        if(customTypeEnabled) {

                            String name = file.getName();
                            int dotIndex = name.lastIndexOf(".");

                            if(dotIndex != -1) {

                                String ext =
                                    name.substring(dotIndex+1).toLowerCase();

                                for(String selExt : selectedExtensions) {

                                    if(ext.equals(selExt.trim().toLowerCase())) {

                                        eligible = true;
                                        moved = sorter.sortByCustomTypeSingle(
                                                folder,file,customFolderName);
                                        break;
                                    }
                                }
                            }
                        }
                        else {
                            eligible = true;
                            moved = sorter.sortByTypeSingle(folder,file);
                        }

                        break;

                    case DATE:

                        long fileTime = file.lastModified();

                        if(!dateFilterEnabled){
                            eligible = true;
                            moved = sorter.sortByDateSingle(folder,file);
                        }

                        else if(filterMode.equals("LAST_TIME")) {

                            long allowedTime = 0;

                            if(timeUnit.equals("Minutes"))
                                allowedTime = lastMinutes * 60 * 1000;
                            else if(timeUnit.equals("Hours"))
                                allowedTime = lastMinutes * 60 * 60 * 1000;
                            else if(timeUnit.equals("Days"))
                                allowedTime = lastMinutes * 24 * 60 * 60 * 1000;

                            long difference =
                                System.currentTimeMillis() - fileTime;

                            if(difference <= allowedTime){
                                eligible = true;
                                moved = sorter.sortByDateSingle(folder,file);
                            }
                        }

                        else if(filterMode.equals("AFTER_DATE")) {

                            if(fileTime > specificDate){
                                eligible = true;
                                moved = sorter.sortByDateSingle(folder,file);
                            }
                        }

                        else if(filterMode.equals("BEFORE_DATE")) {

                            if(fileTime < specificDate){
                                eligible = true;
                                moved = sorter.sortByDateSingle(folder,file);
                            }
                        }

                        else if(filterMode.equals("ON_DATE")) {

                            long dayEnd =
                                specificDate + (24*60*60*1000);

                            if(fileTime >= specificDate
                               && fileTime < dayEnd){
                                eligible = true;
                                moved = sorter.sortByDateSingle(folder,file);
                            }
                        }

                        break;

                    case SIZE:

                        long fileSize = file.length();

                        if(sizeMode.equals("AUTO")){
                            eligible = true;
                            moved = sorter.sortBySizeSingle(folder,file);
                        }

                        else if(sizeMode.equals("THRESHOLD")) {

                            if(thresholdCondition.equals("GREATER")
                            && fileSize > thresholdBytes){

                                eligible = true;

                                moved = sorter.sortByCustomTypeSingle(
                                        folder,
                                        file,
                                        thresholdFolderName);
                            }

                            else if(thresholdCondition.equals("LESS")
                            && fileSize < thresholdBytes){

                                eligible = true;

                                moved = sorter.sortByCustomTypeSingle(
                                        folder,
                                        file,
                                        thresholdFolderName);
                            }
                        }

                        break;
                }

                if(eligible)
                    eligibleFiles++;

                if(moved)
                    movedCount++;

                int progress = 0;

                if(eligibleFiles > 0)
                    progress =
                    (int)(((double)movedCount / eligibleFiles) * 100);

                setProgress(progress);
            }
        }

        return null;
    }

    @Override
    protected void done() {

        if(movedCount == 0)
            statusLabel.setText("No files were sorted!");
        else
            statusLabel.setText("Sorting Completed!");
    }
}



