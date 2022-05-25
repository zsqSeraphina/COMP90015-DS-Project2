package src.gui;

import src.server.IWhiteBoardServant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * @author Siqi Zhou
 * student id 903274
 */
public class ChatPanel extends JPanel {
    private ArrayList<String> messages;
    public JPanel container = new JPanel();
    public static JTextArea inputArea = new JTextArea();
    JPanel messageContainer =  new JPanel();
    private static IWhiteBoardServant server;

    ChatPanel(String username, ArrayList<String> messages, IWhiteBoardServant server) {
        this.setPreferredSize(new Dimension(180,200));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.messages = messages;
        ChatPanel.server = server;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollContainer = new JScrollPane(container);
        scrollContainer.getViewport().setPreferredSize(new Dimension(240, 100));
        scrollContainer.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollContainer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollContainer.setBorder(BorderFactory.createLineBorder(Color.black));
        JPanel wrapper = new JPanel();
        wrapper.add(scrollContainer);
        wrapper.setPreferredSize(new Dimension(240, 100));


        messageContainer.setPreferredSize(new Dimension(180, 40));
        inputArea.setPreferredSize(new Dimension(180, 40));
        inputArea.setBackground(Color.lightGray);
        inputArea.setMargin( new Insets(5,10,5,5) );
        inputArea.setBorder(BorderFactory.createLineBorder(Color.black));
        inputArea.setWrapStyleWord(true);
        inputArea.setLineWrap(true);

        JButton sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(80, 30));

        messageContainer.add(inputArea, BorderLayout.WEST);
        messageContainer.add(sendButton, BorderLayout.EAST);
        sendButton.addActionListener(new sendListener(username));

        add(wrapper);
        add(messageContainer);
        revalidate();
        repaint();
        reloadMessage(messages);
    }

    public synchronized void reloadMessage(ArrayList<String> messages) {
        container.removeAll();
        this.messages = messages;
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
                JOptionPane.showMessageDialog(null, e + ", please try again later",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }
}
