package src.gui;

import src.constants.PaintOptionType;
import src.constants.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class CanvasPanel extends JPanel {

    private Point start;
    private Point end;
    private PaintOptionType type = PaintOptionType.LINE;
    private final ConcurrentHashMap<Point, Shape> shapes;
    private Color paintColor = Color.black;


    CanvasPanel(ConcurrentHashMap<Point, Shape> shapes) {
        this.setPreferredSize(new Dimension(800, 400));
        this.shapes = shapes;
        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBackground(Color.white);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                start = e.getPoint();
            }
            public void mouseReleased(MouseEvent e) {
                if (type == PaintOptionType.TEXT) {
                    drawText(start, end);
                }
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
            public void mouseMoved(MouseEvent e) {
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
            Color color = shape.getColor();
            if (start != null && end != null && type != PaintOptionType.TEXT) {
                drawChosenShape(g, start, end, type, color);
            } else if (start != null && end != null && shape.getText() != null) {
                g.setColor(shape.getColor());
                g.drawString(shape.getText(), start.x + 6, start.y + 18);
                repaint();
            }
        }

        if (start != null && end != null) {
            Shape newShape = new Shape();
            newShape.setStart(this.start);
            newShape.setEnd(this.end);
            newShape.setType(this.type);
            newShape.setColor(this.paintColor);
            shapes.put(this.start, newShape);
            drawChosenShape(g, this.start, this.end, this.type, this.paintColor);
        }
    }


    public void drawChosenShape(Graphics g, Point start, Point end, PaintOptionType type, Color color)  {
        g.setColor(color);
        switch (type){
            case LINE -> {
                g.drawLine(start.x, start.y, end.x, end.y);
                repaint();
            }
            case RECTANGLE, TEXT ->  {
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
        }

    }
    public void drawText(Point start, Point end)  {
        if (start != null && end != null) {
            JPanel panel = new JPanel();
            JTextArea textArea = new JTextArea();
            textArea.setPreferredSize(new Dimension(end.x - start.x, end.y - start.y));
            textArea.setBackground(Color.lightGray);
            JButton confirm = new JButton("confirm");
            confirm.setPreferredSize(new Dimension(100, 40));
            panel.add(textArea);
            panel.add(confirm);
            panel.setBounds(start.x, start.y, end.x - start.x  + 120, end.y - start.y + 60);
            panel.setBackground(Color.white);
            this.add(panel);
            this.validate();
            this.repaint();
            confirm.addActionListener(e -> {
                Shape newShape = new Shape();
                newShape.setStart(start);
                newShape.setEnd(end);
                newShape.setType(this.type);
                newShape.setText(textArea.getText());
                newShape.setColor(this.paintColor);
                shapes.put(start, newShape);
                this.remove(panel);
                this.validate();
                this.repaint();
            });
        }

    }


    public void setPaintOptionType(PaintOptionType type)  {
        this.type = type;
    }



    public void setPaintColor(Color color)  {
        this.paintColor = color;
    }

}
