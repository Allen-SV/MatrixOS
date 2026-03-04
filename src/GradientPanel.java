import java.awt.*;
import javax.swing.*;

public class GradientPanel extends JPanel {

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();

        Color c1 = new Color(24,28,47);
        Color c2 = new Color(14,63,92);

        GradientPaint gp =
                new GradientPaint(0,0,c1,w,h,c2);

        g2d.setPaint(gp);
        g2d.fillRect(0,0,w,h);
    }
}