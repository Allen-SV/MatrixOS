import java.awt.*;
import javax.swing.*;

public class GlassPanel extends JPanel {

    public GlassPanel() {
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255,255,255,40));
        g2.fillRoundRect(0,0,getWidth(),getHeight(),
                40,40);

        g2.setColor(new Color(255,255,255,60));
        g2.drawRoundRect(0,0,getWidth()-1,
                getHeight()-1,40,40);

        g2.dispose();

        super.paintComponent(g);
    }
}