import javax.swing.*;

public class App {

    public static void main(String[] args) {

        if(!CSVLoginManager.isAccountCreated()) {

            new CreateAccountGUI(JFrame.NORMAL);

        } else {

            String status = CSVLoginManager.getStatus();

            if(status.equals("login")) {

                new MainGUI(JFrame.NORMAL).setVisible(true);

            } else {

                new LoginGUI(JFrame.NORMAL);
            }
        }
    }
}