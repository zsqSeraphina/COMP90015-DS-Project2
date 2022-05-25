package src.gui;

import src.constants.PaintOptionType;
import src.constants.Shape;
import src.interfaces.IWhiteBoardServant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WhiteBoardFrame extends JFrame {

    private ConcurrentHashMap<String, String> userList;
    private UserInfoPanel userPanel;
    private CanvasPanel canvasPanel;

    public WhiteBoardFrame(ConcurrentHashMap<Point, Shape> shapes, String username,
                           ConcurrentHashMap<String, String> userList, IWhiteBoardServant server) {
        canvasPanel = new CanvasPanel(shapes, server);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        this.userList = userList;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        int closingConfirm = JOptionPane.NO_OPTION;
                        if (!userList.get(username).equals("Manager")) {
                            closingConfirm = JOptionPane.showConfirmDialog(null,
                                    "You are closing the white board",
                                    "Confirm Closing", JOptionPane.YES_NO_OPTION);
                        } else {
                            closingConfirm = JOptionPane.showConfirmDialog(null,
                                    "You are ending the white board for all users",
                                    "Confirm Closing", JOptionPane.YES_NO_OPTION);
                        }

                        if (closingConfirm == JOptionPane.YES_OPTION) {
                            try {
                                if (userList.get(username).equals("Manager")) {
                                    server.resetAll();
                                }
                                userList.remove(username);
                                server.setUserList(userList);
                            } catch (RemoteException error) {
                                error.printStackTrace();
                            }
                            System.exit(0);
                        }
                    }
                }
        );

        if (userList.get(username).equals("Manager")) {
            this.setTitle("Manager White Board");
        } else {
            this.setTitle("User White Board");
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

        MenuItem cp = new MenuItem("colour Palate");
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
        userPanel = new UserInfoPanel(username, userList, server);
        reloadList(userList);
        this.getContentPane().add(userPanel, gbc);

        this.setResizable(false);
        this.pack();
        this.setVisible(true);

    }

    public ConcurrentHashMap<String, String> getUserList() {
        return userList;
    }
    public void setUserList(ConcurrentHashMap<String, String> userList) {
        this.userList = userList;
    }

    public void reloadList(ConcurrentHashMap<String, String> userList) {
        if (!userList.equals(this.userList)) {
            userPanel.reloadList(userList);
            this.userList = userList;
            this.revalidate();
            this.repaint();
        }
    }

    public void reloadShapes(ConcurrentHashMap<Point, Shape> shapes) {
        canvasPanel.setShapes(shapes);
    }

}
