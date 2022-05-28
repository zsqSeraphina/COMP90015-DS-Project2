package src.utils;

import src.server.IWhiteBoardServant;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class FileHandler {

    public static void saveFile(IWhiteBoardServant server) throws RemoteException {
        if (server.getFileName() != null) {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(
                        server.getFileName()));
                outputStream.writeObject(server.getShapes());
                outputStream.close();
                JOptionPane.showMessageDialog(null, "File saved!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e + ", please try again later",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } else {
            saveAsFile(server);
        }
    }

    public static void saveAsFile(IWhiteBoardServant server) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter(
                "(*.wbd)", "wbd");
        fileChooser.setFileFilter(filter1);

        int chosen = fileChooser.showSaveDialog(null);
        if (chosen == JFileChooser.APPROVE_OPTION) {
            int closeFileConfirm = JOptionPane.NO_OPTION;
            boolean fileExist = new File(fileChooser.getSelectedFile() + ".wbd").exists();

            if (fileExist) {
                closeFileConfirm = JOptionPane.showConfirmDialog(null,
                        "File existed, your previous file will be overwritten",
                        "Name Conflict", JOptionPane.YES_NO_OPTION);
            }
            if (!fileExist || closeFileConfirm == JOptionPane.YES_OPTION) {
                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(
                            fileChooser.getSelectedFile() + ".wbd"));
                    outputStream.writeObject(server.getShapes());
                    outputStream.close();
                    JOptionPane.showMessageDialog(null, "File saved!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, e + ", please try again later",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        }
    }

    public static void openFile(IWhiteBoardServant server) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "(*.wbd)", "wbd");
        chooser.setFileFilter(filter);

        int chosen = chooser.showOpenDialog(null);
        if (chosen == JFileChooser.APPROVE_OPTION) {
            try {
                server.setFileName(chooser.getSelectedFile().getAbsolutePath());
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(
                        chooser.getSelectedFile()));
                Object board = inputStream.readObject();
                ConcurrentHashMap<Point, Shape> shapes = (ConcurrentHashMap<Point, Shape>) board;
                server.setShapes(shapes);
                inputStream.close();
                JOptionPane.showMessageDialog(null, "File opened!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e + ", please try again later",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }
}
