/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import domain.Configuration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Renato Oliveira 1140822@isep.ipp.pt
 */
public class TcpClientThread extends Thread {

    /**
     * A stream used to read data from a socket.
     */
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private Socket sockListener;

    public TcpClientThread(Socket sockListener) {
        try {
            this.sockListener = sockListener;
            dataIn = new DataInputStream(sockListener.getInputStream());
            dataOut = new DataOutputStream(sockListener.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(TcpClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            dataIn = new DataInputStream(sockListener.getInputStream());
            dataOut = new DataOutputStream(sockListener.getOutputStream());
            
            String fileName = readFileRequestInfo();
            File file = findFile(fileName);
            if (file != null) {
                sendFileSize(file);
                sendFile(file);
            }
        } catch (IOException ex) {
            Logger.getLogger(TcpClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reads information regarding the file requested by other user.
     *
     * @return the string containing the information
     * @throws IOException
     */
    private String readFileRequestInfo() throws IOException {
        int fileSize = 0;
        fileSize = dataIn.readInt();
        int requestSize = fileSize;
        byte[] fileData = new byte[requestSize];
        dataIn.readFully(fileData, 0, fileData.length);
        String downloadInfo = new String(fileData);
        return downloadInfo;
    }

    /**
     * Sends the file size of a file to the destination ip address configured
     *
     * @param file the file to send its size
     * @return true if file size sent
     * @throws FileNotFoundException if file not found
     * @throws IOException if failed to write
     */
    public boolean sendFileSize(File file) throws FileNotFoundException, IOException {
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        dataOut.writeInt(fileBytes.length);
        return true;
    }

    /**
     * Sends a file to a waiting host.
     *
     * @param file the file to send
     * @return true if file is sent
     * @throws IOException if failed to write the file
     */
    public boolean sendFile(File file) throws IOException {
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        dataOut.write(fileBytes);
        return true;
    }

    public File findFile(String fileName) {
        File folder = new File(Configuration.getSharedFolderName());
        File[] files = folder.listFiles();
        for (File file : files) {
            boolean isEqual = file.getName().equals(fileName);
            if (isEqual) {
                return file;
            }
        }
        return null;
    }
}
