import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginGUI extends AppFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField passwordField;
    ModernButton loginBtn;

    public LoginGUI(int state) {
        
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

        JLabel title = new JLabel("Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        styleField(usernameField);
        styleField(passwordField);

        usernameField.setMaximumSize(new Dimension(250,40));
        passwordField.setMaximumSize(new Dimension(250,40));

        loginBtn = new ModernButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(200,50));
        loginBtn.addActionListener(this);

        card.add(Box.createVerticalGlue());
        card.add(title);
        card.add(Box.createVerticalStrut(20));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(15));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(25));
        card.add(loginBtn);
        card.add(Box.createVerticalGlue());

        bg.add(card, new GridBagConstraints(
                0,0,1,1,1,1,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(0,0,0,0),
                0,0
        ));

        setVisible(true);
    }

    private void styleField(JTextField field) {

        field.setOpaque(true);
        field.setBackground(new Color(245,245,245));
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(180,180,180),1,true),
                BorderFactory.createEmptyBorder(6,10,6,10)
        ));
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == loginBtn) {

            String user = usernameField.getText().trim();
            String pass = String.valueOf(passwordField.getPassword()).trim();

            boolean success = CSVLoginManager.verifyLogin(user, pass);

            if(success) {

                CSVLoginManager.setStatus("login");

                int state = this.getExtendedState();   // ⭐ GET CURRENT WINDOW STATE
                new MainGUI(state);                    // ⭐ PASS IT

                this.dispose();
            }
            else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Username or Password",
                        "Login Failed",
                        JOptionPane.PLAIN_MESSAGE
                );
            }
        }
    }
}