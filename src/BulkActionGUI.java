import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class BulkActionGUI extends AppFrame implements ActionListener {

    JTextField pathField;
    ModernButton browseBtn, applyBtn, backBtn;
    JProgressBar progressBar;
    JLabel statusLabel;

    public BulkActionGUI(int state) {

        setSize(800,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(state);

        // GRADIENT BACKGROUND
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        // GLASS CARD
        GlassPanel card = new GlassPanel();
        card.setPreferredSize(new Dimension(600,350));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // TOP PANEL WITH BACK BUTTON
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,60));

        JPanel backWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        backWrapper.setOpaque(false);

        backBtn = new ModernButton("←");
        backBtn.setPreferredSize(new Dimension(60,40));
        backBtn.addActionListener(this);

        backWrapper.add(backBtn);
        topPanel.add(backWrapper, BorderLayout.WEST);

        // TITLE
        JLabel folderLabel = new JLabel("Select Folder for Bulk Action");
        folderLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        folderLabel.setForeground(Color.WHITE);
        folderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // PATH + BROWSE PANEL
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

        // APPLY BUTTON
        applyBtn = new ModernButton("Apply Action");
        applyBtn.setMaximumSize(new Dimension(200,50));
        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyBtn.addActionListener(this);

        // PROGRESS BAR
        progressBar = new JProgressBar();
        progressBar.setMaximumSize(new Dimension(500,20));

        // STATUS
        statusLabel = new JLabel("Status: Ready");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ADD TO CARD
        card.add(topPanel);

        card.add(Box.createVerticalStrut(-6));
        card.add(folderLabel);

        card.add(Box.createVerticalStrut(20));
        card.add(pathPanel);

        card.add(Box.createVerticalStrut(40));
        card.add(applyBtn);

        card.add(Box.createVerticalStrut(40));
        card.add(progressBar);

        card.add(Box.createVerticalStrut(20));
        card.add(statusLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(25, 0, 0, 0);

        bg.add(card, gbc);
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

            if(chooser.showOpenDialog(this)
               == JFileChooser.APPROVE_OPTION)

                pathField.setText(
                    chooser.getSelectedFile()
                    .getAbsolutePath());
        }

        if(e.getSource() == applyBtn) {

            File folder = new File(pathField.getText());

            // -------- RADIO BUTTON POPUP --------

            JRadioButton renameRB = new JRadioButton("Mass Rename");
            JRadioButton deleteRB = new JRadioButton("Mass Delete");

            renameRB.setSelected(true);

            ButtonGroup group = new ButtonGroup();
            group.add(renameRB);
            group.add(deleteRB);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(renameRB);
            panel.add(deleteRB);

            int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Choose Action",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if(result != JOptionPane.OK_OPTION)
                return;

            // ------------------ MASS RENAME ------------------

            if(renameRB.isSelected()) {

                JTextField baseNameField = new JTextField();

                JPanel basePanel = new JPanel();
                basePanel.setLayout(
                    new BoxLayout(basePanel, BoxLayout.Y_AXIS)
                );

                basePanel.add(new JLabel("Enter Base Name:"));
                basePanel.add(baseNameField);
                basePanel.add(Box.createVerticalStrut(10));
                basePanel.add(new JLabel("Example: img"));

                int baseResult = JOptionPane.showConfirmDialog(
                        this,
                        basePanel,
                        "Input",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if(baseResult != JOptionPane.OK_OPTION)
                    return;

                String baseName = baseNameField.getText();

                if(baseName == null || baseName.isEmpty()) {

                    JOptionPane.showMessageDialog(
                        this,"Invalid Name!");
                    return;
                }

                statusLabel.setText(
                    "Mass Rename Started");

                BulkActionWorker worker =
                    new BulkActionWorker(
                        folder,
                        progressBar,
                        statusLabel,
                        baseName
                    );

                worker.execute();
            }

            // ------------------ MASS DELETE ------------------

            else {

                JCheckBox nameCB = new JCheckBox("Name");
                JCheckBox typeCB = new JCheckBox("Type");
                JCheckBox sizeCB = new JCheckBox("Size");
                JCheckBox dateCB = new JCheckBox("Date");

                JPanel deletePanel = new JPanel();
                deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));

                deletePanel.add(new JLabel("Delete Based On:"));
                deletePanel.add(Box.createVerticalStrut(10));
                deletePanel.add(nameCB);
                deletePanel.add(typeCB);
                deletePanel.add(sizeCB);
                deletePanel.add(dateCB);

                int deleteResult = JOptionPane.showConfirmDialog(
                        this,
                        deletePanel,
                        "Select Filters",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if(deleteResult != JOptionPane.OK_OPTION)
                    return;

                if(!nameCB.isSelected() &&
                !typeCB.isSelected() &&
                !sizeCB.isSelected() &&
                !dateCB.isSelected()) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Please select at least one filter!",
                        "Warning",
                        JOptionPane.PLAIN_MESSAGE
                    );
                    return;
                }

                // -------- NOW CREATE COMBINED PANEL --------

                JPanel combinedPanel = new JPanel();
                combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));

                JTextField nameField = new JTextField();
                JTextField typeField = new JTextField();
                JTextField sizeField = new JTextField();
                JTextField dateField = new JTextField();

                JRadioButton aboveRB = new JRadioButton("Above", true);
                JRadioButton belowRB = new JRadioButton("Below");
                ButtonGroup sizeGroup = new ButtonGroup();
                sizeGroup.add(aboveRB);
                sizeGroup.add(belowRB);

                JRadioButton kbRB = new JRadioButton("KB", true);
                JRadioButton mbRB = new JRadioButton("MB");
                JRadioButton gbRB = new JRadioButton("GB");
                ButtonGroup unitGroup = new ButtonGroup();
                unitGroup.add(kbRB);
                unitGroup.add(mbRB);
                unitGroup.add(gbRB);

                JRadioButton beforeRB = new JRadioButton("Before", true);
                JRadioButton afterRB = new JRadioButton("After");
                ButtonGroup dateGroup = new ButtonGroup();
                dateGroup.add(beforeRB);
                dateGroup.add(afterRB);

                if(nameCB.isSelected()) {
                    combinedPanel.add(new JLabel("Name Contains:"));
                    combinedPanel.add(nameField);
                    combinedPanel.add(Box.createVerticalStrut(10));
                }

                if(typeCB.isSelected()) {
                    combinedPanel.add(new JLabel("Extension:"));
                    combinedPanel.add(typeField);
                    combinedPanel.add(Box.createVerticalStrut(10));
                }

                if(sizeCB.isSelected()) {
                    combinedPanel.add(new JLabel("Size:"));
                    combinedPanel.add(aboveRB);
                    combinedPanel.add(belowRB);
                    combinedPanel.add(sizeField);
                    combinedPanel.add(kbRB);
                    combinedPanel.add(mbRB);
                    combinedPanel.add(gbRB);
                    combinedPanel.add(Box.createVerticalStrut(10));
                }

                if(dateCB.isSelected()) {
                    combinedPanel.add(new JLabel("Date (d-M-yyyy):"));
                    combinedPanel.add(beforeRB);
                    combinedPanel.add(afterRB);
                    combinedPanel.add(dateField);
                }

                int finalResult = JOptionPane.showConfirmDialog(
                        this,
                        combinedPanel,
                        "Delete Filters",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if(finalResult != JOptionPane.OK_OPTION)
                    return;

                String nameContains = "";
                String fileType = "";
                String sizeCondition = "";
                long sizeBytes = 0;
                String dateCondition = "";
                long dateValue = 0;

                try {

                    if(nameCB.isSelected())
                        nameContains = nameField.getText();

                    if(typeCB.isSelected())
                        fileType = typeField.getText();

                    if(sizeCB.isSelected()) {

                        long val = Long.parseLong(sizeField.getText());

                        if(aboveRB.isSelected())
                            sizeCondition = "GT";
                        else
                            sizeCondition = "LT";

                        if(kbRB.isSelected())
                            sizeBytes = val * 1024;
                        else if(mbRB.isSelected())
                            sizeBytes = val * 1024 * 1024;
                        else
                            sizeBytes = val * 1024 * 1024 * 1024;
                    }

                    if(dateCB.isSelected()) {

                        if(beforeRB.isSelected())
                            dateCondition = "BEFORE";
                        else
                            dateCondition = "AFTER";

                        java.text.SimpleDateFormat sdf =
                            new java.text.SimpleDateFormat("d-M-yyyy");

                        dateValue =
                            sdf.parse(dateField.getText())
                            .getTime();
                    }

                }
                catch(Exception ex) {
                    JOptionPane.showMessageDialog(this,"Invalid Input!");
                    return;
                }

                statusLabel.setText("Mass Delete Started");

                MassDeleteWorker worker =
                    new MassDeleteWorker(
                        folder,
                        progressBar,
                        statusLabel,
                        nameContains,
                        fileType,
                        sizeCondition,
                        sizeBytes,
                        dateCondition,
                        dateValue
                    );

                worker.execute();
            }
        }
    }

    public static void main(String[] args) {
        new BulkActionGUI(JFrame.NORMAL);
    }
}