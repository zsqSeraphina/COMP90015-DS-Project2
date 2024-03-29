package src.server;


import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class WhiteBoardServer {

    private static String serverIpAddress;
    private static int port;

    public static void main(String[] args) {
        if (2 != args.length) {
            throw new IllegalArgumentException
                    ("Please enter IP address, port");
        }
        try {
            serverIpAddress = args[0];
            port = Integer.parseInt(args[1]);
        } catch(Exception e) {
            throw new IllegalArgumentException
                    ("Error:" + e
                            + ", Please enter correct IP address, port");
        }
        try {
            WhiteBoardServant servant = new WhiteBoardServant();
            LocateRegistry.createRegistry(port);
            Naming.rebind("rmi://" + serverIpAddress + ":" + port + "/WhiteBoardServer", servant);
            System.out.println("White Board Server Started!");
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "name should in URL format",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "remote registry failed",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        }
    }
}
