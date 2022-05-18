package src.Implement;

import java.rmi.RemoteException;

public class StartServer {

    private static String serverIpAddress;
    private static int port;
    private static String userName;

    public static void main(String[] args) {

        /* check the arguments and get the port and dictionary path */
        if (3 != args.length) {
            throw new IllegalArgumentException
                    ("Please enter IP address, port and username");
        }
    }
}
