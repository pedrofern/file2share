/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import domain.Configuration;
import domain.ConfigurationBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;

/**
 * @author Pedro Fernandes
 */
public class ChangeConfigurationController {

    private final Configuration configuration;
    private String sharedFolder;
    private String downloadFolder;

    private static final String DEFAULT_FILENAME = "Config.txt";

    public ChangeConfigurationController() throws IOException {
        final StartConfiguration s = new StartConfiguration();
        this.configuration = s.readConfigurationFile();
        this.sharedFolder = Configuration.getSharedFolderName();
        this.downloadFolder = Configuration.getDownloadFolderName();
    }

    public Integer currentUDPPortNumber() {
        return Configuration.getUDPPortNumber();
    }

    public Integer currentUDPTimeAnnoucement() {
        return Configuration.getUDPTimeAnnouncement();
    }

    public Integer currentRefreshFileTime() {
        return Configuration.getRefreshFileTime();
    }

    public String currentDownloadFolderName() {
        return Configuration.getDownloadFolderName().replace("/", "");
    }

    public String currentSharedFolderName() {
        return Configuration.getSharedFolderName().replace("/", "");
    }

    public boolean saveConfigurations(Integer udpPort, Integer udpTime,
          Integer refreshTime,
          boolean ignoreFolders, boolean ignoreFilesWithoutExtension,
          String[] ignoreFiles, String[] ignoreFilesStartingWith,
          String[] ignoreFilesWithExtension) throws FileNotFoundException {
        
        final ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.withUDPPort(udpPort).withUDPTimeAnnouncement(udpTime)
                .withRefreshFileTime(refreshTime).withSharedFolderName(sharedFolder)
                .withDownloadFolderName(downloadFolder).withIgnoreFolders(ignoreFolders)
                .withIgnoreFilesWithoutExtension(ignoreFilesWithoutExtension)
                .withIgnoreFiles(ignoreFiles)
                .withIgnoreFilesStartingWith(ignoreFilesStartingWith)
                .withIgnoreFilesWithExtension(ignoreFilesWithExtension);

        final Configuration changedConfiguration = builder.buildConfiguration();

        if (changedConfiguration == null) {
            return false;
        }

        createConfigurationFile(changedConfiguration);

        return true;
    }

    private void createConfigurationFile(Configuration c) throws FileNotFoundException {

        File createFile = new File(DEFAULT_FILENAME);
        Formatter fileFormatter = new Formatter(createFile);

        fileFormatter.format("%s", c.toString());

        fileFormatter.close();

    }
    
    public String changeSharedFolder(String folder){
        return this.sharedFolder = folder;
    }
    
    public String changeDownloadFolder(String folder){
        return this.downloadFolder = folder;
    }

}
