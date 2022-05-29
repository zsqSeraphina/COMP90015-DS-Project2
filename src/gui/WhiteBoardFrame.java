package src.gui;

import src.utils.PaintOptionType;
import src.utils.Shape;
import src.utils.FileHandler;
import src.server.IWhiteBoardServant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class WhiteBoardFrame extends JFrame {

    private ConcurrentHashMap<String, String> userList;
    private ArrayList<String> messages;
    private final UserInfoPanel userPanel;
    private CanvasPanel canvasPanel;
    private final ChatPanel chatPanel;
    private JPanel emptyPanel;
    private ConcurrentHashMap<Point, Shape> shapes;
    private IWhiteBoardServant server;

    public WhiteBoardFrame(ConcurrentHashMap<Point, Shape> shapes, String username,
                           ConcurrentHashMap<String, String> userList,
                           ArrayList<String> messages, IWhiteBoardServant server) {
        canvasPanel = new CanvasPanel(shapes, server);
        this.setLayout(new GridBagLayout());
        this.setTitle(username + " White Board");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        this.userList = userList;
        this.messages = messages;
        this.shapes = shapes;
        this.server = server;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        int closingConfirm;
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
                                JOptionPane.showMessageDialog(null, e + ", please try again later",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                            System.exit(0);
                        }
                    }
                }
        );

        MenuBar menuBar = new MenuBar();
        Menu fileOption= new Menu("File");
        MenuItem newFile, open, save, saveAs, close;
        newFile = new MenuItem("New");
        open = new MenuItem("Open");
        save = new MenuItem("Save");
        saveAs = new MenuItem("Save as");
        close = new MenuItem("Close");

        save.addActionListener(e -> {
            if (this.canvasPanel != null) {
                try {
                    FileHandler.saveFile(server);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, e + ", please try again later",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please create a white board",
                        "No white board found", JOptionPane.ERROR_MESSAGE);
            }

        });

        saveAs.addActionListener(e -> {
            if (this.canvasPanel != null) {
                FileHandler.saveAsFile(server);
            } else {
                JOptionPane.showMessageDialog(null, "Please create a white board",
                        "No white board found", JOptionPane.ERROR_MESSAGE);
            }

        });

        newFile.addActionListener(e -> {
            try {
                int newFileConfirm = 1;
                if (this.canvasPanel != null) {
                    newFileConfirm = JOptionPane.showConfirmDialog(null,
                            "You will lose your current painting!",
                            "Confirm New File", JOptionPane.YES_NO_OPTION);
                    if (newFileConfirm == JOptionPane.YES_OPTION) {
                        server.setFileName(null);
                        server.renew();
                        this.repaint();
                    }
                }
                if (this.emptyPanel != null) {
                    try {
                        server.setCanvasClosed(false);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, e + ", please try again later",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, e + ", please try again later",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });

        open.addActionListener(e -> {
            try {
                server.setCanvasClosed(false);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, e + ", please try again later",
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            FileHandler.openFile(server);
        });

        close.addActionListener(e -> {
            int closeFileConfirm = JOptionPane.showConfirmDialog(null,
                    "You will lose your current painting!",
                    "Confirm New File", JOptionPane.YES_NO_OPTION);
            if (closeFileConfirm == JOptionPane.YES_OPTION) {
                try {
                    server.setCanvasClosed(true);
                    server.renew();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, e + ", please try again later",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        fileOption.add(newFile);
        fileOption.add(open);
        fileOption.add(save);
        fileOption.add(saveAs);
        fileOption.add(close);
        if (userList.get(username).equals("Manager")) {
            menuBar.add(fileOption);
        }

        Menu paintOption= new Menu("Shape");
        MenuItem line, rect, circle, triangle, text;
        line = new MenuItem("Line");
        rect = new MenuItem("Rectangle");
        circle = new MenuItem("Circle");
        triangle = new MenuItem("Triangle");
        text = new MenuItem("Text");

        line.addActionListener(e -> canvasPanel.setPaintOptionType(PaintOptionType.LINE));

        rect.addActionListener(e -> canvasPanel.setPaintOptionType(PaintOptionType.RECTANGLE));

        circle.addActionListener(e -> canvasPanel.setPaintOptionType(PaintOptionType.CIRCLE));

        triangle.addActionListener(e -> canvasPanel.setPaintOptionType(PaintOptionType.TRIANGLE));

        text.addActionListener(e -> canvasPanel.setPaintOptionType(PaintOptionType.TEXT));

        paintOption.add(line);
        paintOption.add(rect);
        paintOption.add(circle);
        paintOption.add(triangle);
        paintOption.add(text);
        menuBar.add(paintOption);

        MenuItem cp = new MenuItem("Colour");
        Menu colorOption= new Menu("Color Palate");
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

        JPanel container = new JPanel();
        container.setLayout(new GridLayout(2, 1));
        container.setPreferredSize(new Dimension(200,400));

        userPanel = new UserInfoPanel(username, userList, server);
        reloadList(userList);
        container.add(userPanel);
        chatPanel = new ChatPanel(username, messages, server);
        container.add(chatPanel);

        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        gbc.gridx = 1;
        gbc.gridy = 0;
        this.getContentPane().add(container, gbc);


        this.setResizable(false);
        this.pack();
        this.setVisible(true);

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
        if (canvasPanel != null) {
            canvasPanel.setShapes(shapes);
        }
    }

    public void reloadMessage(ArrayList<String> messages) {
        if (!messages.equals(this.messages)) {
            chatPanel.reloadMessage(messages);
            this.messages = messages;
            this.revalidate();
            this.repaint();
        }
    }

    public void closeCanvas () {
        remove(canvasPanel);
        canvasPanel = null;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(800, 400));
        emptyPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        this.getContentPane().add(new JPanel(), gbc);
        this.revalidate();
        this.repaint();
    }

    public void newCanvas () {
        remove(emptyPanel);
        emptyPanel = null;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        canvasPanel = new CanvasPanel(shapes, server);
        this.getContentPane().add(canvasPanel, gbc);
        this.revalidate();
        this.repaint();
    }


}
