package src.implement;

import src.constants.Shape;
import src.interfaces.IWhiteBoardServant;

import java.awt.*;
import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class WhiteBoardServant extends UnicastRemoteObject implements IWhiteBoardServant {

    @Serial
    private static final long serialVersionUID = -7231033280190281294L;
    private ConcurrentHashMap<String, String> userList;
    private ConcurrentHashMap<Point, Shape> shapes;
    private ArrayList<String> candidateList;
    private ArrayList<String> messageList;

    public WhiteBoardServant() throws RemoteException {
        super();
        this.userList = new ConcurrentHashMap<>();
        this.shapes = new ConcurrentHashMap<>();
        this.candidateList = new ArrayList<>();
        this.messageList = new ArrayList<>();
    }


    public synchronized ConcurrentHashMap<String, String> addUser(String username)throws RemoteException {
        if (userList.isEmpty()) {
            this.userList.put(username, "Manager");
        } else {
            candidateList.add(username);
        }
        return this.userList;
    }


    public synchronized ConcurrentHashMap<String, String> getUserList() throws RemoteException {
        return this.userList;
    }


    public ConcurrentHashMap<Point, Shape> getShapes() throws RemoteException {
        return this.shapes;
    }


    public void updateShapes(Point point, Shape shape) throws RemoteException {
        this.shapes.put(point, shape);
    }


    public void setUserList(ConcurrentHashMap<String, String> userList) throws RemoteException {
        this.userList = userList;
    }


    public ArrayList<String> getCandidateList() throws RemoteException {
        return this.candidateList;
    }


    public void setCandidateList(ArrayList<String> candidateList) throws RemoteException {
        this.candidateList = candidateList;
    }


    public void resetAll() throws RemoteException {
        this.userList = new ConcurrentHashMap<>();
        this.shapes = new ConcurrentHashMap<>();
        this.candidateList = new ArrayList<>();
        this.messageList = new ArrayList<>();
    }

    @Override
    public void addMessageList(String message) throws RemoteException {
        this.messageList.add(message);
        System.out.println(this.messageList);
    }

    @Override
    public ArrayList<String> getMessageList() throws RemoteException {
        return this.messageList;
    }
}
