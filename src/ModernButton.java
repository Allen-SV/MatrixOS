import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ModernButton extends JButton {

    public ModernButton(String text) {

        setText(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);

        setFont(new Font("Segoe UI",Font.BOLD,16));

        addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(0,200,255));
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                setBackground(new Color(255,255,255,30));
                repaint();
            }
        });

        setBackground(new Color(255,255,255,30));
    }

    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0,0,getWidth(),getHeight(),
                30,30);

        super.paintComponent(g);
    }
}
