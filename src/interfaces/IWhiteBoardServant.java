package src.interfaces;

import src.constants.Shape;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public interface IWhiteBoardServant extends Remote{
    void addUser(String userName)throws RemoteException;
    ConcurrentHashMap<String, String> getUserList() throws RemoteException;
    ConcurrentHashMap<Point, Shape> getShapes() throws RemoteException;
    void updateShapes(Point point, Shape shape) throws RemoteException;
    void setShapes(ConcurrentHashMap<Point, Shape> shape) throws RemoteException;
    void setUserList(ConcurrentHashMap<String, String> userList)throws RemoteException;
    ArrayList<String> getCandidateList() throws RemoteException;
    void setCandidateList(ArrayList<String> candidateList) throws RemoteException;
    void resetAll() throws RemoteException;
    void addMessageList(String message) throws RemoteException;
    ArrayList<String> getMessageList() throws RemoteException;
    void renew() throws RemoteException;
}
