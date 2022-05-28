package src.gui;

import src.utils.PaintOptionType;
import src.utils.Shape;
import src.server.IWhiteBoardServant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.rmi.RemoteException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class CanvasPanel extends JPanel {

    private Point start;
    private Point end;
    private PaintOptionType type = PaintOptionType.LINE;
    private ConcurrentHashMap<Point, Shape> shapes;
    private Color paintColor = Color.black;
    private final IWhiteBoardServant server;


    CanvasPanel(ConcurrentHashMap<Point, Shape> shapes, IWhiteBoardServant server) {
        this.setPreferredSize(new Dimension(800, 400));
        this.shapes = shapes;
        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBackground(Color.white);
        this.server  = server;

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

        // paint all the shapes currently in the server shape list
        for (Shape shape : shapes.values())  {
            Point start = shape.getStart();
            Point end = shape.getEnd();
            PaintOptionType type = shape.getType();
            Color color = shape.getColor();
            if (start != null && end != null && type != PaintOptionType.TEXT) {
                drawChosenShape(g, start, end, type, color);
            } else if (start != null && end != null && shape.getText() != null) {
                // draw the text with new lines
                Graphics2D g2d = (Graphics2D) g;
                g.setColor(shape.getColor());
                AttributedString attributedText = new AttributedString(shape.getText());
                AttributedCharacterIterator paragraph = attributedText.getIterator();
                FontRenderContext frc = g2d.getFontRenderContext();
                LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
                int paragraphStart = paragraph.getBeginIndex();
                int paragraphEnd = paragraph.getEndIndex();
                float breakWidth = (float)(end.x - start.x);
                float drawPosY = start.y + 6;
                lineMeasurer.setPosition(paragraphStart);
                while (lineMeasurer.getPosition() < paragraphEnd) {

                    TextLayout layout = lineMeasurer.nextLayout(breakWidth);
                    float drawPosX = layout.isLeftToRight()
                            ? start.x + 6 : breakWidth - layout.getAdvance();
                    drawPosY += layout.getAscent();
                    layout.draw(g2d, drawPosX, drawPosY);
                    drawPosY += layout.getDescent() + layout.getLeading();
                }
                repaint();
            }
        }

        // paint the current drawing shape
        if (start != null && end != null) {
            Shape newShape = new Shape();
            newShape.setStart(this.start);
            newShape.setEnd(this.end);
            newShape.setType(this.type);
            newShape.setColor(this.paintColor);
            shapes.put(this.start, newShape);
            drawChosenShape(g, this.start, this.end, this.type, this.paintColor);
            try {
                server.updateShapes(this.start, newShape);
            } catch (RemoteException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e + ", please try again later",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
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
                int x = Math.min(start.x, end.x);
                int y = Math.min(start.y, end.y);
                g.drawRect(x, y, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
                repaint();
            }
            case CIRCLE  ->  {
                int x = Math.min(start.x, end.x);
                int y = Math.min(start.y, end.y);
                g.drawOval(x, y, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
                repaint();
            }
            case TRIANGLE  ->  {
                if (start.x < end.x && start.y > end.y) {
                    g.drawLine(start.x, start.y, end.x, end.y);
                    g.drawLine(start.x, start.y, (end.x - 180), end.y);
                    g.drawLine(end.x, end.y, (end.x - 180), end.y);
                } else if (start.x > end.x && start.y < end.y) {
                    g.drawLine(start.x, start.y, end.x, end.y);
                    g.drawLine(start.x, start.y, (end.x + 180), end.y);
                    g.drawLine(end.x, end.y, (end.x + 180), end.y);
                } else {
                    int midX = Math.abs(start.x - end.x)/2 + start.x;
                    int midY = Math.abs(start.y - end.y)/2 + end.y;
                    int[] xLine = {start.x, end.x, midX};
                    int[] yLine = {start.y, end.y, midY};
                    g.drawPolygon(xLine, yLine, 3);
                }
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
            textArea.setBounds(0, 0, end.x - start.x, end.y - start.y);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
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
                if (!textArea.getText().isBlank()) {
                    Shape newShape = new Shape();
                    newShape.setStart(start);
                    newShape.setEnd(end);
                    newShape.setType(this.type);
                    newShape.setText(textArea.getText());
                    newShape.setColor(this.paintColor);
                    shapes.put(start, newShape);
                    try {
                        server.updateShapes(start, newShape);
                    } catch (RemoteException error) {
                        error.printStackTrace();
                        JOptionPane.showMessageDialog(null, e + ", please try again later",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                }
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

    public void setShapes (ConcurrentHashMap<Point, Shape> shapes) {
        this.shapes = shapes;
        repaint();
    }
}
