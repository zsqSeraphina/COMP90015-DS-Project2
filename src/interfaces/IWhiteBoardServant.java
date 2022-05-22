package src.interfaces;

import src.gui.WhiteBoardFrame;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public interface IWhiteBoardServant extends Remote{
    WhiteBoardFrame initialWhiteBoard()throws RemoteException;
    void updateUser(String userName)throws RemoteException;
}
