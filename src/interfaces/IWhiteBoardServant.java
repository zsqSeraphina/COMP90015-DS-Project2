package src.interfaces;

import src.gui.WhiteBoardFrame;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public interface IWhiteBoardServant extends Remote{
    WhiteBoardFrame initialWhiteBoard()throws RemoteException;
    ConcurrentHashMap<String, String> updateUser(String userName)throws RemoteException;
    ConcurrentHashMap<String, String> getUserList() throws RemoteException;
}
