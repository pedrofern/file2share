/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import domain.DataFile;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Renato Oliveira 1140822@isep.ipp.pt
 */
public class CommunicationController {

    /**
     * Class vars
     */
    private TcpConnection tcpConnection;

    /**
     * Class constructor receiving a string for the IP address.
     *
     * @param destinationIP the ip address
     */
    public CommunicationController(String destinationIP, int portNumber) {
        try {
            this.tcpConnection = new TcpConnection(destinationIP, portNumber);
        } catch (IOException ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends the data file size.
     *
     * @param dataFile the datafile
     * @return true if sending was OK.
     */
    public boolean sendDataFileSize(DataFile dataFile) {

        try {
            File tmpFile = dataFile.getFileFromDataFile();
            tcpConnection.sendFileSize(tmpFile);

        } catch (IOException ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    /**
     * Sends the Data File.
     *
     * @param dataFile the datafile
     * @return true if sending was OK
     */
    public boolean sendDataFile(DataFile dataFile) {
        try {
            File tmpFile = dataFile.getFileFromDataFile();
            tcpConnection.sendFile(tmpFile);
        } catch (IOException ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    /**
     * Downloads a data file
     *
     * @param fileName the NEW file name
     * @param path     the NEW path
     * @return true if file downloaded
     */
    public boolean downloadDataFile(String fileName, String path) {
        final String threadFileName = fileName;
        final String threadFilePath = path;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    int fileSize;
                    fileSize = tcpConnection.readFileSize();
                    tcpConnection.downloadFile(threadFileName, threadFilePath, fileSize);
                    this.join();
                } catch (IOException ex) {
                    Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
        return true;
    }

    /**
     * Reads information regarding the file requested by other user.
     *
     * @return the string containing the information
     */
    public String readFileRequestInfo() {
        try {
            return this.tcpConnection.readFileRequestInfo();
        } catch (IOException ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
