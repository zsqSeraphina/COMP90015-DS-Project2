package src.implement;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteBoardServer {

    private static String serverIpAddress;
    private static int port;

    public static void main(String[] args) {
        //Utils.checkArgs(args, serverIpAddress, port, "", false);
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
            Registry registry = LocateRegistry.createRegistry(port);
            Naming.rebind("rmi://" + serverIpAddress + ":" + port + "/WhiteBoardServer", servant);
            System.out.println("White Board Server Started!");
        } catch (RemoteException | MalformedURLException e) {
            // TODO: popup message
            e.printStackTrace();
            System.exit(0);
        }
    }
}
