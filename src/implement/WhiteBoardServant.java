package src.implement;

import src.interfaces.IWhiteBoardServant;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class WhiteBoardServant extends UnicastRemoteObject implements IWhiteBoardServant {

    private ConcurrentHashMap<String, String> userList;

    public WhiteBoardServant() throws RemoteException {
        super();

        userList = new ConcurrentHashMap<>();

    }


}
