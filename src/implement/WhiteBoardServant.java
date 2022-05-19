package src.implement;

import src.gui.CanvasFrame;
import src.gui.WhiteBoardGUI;
import src.interfaces.IWhiteBoardServant;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class WhiteBoardServant extends UnicastRemoteObject implements IWhiteBoardServant {

    private ConcurrentHashMap<String, String> userList;

    public WhiteBoardServant() throws RemoteException {
        super();

        userList = new ConcurrentHashMap<>();

    }


    @Override
    public void initialWhiteBoard() throws RemoteException {
        WhiteBoardGUI.initialise();
    }
}
