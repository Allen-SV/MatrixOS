import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class FileSorterSwingGUI extends AppFrame implements ActionListener {

    JTextField pathField;
    ModernButton browseBtn, sortBtn, backBtn;
    JRadioButton typeRB, dateRB, sizeRB;
    JProgressBar progressBar;
    JLabel statusLabel;

    public FileSorterSwingGUI(int state) {

        setSize(800,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(state);

        GradientPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        GlassPanel card = new GlassPanel();
        card.setPreferredSize(new Dimension(600,400));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // ---------------- TOP PANEL (BACK BUTTON) ----------------

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,60));

        // padding panel (THIS gives spacing)
        JPanel backWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT,10, 10));
        backWrapper.setOpaque(false);

        backBtn = new ModernButton("←");
        backBtn.setPreferredSize(new Dimension(60,40));
        backBtn.addActionListener(this);

        backWrapper.add(backBtn);

        topPanel.add(backWrapper, BorderLayout.WEST);

        // ---------------- TITLE ----------------

        JLabel title = new JLabel("Select Folder to Organize");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);

        // ---------------- PATH FIELD ----------------

        pathField = new JTextField();
        pathField.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        pathField.setBackground(Color.WHITE);
        pathField.setForeground(Color.BLACK);
        pathField.setOpaque(true);

        browseBtn = new ModernButton("Browse");
        browseBtn.addActionListener(this);

        JPanel pathPanel = new JPanel();
        pathPanel.setOpaque(false);
        pathPanel.setLayout(new BoxLayout(pathPanel, BoxLayout.X_AXIS));
        pathPanel.setMaximumSize(new Dimension(500,40));

        pathField.setMaximumSize(new Dimension(350,35));
        browseBtn.setMaximumSize(new Dimension(120,35));

        pathPanel.add(pathField);
        pathPanel.add(Box.createHorizontalStrut(20));
        pathPanel.add(browseBtn);

        // ---------------- RADIO BUTTONS ----------------

        JLabel methodLabel = new JLabel("Sorting Method");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        methodLabel.setForeground(Color.WHITE);
        methodLabel.setAlignmentX(CENTER_ALIGNMENT);

        typeRB = new JRadioButton("By File Type", true);
        dateRB = new JRadioButton("By Date Modified");
        sizeRB = new JRadioButton("By File Size");

        typeRB.setOpaque(false);
        dateRB.setOpaque(false);
        sizeRB.setOpaque(false);

        typeRB.setForeground(Color.WHITE);
        dateRB.setForeground(Color.WHITE);
        sizeRB.setForeground(Color.WHITE);

        ButtonGroup group = new ButtonGroup();
        group.add(typeRB);
        group.add(dateRB);
        group.add(sizeRB);

        JPanel rbPanel = new JPanel();
        rbPanel.setOpaque(false);
        rbPanel.setLayout(new BoxLayout(rbPanel, BoxLayout.Y_AXIS));
        rbPanel.setAlignmentX(CENTER_ALIGNMENT);

        typeRB.setAlignmentX(CENTER_ALIGNMENT);
        dateRB.setAlignmentX(CENTER_ALIGNMENT);
        sizeRB.setAlignmentX(CENTER_ALIGNMENT);

        rbPanel.add(typeRB);
        rbPanel.add(dateRB);
        rbPanel.add(sizeRB);

        // ---------------- SORT BUTTON ----------------

        sortBtn = new ModernButton("Sort Files");
        sortBtn.setMaximumSize(new Dimension(200,50));
        sortBtn.setAlignmentX(CENTER_ALIGNMENT);
        sortBtn.addActionListener(this);

        progressBar = new JProgressBar();
        progressBar.setMaximumSize(new Dimension(500,20));

        statusLabel = new JLabel("Status: Ready");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);

        // ---------------- ADDING TO CARD ----------------

        card.add(topPanel);
        card.add(Box.createVerticalStrut(-6));
        card.add(title);
        card.add(Box.createVerticalStrut(12));
        card.add(pathPanel);
        card.add(Box.createVerticalStrut(10));
        card.add(methodLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(rbPanel);
        card.add(Box.createVerticalStrut(15));
        card.add(sortBtn);
        card.add(Box.createVerticalStrut(15));
        card.add(progressBar);
        card.add(Box.createVerticalStrut(10));
        card.add(statusLabel);

        bg.add(card);
        setVisible(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == backBtn) {

            int state = this.getExtendedState();
            new MainGUI(state);

            this.dispose();
        }

        if(e.getSource() == browseBtn) {

            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                pathField.setText(chooser.getSelectedFile().getAbsolutePath());
        }

        if(e.getSource() == sortBtn) {

            File folder = new File(pathField.getText());

            SortType method = SortType.TYPE;

            if(dateRB.isSelected())
                method = SortType.DATE;
            else if(sizeRB.isSelected())
                method = SortType.SIZE;

            progressBar.setValue(0);
            statusLabel.setText("Sorting Started");

            boolean customTypeEnabled = false;
            String customFolderName = "";
            String[] selectedExtensions = null;

            boolean dateFilterEnabled = false;
            int lastValue = 0;
            String timeUnit = "Minutes";
            String filterMode = "";
            long specificDate = 0;

            String sizeMode = "";
            String thresholdCondition = "";
            long thresholdBytes = 0;
            long rangeFromBytes = 0;
            long rangeToBytes = 0;
            String thresholdFolderName = "";

            // TYPE POPUP
            if(method == SortType.TYPE) {

                JRadioButton autoRB = new JRadioButton("Automatic", true);
                JRadioButton customRB = new JRadioButton("Custom");

                ButtonGroup bg = new ButtonGroup();
                bg.add(autoRB);
                bg.add(customRB);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.add(autoRB);
                panel.add(customRB);

                int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Choose Type Sort Mode",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

                if(result != JOptionPane.OK_OPTION)
                    return;

                if(customRB.isSelected()) {

                    customTypeEnabled = true;

                    JTextField folderNameField = new JTextField();
                    JTextField extField = new JTextField();

                    JPanel customPanel = new JPanel();
                    customPanel.setLayout(new BoxLayout(customPanel, BoxLayout.Y_AXIS));
                    customPanel.add(new JLabel("Folder Name:"));
                    customPanel.add(folderNameField);
                    customPanel.add(new JLabel("Extensions (comma separated):"));
                    customPanel.add(extField);

                    int customResult = JOptionPane.showConfirmDialog(
                    this,
                    customPanel,
                    "Custom Type Sorting",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

                    if(customResult != JOptionPane.OK_OPTION)
                        return;

                    customFolderName = folderNameField.getText();
                    selectedExtensions = extField.getText().split(",");
                }
            }

            // ---------------- DATE ----------------

            if(method == SortType.DATE) {

                dateFilterEnabled = true;

                JRadioButton lastTimeRB = new JRadioButton("Last X Minutes / Hours / Days", true);
                JRadioButton afterDateRB = new JRadioButton("Modified After Date");
                JRadioButton beforeDateRB = new JRadioButton("Modified Before Date");
                JRadioButton onDateRB = new JRadioButton("Modified On Date");

                ButtonGroup bg = new ButtonGroup();
                bg.add(lastTimeRB);
                bg.add(afterDateRB);
                bg.add(beforeDateRB);
                bg.add(onDateRB);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                panel.add(lastTimeRB);
                panel.add(afterDateRB);
                panel.add(beforeDateRB);
                panel.add(onDateRB);

                int result = JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Choose Date Filter Type",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE   
                );

                if(result != JOptionPane.OK_OPTION)
                    return;

                try {

                    if(lastTimeRB.isSelected()) {

                        filterMode = "LAST_TIME";

                        JTextField valueField = new JTextField();

                        JRadioButton minRB = new JRadioButton("Minutes", true);
                        JRadioButton hrRB = new JRadioButton("Hours");
                        JRadioButton dayRB = new JRadioButton("Days");

                        ButtonGroup unitGroup = new ButtonGroup();
                        unitGroup.add(minRB);
                        unitGroup.add(hrRB);
                        unitGroup.add(dayRB);

                        JPanel timePanel = new JPanel();
                        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));

                        timePanel.add(new JLabel("Enter Time Value:"));
                        timePanel.add(valueField);
                        timePanel.add(minRB);
                        timePanel.add(hrRB);
                        timePanel.add(dayRB);

                        int timeResult = JOptionPane.showConfirmDialog(
                                this,
                                timePanel,
                                "Last X Time Filter",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE   // 🔥 NO ICON
                        );

                        if(timeResult != JOptionPane.OK_OPTION)
                            return;

                        lastValue = Integer.parseInt(valueField.getText());

                        if(minRB.isSelected())
                            timeUnit = "Minutes";
                        else if(hrRB.isSelected())
                            timeUnit = "Hours";
                        else
                            timeUnit = "Days";
                    }

                    else {

                        java.text.SimpleDateFormat sdf =
                                new java.text.SimpleDateFormat("d-M-yyyy");

                        specificDate = sdf.parse(
                                JOptionPane.showInputDialog(
                                        this,
                                        "Enter Date (d-M-yyyy):",
                                        "Date Input",
                                        JOptionPane.PLAIN_MESSAGE   // 🔥 NO ICON
                                )
                        ).getTime();

                        if(afterDateRB.isSelected())
                            filterMode = "AFTER_DATE";
                        else if(beforeDateRB.isSelected())
                            filterMode = "BEFORE_DATE";
                        else
                            filterMode = "ON_DATE";
                    }

                }
                catch(Exception ex) {
                    JOptionPane.showMessageDialog(this,"Invalid Input!");
                    return;
                }
            }

            // ---------- SIZE SORT ----------

            if(method == SortType.SIZE) {

                JRadioButton greaterRB = new JRadioButton("Greater Than", true);
                JRadioButton lessRB = new JRadioButton("Less Than");

                ButtonGroup bg = new ButtonGroup();
                bg.add(greaterRB);
                bg.add(lessRB);

                JPanel condPanel = new JPanel();
                condPanel.setLayout(new BoxLayout(condPanel, BoxLayout.Y_AXIS));
                condPanel.add(greaterRB);
                condPanel.add(lessRB);

                int condResult = JOptionPane.showConfirmDialog(
                        this,
                        condPanel,
                        "Choose Size Condition",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE   
                );

                if(condResult != JOptionPane.OK_OPTION)
                    return;

                JTextField sizeField = new JTextField();
                JTextField folderField = new JTextField();

                JRadioButton kbRB = new JRadioButton("KB", true);
                JRadioButton mbRB = new JRadioButton("MB");
                JRadioButton gbRB = new JRadioButton("GB");

                ButtonGroup unitGroup = new ButtonGroup();
                unitGroup.add(kbRB);
                unitGroup.add(mbRB);
                unitGroup.add(gbRB);

                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

                inputPanel.add(new JLabel("Enter Size Value:"));
                inputPanel.add(sizeField);
                inputPanel.add(new JLabel("Select Unit:"));
                inputPanel.add(kbRB);
                inputPanel.add(mbRB);
                inputPanel.add(gbRB);
                inputPanel.add(new JLabel("Folder Name:"));
                inputPanel.add(folderField);

                int inputResult = JOptionPane.showConfirmDialog(
                        this,
                        inputPanel,
                        "Size Filter",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE   
                );

                if(inputResult != JOptionPane.OK_OPTION)
                    return;

                long size = Long.parseLong(sizeField.getText());

                if(kbRB.isSelected())
                    thresholdBytes = size * 1024;
                else if(mbRB.isSelected())
                    thresholdBytes = size * 1024 * 1024;
                else
                    thresholdBytes = size * 1024 * 1024 * 1024;

                if(greaterRB.isSelected())
                    thresholdCondition = "GREATER";
                else
                    thresholdCondition = "LESS";

                thresholdFolderName = folderField.getText();
                sizeMode = "THRESHOLD";
            }

            SortWorker worker =
                    new SortWorker(folder, method,
                            progressBar,
                            statusLabel,
                            dateFilterEnabled,
                            lastValue,
                            timeUnit,
                            filterMode,
                            specificDate,
                            0,
                            0,
                            customTypeEnabled,
                            customFolderName,
                            selectedExtensions,
                            sizeMode,
                            thresholdCondition,
                            thresholdBytes,
                            rangeFromBytes,
                            rangeToBytes,
                            thresholdFolderName);

            worker.execute();
        }
    }

    public static void main(String[] args) {
        new FileSorterSwingGUI(JFrame.NORMAL);
    }
}