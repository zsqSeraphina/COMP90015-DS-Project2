package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class CanvasPanel extends JPanel {

    private Point start;
    private Point end;

    private final Map<Point, Point> shapes = new HashMap<>();

    CanvasPanel() {
        this.setPreferredSize(new Dimension(800, 400));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                start = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                repaint();
                start = null;
                end = null;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                end = e.getPoint();
            }

            public void mouseDragged(MouseEvent e) {
                end = e.getPoint();
                repaint();
            }
        });
    }
    public void paint(Graphics g) {
        super.paint(g);

        for (Point start : shapes.keySet())  {
            Point end = shapes.get(start);

            if (start != null) {
                g.setColor(Color.black);
                g.drawRect(start.x, start.y, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
            }
        }

        if (start != null) {
            g.setColor(Color.black);
            g.drawRect(start.x, start.y, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
            shapes.put(start, end);
        }
    }
}
