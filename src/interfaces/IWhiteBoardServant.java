package src.interfaces;

import src.gui.CanvasFrame;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public interface IWhiteBoardServant extends Remote{
    void initialWhiteBoard()throws RemoteException;
    void updateUser(String userName)throws RemoteException;
}
