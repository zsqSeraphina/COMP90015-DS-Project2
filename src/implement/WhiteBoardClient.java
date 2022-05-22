package src.implement;

import src.gui.WhiteBoardFrame;
import src.interfaces.IWhiteBoardServant;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class WhiteBoardClient {

    private static String serverIpAddress;
    private static int port;
    private static String userName;
    private static boolean isManager;
    private static volatile WhiteBoardFrame board;

    public static void main(String[] args) {
        if (3 != args.length) {
            throw new IllegalArgumentException
                    ("Please enter IP address, port and username");
        }

        try {
            serverIpAddress = args[0];
            port = Integer.parseInt(args[1]);
//            userName = args[2];
            userName = JOptionPane.showInputDialog(null, "Enter 3-8 letters user name", "Login", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e) {
            throw new IllegalArgumentException
                    ("Error:" + e
                            + ", Please enter correct IP address, port and username");
        }

        String registration = "rmi://" + serverIpAddress + ":" + port + "/WhiteBoardServer";
        try{
            IWhiteBoardServant server = (IWhiteBoardServant) Naming.lookup(registration);
            System.out.println("Client connected!");
            server.updateUser(userName);
            board = server.initialWhiteBoard();
        }catch (MalformedURLException | NotBoundException | RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
