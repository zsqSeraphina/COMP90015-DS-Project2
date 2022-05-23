package src.gui;

import src.constants.PaintOptionType;
import src.constants.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserInfoPanel extends JPanel {

    private final ConcurrentHashMap<String, String> userList;
    private final boolean isManager;

    UserInfoPanel(boolean isManager, ConcurrentHashMap<String, String> userList) {
        this.setPreferredSize(new Dimension(200,400));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.isManager = isManager;
        this.userList = userList;
        //reloadList(userList);
    }

    public void reloadList(ConcurrentHashMap<String, String> users) {
        removeAll();
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(180, 360));
        for (String user : users.keySet()) {
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
            wrapper.setPreferredSize(new Dimension(180, 80));
            wrapper.add(Box.createRigidArea(new Dimension(10, 0)));
            JLabel userName = new JLabel(user);
            userName.setAlignmentX(LEFT_ALIGNMENT);
            wrapper.add(userName, BorderLayout.WEST);
            wrapper.add(Box.createRigidArea(new Dimension(20, 0)));
            if (isManager) {
                JButton kickButton = new JButton("Kick out");
                kickButton.setAlignmentX(RIGHT_ALIGNMENT);
                wrapper.add(kickButton, BorderLayout.EAST);
            }
            wrapper.setBorder(BorderFactory.createLineBorder(Color.black));
            container.add(wrapper);
        }
        add(container);
        validate();
        repaint();
    }

    public void print() {
        System.out.println(userList);
    }


}
