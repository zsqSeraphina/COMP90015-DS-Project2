package src.gui;

import src.constants.PaintOptionType;
import src.constants.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class CanvasFrame extends JFrame {

    private final CanvasPanel canvasPanel;

    public CanvasFrame(String title, Map<Point, Shape> shapes, boolean isManager) {
        canvasPanel = new CanvasPanel(shapes);

        if (isManager) {
            this.setTitle("Manager " + title);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            this.setTitle("User " + title);
        }

        JMenuBar menuBar = new JMenuBar();
        JMenu paintOption;
        JMenuItem line, rect, circle, triangle, text;
        paintOption = new JMenu("Paint Option");
        line = new JMenuItem("Line");
        rect = new JMenuItem("Rectangle");
        circle = new JMenuItem("Circle");
        triangle = new JMenuItem("Triangle");
        text = new JMenuItem("Text");

        line.addActionListener(e -> {
                canvasPanel.setPaintOptionType(PaintOptionType.LINE);
        });

        rect.addActionListener(e -> {
            canvasPanel.setPaintOptionType(PaintOptionType.RECTANGLE);
        });

        circle.addActionListener(e -> {
            canvasPanel.setPaintOptionType(PaintOptionType.CIRCLE);
        });

        triangle.addActionListener(e -> {
            canvasPanel.setPaintOptionType(PaintOptionType.TRIANGLE);
        });

        text.addActionListener(e -> {
            canvasPanel.setPaintOptionType(PaintOptionType.TEXT);
        });

        paintOption.add(line);
        paintOption.add(rect);
        paintOption.add(circle);
        paintOption.add(triangle);
        paintOption.add(text);
        menuBar.add(paintOption);
        this.setJMenuBar(menuBar);

        this.add(canvasPanel);

        this.pack();
        this.setVisible(true);
    }
}
