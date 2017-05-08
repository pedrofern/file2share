/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.swing;

import application.ChangeConfigurationController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Pedro Fernandes
 */
public class ChangeConfigurationsUI extends JDialog{
    
    private ChangeConfigurationController controller;
    
    private JTextField txtUDPPort;
    private JTextField txtUDPTime;
    private JTextField txtRefreshFile;
    private JTextField txtShared;
    private JTextField txtDownload;
    
    private JButton saveBtn;
    private JButton editBtn;
    
    private static final int WIDTH = 500, LENGTH = 400;
    
    private static final String UDP_PORT = "UDP Port Number/";
    private static final String UDP_TIME = "UDP Time Annoucement: ";
    private static final String REFRESH_TIME = "Refresh Time Annoucement: ";
    private static final String SHARED_FOLDER = "Shared Folder: ";
    private static final String DOWNLOAD_FOLDER = "Download Folder: ";
    private static final Dimension LABEL_SIZE = new JLabel(UDP_TIME).
                                                        getPreferredSize(); 
    
    public ChangeConfigurationsUI(JFrame frame) throws IOException{
        
        super(frame, "Configurations", true);
        
        controller = new ChangeConfigurationController();
        
        add(createComponents()); 
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        pack();
        setResizable(true);
        setMinimumSize(new Dimension(WIDTH, LENGTH));
        setLocationRelativeTo(null);        
        setVisible(true);
        
    }
    
    private JPanel createComponents(){
        JPanel panel = new JPanel(new BorderLayout());
                
        panel.add(createPanelImage(), BorderLayout.WEST);
        panel.add(createGeneralPanel(), BorderLayout.CENTER);
        panel.add(createPanelButons(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createGeneralPanel(){
        
        JPanel p = new JPanel(new GridLayout(5,1));
        
        p.setBorder(BorderFactory.createTitledBorder("Configurations:"));
 
        txtUDPPort = new JTextField(30);
        txtUDPPort.requestFocusInWindow();
        txtUDPPort.setText("" + controller.currentUDPPortNumber());
        txtUDPPort.setEditable(false);
        
        txtUDPTime = new JTextField(30);
        txtUDPTime.setText("" + controller.currentUDPTimeAnnoucement());
        txtUDPTime.setEditable(false);
        
        txtRefreshFile = new JTextField(30);
        txtRefreshFile.setText("" + controller.currentRefreshFileTime());
        txtRefreshFile.setEditable(false);
        
        txtShared = new JTextField(30);
        txtShared.setText(controller.currentSharedFolderName());
        txtShared.setEditable(false);
        
        txtDownload = new JTextField(30);
        txtDownload.setText(controller.currentDownloadFolderName());
        txtDownload.setEditable(false);

        p.add(createPanelLabelTextLabel(UDP_PORT, txtUDPPort));
        p.add(createPanelLabelTextLabel(UDP_TIME, txtUDPTime));
        p.add(createPanelLabelTextLabel(REFRESH_TIME, txtRefreshFile));
        p.add(createPanelLabelTextLabel(SHARED_FOLDER, txtShared));
        p.add(createPanelLabelTextLabel(DOWNLOAD_FOLDER, txtDownload));
        
        return p;
    }
    
    private JPanel createPanelLabelTextLabel(String label1, JTextField text) {
        JLabel lb1 = new JLabel(label1, JLabel.RIGHT);
        lb1.setPreferredSize(LABEL_SIZE);
        
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));

        p.add(lb1);
        p.add(text);

        return p;
    }
    
    private JPanel createPanelButons(){
        
        FlowLayout l = new FlowLayout();

        l.setHgap(20);
        l.setVgap(20);

        JPanel p = new JPanel(l);
        
        p.setBorder(BorderFactory.createTitledBorder("Options:"));
        
        JButton bt1 = createButonSave();
        JButton bt2 = createButonEdit();
        
        getRootPane().setDefaultButton(bt1);
        
        p.add(bt1);
        p.add(bt2);
        
        return p;
    }
    
    private JButton createButonSave(){
        saveBtn = new JButton("Save");
        saveBtn.setMnemonic(KeyEvent.VK_S);
        saveBtn.setToolTipText("Save new configurations");
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Integer UDPPortNumber = Integer.parseInt(txtUDPPort.getText());
                    Integer UDPTimeAnnouncement = Integer.parseInt(txtUDPTime.getText());
                    Integer refreshFileTime = Integer.parseInt(txtRefreshFile.getText());
                    String sharedFolderName = txtShared.getText();
                    String downloadFolderName = txtDownload.getText();                    
                    
                    if(controller.saveConfigurations(UDPPortNumber, UDPTimeAnnouncement,
                        refreshFileTime, sharedFolderName, downloadFolderName)){
                        finish();
                    }else{
                        JOptionPane.showMessageDialog(
                                    null,
                                    "It was not possible change configurations!\n",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE); 
                    }
                }catch (IllegalStateException ex ){
                    JOptionPane.showMessageDialog(
                                    null,
                                    "Invalid data!",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE); 
                } catch (NumberFormatException ne){
                    JOptionPane.showMessageDialog(
                                    null,
                                    "Invalid data!",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE); 
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ChangeConfigurationsUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(
                                    null,
                                    "Problem with configuration file!",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE); 
                }       
            }
        });
        return saveBtn;
        
    }
    
    private JButton createButonEdit(){
        editBtn = new JButton("Edit");
        editBtn.setMnemonic(KeyEvent.VK_S);
        editBtn.setToolTipText("Edit configurations");
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtUDPPort.setEditable(true);
                txtUDPTime.setEditable(true);        
                txtRefreshFile.setEditable(true);        
                txtShared.setEditable(true);        
                txtDownload.setEditable(true);
                saveBtn.setEnabled(true);    
            }
        });
        return editBtn;
        
    }
    
    private JPanel createPanelImage() {
        ImageIcon background = new ImageIcon("src/main/resources/config.png");
        
        JLabel label = new JLabel();
        label.setIcon(background);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 10, 10, 10));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }
    
    private void finish() {
        JOptionPane.showMessageDialog(
                                    null,
                                    "Configurations changed successfully!",
                                    "Configurations",
                                    JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
}