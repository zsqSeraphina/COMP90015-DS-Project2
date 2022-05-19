package src.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public interface IWhiteBoardServant extends Remote{
    public void initialWhiteBoard()throws RemoteException;
}
