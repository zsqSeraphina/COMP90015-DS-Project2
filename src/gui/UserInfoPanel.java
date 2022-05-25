package src.gui;

import src.interfaces.IWhiteBoardServant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public class UserInfoPanel extends JPanel {

    private static ConcurrentHashMap<String, String> userList;
    private boolean isManager = false;
    public JPanel container = new JPanel();
    private String username;
    private static IWhiteBoardServant server;

    UserInfoPanel(String username, ConcurrentHashMap<String, String> userList, IWhiteBoardServant server) {
        this.setPreferredSize(new Dimension(200,290));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        if (userList.get(username).equals("Manager")) {
            this.isManager = true;
        }
        this.username = username;
        this.userList = userList;
        this.server = server;
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(200, 180));
        JScrollPane scrollContainer = new JScrollPane(container);
        scrollContainer.getViewport().setPreferredSize(new Dimension(280, 180));
        scrollContainer.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollContainer);
        reloadList(userList);
    }

    public void reloadList(ConcurrentHashMap<String, String> users) {

        container.removeAll();
        userList = users;

        for (String user : users.keySet()) {
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
            wrapper.setPreferredSize(new Dimension(180, 80));
            wrapper.add(Box.createRigidArea(new Dimension(10, 0)));
            JLabel userLabel = new JLabel(user);
            userLabel.setAlignmentX(LEFT_ALIGNMENT);
            wrapper.add(userLabel, BorderLayout.WEST);
            wrapper.add(Box.createRigidArea(new Dimension(20, 0)));
            if (isManager && !users.get(user).equals("Manager")) {
                JButton kickButton = new JButton("Kick out");
                kickButton.setAlignmentX(RIGHT_ALIGNMENT);
                kickButton.addActionListener(new KickListener(userLabel.getText()));
                wrapper.add(kickButton, BorderLayout.EAST);
            }
            container.add(wrapper);
        }
        this.revalidate();
        this.repaint();
    }


    record KickListener(String username) implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            userList.remove(username);
            try {
                server.setUserList(userList);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

}
