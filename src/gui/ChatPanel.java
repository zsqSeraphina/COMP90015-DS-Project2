package src.gui;

import src.interfaces.IWhiteBoardServant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ChatPanel extends JPanel {
    private static ArrayList<String> messages;
    public JPanel container = new JPanel();
    public static JTextArea inputArea = new JTextArea();
    JPanel messageContainer =  new JPanel();
    private static IWhiteBoardServant server;

    ChatPanel(String username, ArrayList<String> messages, IWhiteBoardServant server) {
        this.setPreferredSize(new Dimension(200,200));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        ChatPanel.messages = messages;
        ChatPanel.server = server;
//        this.setLayout(new GridLayout(2, 1));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollContainer = new JScrollPane(container);
        scrollContainer.setPreferredSize(new Dimension(200, 120));
        scrollContainer.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollContainer.setBorder(BorderFactory.createLineBorder(Color.black));
        add(scrollContainer);

        inputArea.setPreferredSize(new Dimension(200, 80));
        inputArea.setBackground(Color.lightGray);
        inputArea.setMargin( new Insets(5,10,5,5) );
        inputArea.setWrapStyleWord(true);
        inputArea.setLineWrap(true);

        JButton sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(80, 40));

        messageContainer.add(inputArea, BorderLayout.WEST);
        messageContainer.add(sendButton, BorderLayout.EAST);
        sendButton.addActionListener(new sendListener(username));

        add(messageContainer);
        revalidate();
        repaint();
        reloadMessage(messages);
    }

    public synchronized void reloadMessage(ArrayList<String> messages) {
        container.removeAll();
        ChatPanel.messages = messages;
        for (String message : messages) {
            JLabel userLabel = new JLabel(message);
            userLabel.setAlignmentX(LEFT_ALIGNMENT);
            container.add(userLabel);
        }
        this.revalidate();
        this.repaint();
    }

    record sendListener(String username) implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                String message= inputArea.getText();
                if (!message.isBlank()) {
                    server.addMessageList(username + ": " + message);
                    inputArea.setText("");
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }
}
