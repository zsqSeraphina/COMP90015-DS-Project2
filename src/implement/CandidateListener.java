package src.implement;

import src.constants.Shape;
import src.gui.WhiteBoardFrame;
import src.interfaces.IWhiteBoardServant;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateListener implements Runnable {
    private IWhiteBoardServant server;
    private String username;
    private ConcurrentHashMap<String, String> userList;
    CandidateListener(String username, IWhiteBoardServant server, ConcurrentHashMap<String, String> userList) {
        this.server = server;
        this.username = username;
        this.userList = userList;
    }
    @Override
    public void run() {
        try {
            JFrame waiting = new JFrame("Waiting");
            JTextField waitingText = new JTextField("You are waiting for the permission from the manager");
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
                    WhiteBoardFrame board = new WhiteBoardFrame(shapes, username, server.getUserList(), server);
                    new Thread(new UserListener(board, username, server), "UserListener.java").start();
                    break;
                }

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
