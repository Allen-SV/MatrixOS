import javax.swing.*;

public class AppFrame extends JFrame {

    public AppFrame() {

        setTitle("MatrixOS");

        ImageIcon icon = new ImageIcon(getClass().getResource("icon.png"));
        setIconImage(icon.getImage());
    }
}
