package src.implement;

import src.constants.Shape;
import src.gui.WhiteBoardFrame;
import src.interfaces.IWhiteBoardServant;

import java.awt.*;
import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class WhiteBoardServant extends UnicastRemoteObject implements IWhiteBoardServant {

    @Serial
    private static final long serialVersionUID = -7231033280190281294L;
    private final ConcurrentHashMap<String, String> userList;
    private final ConcurrentHashMap<Point, Shape> shapes;
    private boolean isManager;

    public WhiteBoardServant() throws RemoteException {
        super();
        this.userList = new ConcurrentHashMap<>();
        this.shapes = new ConcurrentHashMap<>();
        shapes.put(new Point(), new Shape());
    }

    @Override
    public synchronized WhiteBoardFrame initialWhiteBoard() throws RemoteException {
        synchronized (this) {
            return new WhiteBoardFrame("White Board", shapes, isManager, userList);
        }
    }

    @Override
    public synchronized void updateUser(String userName)throws RemoteException {
        if (userList.isEmpty()) {
            this.userList.put(userName, "Manager");
            this.isManager = true;
        } else {
            this.userList.put(userName, "User");
            this.isManager = false;
            System.out.println(this.userList);
        }
    }
}
