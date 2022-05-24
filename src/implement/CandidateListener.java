package src.implement;

import src.constants.Shape;
import src.gui.WhiteBoardFrame;
import src.interfaces.IWhiteBoardServant;

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
            while (true) {
                if (server.getUserList().containsKey(username)) {
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
