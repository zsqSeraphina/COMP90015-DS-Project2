package src.gui;

import src.constants.PaintOptionType;
import src.constants.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class CanvasPanel extends JPanel {

    private Point start;
    private Point end;
    private PaintOptionType type = PaintOptionType.LINE;
    private Map<Point, Shape> shapes;

    CanvasPanel(Map<Point, Shape> shapes) {
        this.setPreferredSize(new Dimension(800, 400));
        this.shapes = shapes;

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
            public void mouseDragged(MouseEvent e) {
                end = e.getPoint();
                repaint();
            }
        });
    }
    public void paint(Graphics g) {
        super.paint(g);

        for (Shape shape : shapes.values())  {
            Point start = shape.getStart();
            Point end = shape.getEnd();
            PaintOptionType type = shape.getType();
            if (start != null && end != null) {
                g.setColor(Color.black);
                drawChosenShape(g, start, end, type);
            }
        }

        if (start != null && end != null) {
            g.setColor(Color.black);
            drawChosenShape(g, this.start, this.end, this.type);
            Shape newShape = new Shape();
            newShape.setStart(this.start);
            newShape.setEnd(this.end);
            newShape.setType(this.type);
            shapes.put(this.start, newShape);
        }
    }

    public void setPaintOptionType(PaintOptionType type)  {
        this.type = type;
    }


    public void drawChosenShape(Graphics g, Point start, Point end, PaintOptionType type)  {
        switch (type){
            case LINE -> {
                g.drawLine(start.x, start.y, end.x, end.y);
                repaint();
            }
            case RECTANGLE  ->  {
                g.drawRect(start.x, start.y, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
                repaint();
            }
            case CIRCLE  ->  {
                g.drawOval(start.x, start.y, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
                repaint();
            }
            case TRIANGLE  ->  {
                g.drawLine(start.x, start.y, end.x, end.y);
                g.drawLine(start.x, start.y, (end.x - start.x), end.y);
                g.drawLine(end.x, end.y, (end.x - start.x), end.y);
                repaint();
            }
            case TEXT  ->  {
                JTextArea textArea = new JTextArea();
                textArea.setLocation(start);
                JButton confirm = new JButton("confirm");
                confirm.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        g.drawString(textArea.getText(), start.x, start.y);
                        repaint();
                    }
                });
                this.add(textArea);
                this.add(confirm);
            }
        }

    }
}
