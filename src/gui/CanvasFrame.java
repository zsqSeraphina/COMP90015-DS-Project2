package src.gui;

import javax.swing.*;

public class CanvasFrame extends JFrame {

    CanvasPanel canvasPanel;

    public CanvasFrame(String title) {
        canvasPanel = new CanvasPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(canvasPanel);
        this.pack();
        this.setVisible(true);

    }
}
