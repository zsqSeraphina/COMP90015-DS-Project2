package src.server;

import src.utils.Shape;

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
    private ArrayList<String> rejectList;
    private ArrayList<String> messageList;
    private boolean canvasClosed = false;
    private String fileName = null;

    public WhiteBoardServant() throws RemoteException {
        super();
        this.userList = new ConcurrentHashMap<>();
        this.shapes = new ConcurrentHashMap<>();
        this.candidateList = new ArrayList<>();
        this.messageList = new ArrayList<>();
        this.rejectList = new ArrayList<>();
    }

    @Override
    public synchronized void addUser(String username)throws RemoteException {
        if (userList.isEmpty()) {
            this.userList.put(username, "Manager");
        } else {
            candidateList.add(username);
        }
    }

    @Override
    public synchronized ConcurrentHashMap<String, String> getUserList() throws RemoteException {
        return this.userList;
    }

    @Override
    public ConcurrentHashMap<Point, Shape> getShapes() throws RemoteException {
        return this.shapes;
    }

    @Override
    public void updateShapes(Point point, Shape shape) throws RemoteException {
        this.shapes.put(point, shape);
    }

    @Override
    public void setShapes(ConcurrentHashMap<Point, Shape> shapes) throws RemoteException {
        this.shapes = shapes;
    }
    @Override
    public void setUserList(ConcurrentHashMap<String, String> userList) throws RemoteException {
        this.userList = userList;
    }

    @Override
    public ArrayList<String> getCandidateList() throws RemoteException {
        return this.candidateList;
    }

    @Override
    public void setCandidateList(ArrayList<String> candidateList) throws RemoteException {
        this.candidateList = candidateList;
    }

    @Override
    public void resetAll() throws RemoteException {
        this.userList = new ConcurrentHashMap<>();
        this.shapes = new ConcurrentHashMap<>();
        this.candidateList = new ArrayList<>();
        this.messageList = new ArrayList<>();
        this.rejectList = new ArrayList<>();
        this.canvasClosed = false;
        this.fileName = null;
    }

    @Override
    public void addMessageList(String message) throws RemoteException {
        this.messageList.add(message);
    }

    @Override
    public ArrayList<String> getMessageList() throws RemoteException {
        return this.messageList;
    }

    @Override
    public void renew() throws RemoteException {
        this.shapes = new ConcurrentHashMap<>();
        this.candidateList = new ArrayList<>();
        this.messageList = new ArrayList<>();
        this.rejectList = new ArrayList<>();
        this.fileName = null;
    }

    @Override
    public boolean getCanvasClosed() throws RemoteException {
        return canvasClosed;
    }

    @Override
    public void setCanvasClosed(boolean canvasClosed) throws RemoteException {
        this.canvasClosed = canvasClosed;
    }

    @Override
    public ArrayList<String> getRejectList() throws RemoteException {
        return rejectList;
    }

    @Override
    public void setRejectList(ArrayList<String> rejectList) throws RemoteException {
        this.rejectList = rejectList;
    }

    @Override
    public String getFileName() throws RemoteException {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) throws RemoteException {
        this.fileName = fileName;
    }
}
