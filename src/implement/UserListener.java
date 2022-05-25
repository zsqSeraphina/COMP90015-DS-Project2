package src.implement;

import src.gui.WhiteBoardFrame;
import src.interfaces.IWhiteBoardServant;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class UserListener implements Runnable {

    private final WhiteBoardFrame whiteBoardFrame;
    private final String username;
    private final IWhiteBoardServant server;

    UserListener(WhiteBoardFrame whiteBoardFrame,
                 String username, IWhiteBoardServant server) {
        this.whiteBoardFrame = whiteBoardFrame;
        this.username = username;
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ConcurrentHashMap<String, String> userList = server.getUserList();
                whiteBoardFrame.reloadList(userList);
                whiteBoardFrame.reloadShapes(server.getShapes());
                ArrayList<String> messages = server.getMessageList();
                whiteBoardFrame.reloadMessage(messages);

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
                            }
                        }
                        candidateList.removeAll(removeList);
                        server.setCandidateList(candidateList);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
