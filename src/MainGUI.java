import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainGUI extends AppFrame implements ActionListener{

    ModernButton sortBtn;
    ModernButton bulkBtn;
    ModernButton duplicateBtn;
    ModernButton logoutBtn;

    public MainGUI(int state) {

        setSize(800,500);
        setLocationRelativeTo(null);
        setExtendedState(state);   // ⭐ THIS FIXES MAXIMIZE
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GradientPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        GlassPanel card = new GlassPanel();
        card.setPreferredSize(new Dimension(500,350));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // ----------- TOP BAR (LOGOUT) -----------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,60));

        JPanel logoutWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        logoutWrapper.setOpaque(false);

        logoutBtn = new ModernButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(120,40));
        logoutBtn.addActionListener(this);

        logoutWrapper.add(logoutBtn);
        topPanel.add(logoutWrapper, BorderLayout.EAST);

        // ----------- TITLE -----------
        JLabel title = new JLabel("Select Action");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ----------- BUTTONS -----------
        sortBtn = new ModernButton("File Sorter");
        bulkBtn = new ModernButton("Bulk Actions");
        duplicateBtn = new ModernButton("Duplicate Detection");

        sortBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bulkBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        duplicateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        sortBtn.setMaximumSize(new Dimension(300,60));
        bulkBtn.setMaximumSize(new Dimension(300,60));
        duplicateBtn.setMaximumSize(new Dimension(300,60));

        sortBtn.addActionListener(this);
        bulkBtn.addActionListener(this);
        duplicateBtn.addActionListener(this);

        // ----------- ADD TO CARD -----------
        card.add(topPanel);
        card.add(Box.createVerticalStrut(5));
        card.add(title);
        card.add(Box.createVerticalStrut(40));
        card.add(sortBtn);
        card.add(Box.createVerticalStrut(20));
        card.add(bulkBtn);
        card.add(Box.createVerticalStrut(20));
        card.add(duplicateBtn);
        card.add(Box.createVerticalGlue());

        // ----------- ADD CARD TO BG -----------
        bg.add(card, new GridBagConstraints(
                0,0,1,1,1,1,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(0,0,0,0),
                0,0
        ));

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        int state = this.getExtendedState();

        // ----------- LOGOUT CLICK -----------
        if(e.getSource() == logoutBtn) {

            JPanel panel = new JPanel(new BorderLayout(10,10));

            JLabel label = new JLabel("Do you really want to logout?");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            panel.add(label, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if(result == JOptionPane.YES_OPTION) {

                CSVLoginManager.setStatus("logout");
                new LoginGUI(state);
                this.dispose();
            }
        }

        // ----------- NAVIGATION -----------
        if(e.getSource() == sortBtn) {
            new FileSorterSwingGUI(state);
            this.dispose();
        }

        if(e.getSource() == bulkBtn) {
            new BulkActionGUI(state);
            this.dispose();
        }

        if(e.getSource() == duplicateBtn) {
            new DuplicateGUI(state);
            this.dispose();
        }
    }


    public static void main(String[] args) {
        new MainGUI(JFrame.NORMAL).setVisible(true);
    }
}