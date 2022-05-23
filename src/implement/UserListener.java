package src.implement;

import src.gui.WhiteBoardFrame;
import src.interfaces.IWhiteBoardServant;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public record UserListener(WhiteBoardFrame whiteBoardFrame,
                           ConcurrentHashMap<String, String> userList, IWhiteBoardServant server) implements Runnable {
    @Override
    public void run() {
        while (true) {
//            whiteBoardFrame.getUserInfoPanel().print();
//            whiteBoardFrame.setUserList(server.getUserList());
            try {
                whiteBoardFrame.reloadList(server.getUserList());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
