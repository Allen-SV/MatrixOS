import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class DuplicateGUI extends AppFrame implements ActionListener {

    JTextField pathField;
    ModernButton browseBtn, detectBtn, backBtn;
    JProgressBar progressBar;
    JLabel statusLabel;

    public DuplicateGUI(int state) {

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
        JLabel folderLabel = new JLabel("Select Folder for Duplicate Detection");
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

        // DETECT BUTTON
        detectBtn = new ModernButton("Find Duplicates");
        detectBtn.setMaximumSize(new Dimension(200,50));
        detectBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        detectBtn.addActionListener(this);

        // PROGRESS BAR
        progressBar = new JProgressBar();
        progressBar.setMaximumSize(new Dimension(500,20));
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);

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
        card.add(detectBtn);

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
                    == JFileChooser.APPROVE_OPTION) {

                pathField.setText(
                        chooser.getSelectedFile()
                                .getAbsolutePath());
            }
        }

        if(e.getSource() == detectBtn) {

            String path = pathField.getText();

            if(path.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please select a folder!",
                    "Warning",
                    JOptionPane.PLAIN_MESSAGE
                );
                return;
            }

            File folder = new File(path);

            statusLabel.setText(
                "Duplicate Detection Started");

            DuplicateWorker worker =
                new DuplicateWorker(
                    folder,
                    progressBar,
                    statusLabel
                );

            worker.execute();
        }
    }

    public static void main(String[] args) {
        new DuplicateGUI(JFrame.NORMAL);
    }
}