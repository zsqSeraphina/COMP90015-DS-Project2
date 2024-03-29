package src.client;

import src.gui.WhiteBoardFrame;
import src.server.IWhiteBoardServant;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class UserListener implements Runnable {

    private final WhiteBoardFrame whiteBoardFrame;
    private final String username;
    private final IWhiteBoardServant server;

    public UserListener(WhiteBoardFrame whiteBoardFrame,
                 String username, IWhiteBoardServant server) {
        this.whiteBoardFrame = whiteBoardFrame;
        this.username = username;
        this.server = server;
    }

    @Override
    public void run() {
        boolean canvasClosed = false;
        while (true) {
            try {
                ConcurrentHashMap<String, String> userList = server.getUserList();
                ArrayList<String> rejectList = server.getRejectList();
                whiteBoardFrame.reloadList(userList);
                whiteBoardFrame.reloadShapes(server.getShapes());
                ArrayList<String> messages = server.getMessageList();
                whiteBoardFrame.reloadMessage(messages);
                if  (server.getCanvasClosed() && !canvasClosed) {
                    whiteBoardFrame.closeCanvas();
                    canvasClosed = true;
                } else if  (!server.getCanvasClosed() && canvasClosed) {
                    whiteBoardFrame.newCanvas();
                    canvasClosed = false;
                }

                // Exit when this user not in the list
                if (!userList.containsKey(username)) {
                    JOptionPane.showMessageDialog(null,
                            "You have been kicked out by the manager!",
                            "Closing alert", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                // Exit when manager exited
                if (!userList.containsValue("Manager")) {
                    JOptionPane.showMessageDialog(null,
                            "You have been kicked out by the manager!",
                            "Closing alert", JOptionPane.ERROR_MESSAGE);
                    server.resetAll();
                    System.exit(0);
                }

                if (userList.get(username).equals("Manager")) {
                    ArrayList<String> candidateList;
                    ArrayList<String> removeList = new ArrayList<>();
                    if (!(candidateList = server.getCandidateList()).isEmpty()) {
                        for (String candidate : candidateList) {
                            int joinConfirm = JOptionPane.showConfirmDialog(null,
                                    candidate + " wants to share your whiteboard!",
                                    "Join Request", JOptionPane.YES_NO_OPTION);
                            if (joinConfirm == JOptionPane.YES_OPTION) {
                                removeList.add(candidate);
                                userList.put(candidate, "User");
                                server.setUserList(userList);
                            } else {
                                removeList.add(candidate);
                                rejectList.add(candidate);
                                server.setRejectList(rejectList);
                            }
                        }
                        candidateList.removeAll(removeList);
                        server.setCandidateList(candidateList);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,  "Server closed, please try again later",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,  e + ", please try again later",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }
}
