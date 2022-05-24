package src.implement;

import src.constants.Shape;
import src.gui.WhiteBoardFrame;
import src.interfaces.IWhiteBoardServant;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

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
        ConcurrentHashMap<Point, Shape> shapes = new ConcurrentHashMap<>();

        try {
            serverIpAddress = args[0];
            port = Integer.parseInt(args[1]);
//            userName = args[2];
            username = JOptionPane.showInputDialog(null, "Please enter your username", "Username", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e) {
            throw new IllegalArgumentException
                    ("Error:" + e
                            + ", Please enter correct IP address, port and username");
        }

        String registration = "rmi://" + serverIpAddress + ":" + port + "/WhiteBoardServer";
        try{
            server = (IWhiteBoardServant) Naming.lookup(registration);
            System.out.println("Client connected!");

            ConcurrentHashMap<String, String> userList = server.addUser(username);
            WhiteBoardFrame board = new WhiteBoardFrame(shapes, username, userList, server);

            new Thread(new UserListener(board, username, server), "UserListener.java").start();

        }catch (MalformedURLException | NotBoundException | RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
