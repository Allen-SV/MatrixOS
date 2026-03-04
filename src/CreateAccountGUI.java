import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CreateAccountGUI extends AppFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField passwordField;
    ModernButton createBtn;

    public CreateAccountGUI(int state) {

        setSize(500,400);
        setLocationRelativeTo(null);
        setExtendedState(state);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GradientPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        GlassPanel card = new GlassPanel();
        card.setPreferredSize(new Dimension(350,250));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        styleField(usernameField);
        styleField(passwordField);

        usernameField.setMaximumSize(new Dimension(250,40));
        passwordField.setMaximumSize(new Dimension(250,40));

        createBtn = new ModernButton("Create");
        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        createBtn.setMaximumSize(new Dimension(200,50));
        createBtn.addActionListener(this);

        card.add(Box.createVerticalGlue());
        card.add(title);
        card.add(Box.createVerticalStrut(20));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(15));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(25));
        card.add(createBtn);
        card.add(Box.createVerticalGlue());

        bg.add(card);

        setVisible(true);
    }

    private void styleField(JTextField field) {

        field.setOpaque(true);
        field.setBackground(new Color(245,245,245));
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180,180,180),1,true),
                BorderFactory.createEmptyBorder(6,10,6,10)
        ));
    }

    public void actionPerformed(ActionEvent e) {

        String user = usernameField.getText().trim();
        String pass = String.valueOf(passwordField.getPassword()).trim();

        if(user.isEmpty() || pass.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fields cannot be empty",
                    "Error",
                    JOptionPane.PLAIN_MESSAGE
            );
            return;
        }

        CSVLoginManager.createAccount(user, pass);
        CSVLoginManager.setStatus("login");

        int state = this.getExtendedState();

        new MainGUI(state).setVisible(true);
        this.dispose();
    }
}