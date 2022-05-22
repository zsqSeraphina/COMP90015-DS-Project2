package src.gui;

import src.constants.PaintOptionType;
import src.constants.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

public class WhiteBoardFrame extends JFrame {

    public WhiteBoardFrame(String title, ConcurrentHashMap<Point, Shape> shapes, boolean isManager,
                           ConcurrentHashMap<String, String> userList) {
        CanvasPanel canvasPanel = new CanvasPanel(shapes);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;


        if (isManager) {
            this.setTitle("Manager " + title);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            this.setTitle("User " + title);
        }

        MenuBar menuBar = new MenuBar();
        Menu paintOption= new Menu("Paint Option");
        MenuItem line, rect, circle, triangle, text;
        line = new MenuItem("Line");
        rect = new MenuItem("Rectangle");
        circle = new MenuItem("Circle");
        triangle = new MenuItem("Triangle");
        text = new MenuItem("Text");

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

        MenuItem cp = new MenuItem("CP");
        Menu colorOption= new Menu("Color Option");
        cp.addActionListener(e -> {
            Color color = JColorChooser.showDialog(null, "Choose color", Color.BLACK);
            if (color != null) {
                canvasPanel.setPaintColor(color);
            }
        });
        colorOption.add(cp);
        menuBar.add(colorOption);

        this.setMenuBar(menuBar);

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.getContentPane().add(canvasPanel, gbc);

        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        gbc.gridx = 1;
        gbc.gridy = 0;
        UserInfoPanel userPanel = new UserInfoPanel(isManager, userList);
        this.getContentPane().add(userPanel, gbc);

        this.pack();
        this.setVisible(true);
    }
}
