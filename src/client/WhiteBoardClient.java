package src.client;

import src.server.IWhiteBoardServant;

import javax.swing.*;
import java.rmi.ConnectException;
import java.rmi.Naming;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class WhiteBoardClient {

    public static IWhiteBoardServant server;

    public static void main(String[] args) {
        if (3 != args.length) {
            throw new IllegalArgumentException
                    ("Please enter IP address, port and username");
        }

        String serverIpAddress;
        int port;
        String username;


        try {
            serverIpAddress = args[0];
            port = Integer.parseInt(args[1]);
            username = args[2];

            // can type username through UI
//            username = JOptionPane.showInputDialog(null, "Please enter your username",
//                    "Username", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e) {
            throw new IllegalArgumentException
                    ("Error:" + e
                            + ", Please enter correct IP address, port and username");
        }

        String registration = "rmi://" + serverIpAddress + ":" + port + "/WhiteBoardServer";
        try{
            try {
                server = (IWhiteBoardServant) Naming.lookup(registration);
            } catch (ConnectException e) {
                JOptionPane.showMessageDialog(null, "No server found, please try again later",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            System.out.println("Client connected!");
            if (server.getUserList().containsKey(username)) {
                JOptionPane.showMessageDialog(null, "Username already in use",
                        "Username used", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            server.addUser(username);
            new Thread(new CandidateListener(username, server)).start();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e + ", please try again later",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

    }
}
