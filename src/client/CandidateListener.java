package src.client;

import src.gui.WhiteBoardFrame;
import src.server.IWhiteBoardServant;
import src.utils.Shape;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class CandidateListener implements Runnable {
    private final IWhiteBoardServant server;
    private final String username;
    public CandidateListener(String username, IWhiteBoardServant server) {
        this.server = server;
        this.username = username;
    }
    @Override
    public void run() {
        try {
            JFrame waiting = new JFrame("Waiting");
            JTextField waitingText = new JTextField("You are waiting for the permission from the manager");
            waitingText.setFont(new Font("Times New Roman", Font.BOLD, 20));
            waiting.add(waitingText);
            waiting.setSize(new Dimension(200, 500));
            waiting.setFocusableWindowState(true);
            waiting.setLocationRelativeTo(null);
            waiting.setResizable(false);
            waiting.pack();
            waiting.setVisible(true);
            while (true) {
                if (server.getUserList().containsKey(username)) {
                    waiting.dispose();
                    waiting.setVisible(false);
                    ConcurrentHashMap<Point, Shape> shapes = new ConcurrentHashMap<>();
                    WhiteBoardFrame board = new WhiteBoardFrame(shapes, username,
                            server.getUserList(), server.getMessageList(), server);
                    new Thread(new UserListener(board, username, server), "UserListener.java").start();
                    break;
                } else if (server.getRejectList().contains(username)) {
                    waiting.dispose();
                    waiting.setVisible(false);
                    JOptionPane.showMessageDialog(null,
                            "The manager rejected your request!",
                            "Rejected", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                    break;
                }

            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "The manager rejected your request!",
                    "Rejected", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}
