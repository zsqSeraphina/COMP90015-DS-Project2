package src.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IWhiteBoardClient extends Remote {
    public void updateUser() throws RemoteException;
}
