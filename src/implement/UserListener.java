package src.implement;

import src.constants.Shape;
import src.gui.WhiteBoardFrame;
import src.interfaces.IWhiteBoardServant;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public class UserListener implements Runnable {

    private ConcurrentHashMap<String, String> userList;
    private WhiteBoardFrame whiteBoardFrame;
    private String username;
    private IWhiteBoardServant server;
    UserListener(WhiteBoardFrame whiteBoardFrame,
                 String username, IWhiteBoardServant server) {
        this.whiteBoardFrame = whiteBoardFrame;
        this.username = username;
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.userList = server.getUserList();
                whiteBoardFrame.reloadList(server.getUserList());
                whiteBoardFrame.reloadShapes(server.getShapes());
                if (!userList.containsKey(username)) {
                    System.exit(0);
                }
                if (!userList.containsValue("Manager")) {
                    System.exit(0);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
