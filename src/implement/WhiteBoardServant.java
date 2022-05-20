package src.implement;

import src.constants.Shape;
import src.gui.CanvasFrame;
import src.interfaces.IWhiteBoardServant;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class WhiteBoardServant extends UnicastRemoteObject implements IWhiteBoardServant {

    private final ConcurrentHashMap<String, String> userList;
    private final Map<Point, Shape> shapes;
    private boolean isManager;

    public WhiteBoardServant() throws RemoteException {
        super();
        this.userList = new ConcurrentHashMap<>();
        this.shapes = new HashMap<>(1);
        shapes.put(new Point(), new Shape());
    }

    @Override
    public synchronized void initialWhiteBoard() throws RemoteException {
        synchronized (this) {
            new CanvasFrame("White Board", shapes, isManager);
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
        }
        System.out.println(userList);
    }
}
