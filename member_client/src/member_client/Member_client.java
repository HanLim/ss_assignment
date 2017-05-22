/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package member_client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import java.net.Socket;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Han Lim
 */
public class Member_client implements Serializable {

    public JFrame mainFrame, registerFrame;
    public Socket socket;
    OutputStream os ;
    ObjectOutputStream oos;
    InputStream is;
    ObjectInputStream ois;
    public void register() {
        try{
            socket = new Socket("localhost",2000);
        } catch(IOException e) {
            System.out.println(e);
        }
        
        ArrayList profileChosen = new ArrayList();
        String profileType[] = {"opera", "pop music", "rock music"};
        registerFrame = new JFrame("Music Social Network - Registration");
        
        JLabel lblName = new JLabel("Username", SwingConstants.RIGHT);
        JLabel lblPlace = new JLabel("Place of Birth", SwingConstants.RIGHT);
        JLabel lblDob = new JLabel ("Date of Birth", SwingConstants.RIGHT);
        JLabel lblProfile = new JLabel("Music Profile");
        
        JTextField txtName = new JTextField();
        JTextField txtPlace = new JTextField();
        JTextField txtDob = new JTextField();
        JTextArea txtProfile = new JTextArea();
        
        JButton btnRegister = new JButton("Register");
        JButton btnCancel  = new JButton("Cancel");
        JButton btnAddProfile = new JButton("Add");
        
        
        JComboBox cbProfile = new JComboBox(profileType);
        
        lblName.setBounds(30, 70, 100, 20);
        lblPlace.setBounds(30, 110, 100, 20);
        lblDob.setBounds(30, 150, 100, 20);
        lblProfile.setBounds(400, 20, 100, 20);
        
        txtName.setBounds(150, 70, 130, 20);
        txtPlace.setBounds(150, 110, 130, 20);
        txtDob.setBounds(150, 150, 130, 20);
        txtProfile.setBounds(400, 70, 300, 200);
        txtProfile.setEditable(false);
        txtProfile.setBackground(Color.white);
        
        btnRegister.setBounds(20, 250, 100, 20);
        btnRegister.addActionListener((ActionEvent e) -> {
            if(txtName.getText().trim().isEmpty() || txtPlace.getText().trim().isEmpty() || txtDob.getText().trim().isEmpty() || profileChosen.size() < 1){
                JOptionPane.showMessageDialog(null, "Please fill in all the field", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                ArrayList al = new ArrayList();
                al.add(txtName.getText());
                al.add(txtPlace.getText());
                al.add(txtDob.getText());
                Iterator<String> iterator = profileChosen.iterator();
		while (iterator.hasNext()) {
                        al.add(iterator.next());
		}
                try {
                    os = socket.getOutputStream();
                    oos = new ObjectOutputStream(os);
                    oos.writeUTF("registration");
                    oos.writeObject(al);
                    oos.flush();
                    registerFrame.dispose();
                    mainApp(txtName.getText());
                }catch(IOException ex){
                    System.out.println(ex);
                    JOptionPane.showMessageDialog(null, "Connection refused, please check your connection", "Connection refused", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnCancel.setBounds(150, 250, 100, 20);
        btnCancel.addActionListener((ActionEvent e) -> {
//            System.exit(0);
        });
        btnAddProfile.setBounds(650, 40, 100, 20);
        btnAddProfile.addActionListener((ActionEvent e) -> {
            String chosen = cbProfile.getSelectedItem().toString();
            if(!profileChosen.contains(chosen)){
                profileChosen.add(cbProfile.getSelectedItem().toString());
                Iterator<String> iterator = profileChosen.iterator();
                txtProfile.setText(null);
		while (iterator.hasNext()) {
                        txtProfile.append(iterator.next() + "\n");
		}
            }
        });
        cbProfile.setBounds(400, 40, 200, 20);
        
        registerFrame.add(lblName);
        registerFrame.add(lblPlace);
        registerFrame.add(lblDob);
        registerFrame.add(lblProfile);
        
        registerFrame.add(txtName);
        registerFrame.add(txtPlace);
        registerFrame.add(txtDob);
        registerFrame.add(txtProfile);
        
        registerFrame.add(btnRegister);
        registerFrame.add(btnCancel);
        registerFrame.add(btnAddProfile);
        
        registerFrame.add(cbProfile);
        
        registerFrame.setResizable(false);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setLayout(null);
        registerFrame.setBounds(100, 100, 800, 350);
        registerFrame.setVisible(true);
    }

    public static void main(String[] args) {
        Member_client m = new Member_client();
        m.register();
    }
    
    public void mainApp(String name){
        mainFrame = new JFrame("NTU Music Social Network - Username: " + name);
        
        JLabel lblFriends = new JLabel("Friends");
        JLabel lblInfo = new JLabel("Info");
        JLabel lblSongs = new JLabel("Shared songs");
        JLabel lblFriendPost =  new JLabel("Friend Post");
        JLabel lblUserPost = new JLabel("Post: ");
        JLabel lblOnline = new JLabel("Online Users");
        JLabel lblRequest = new JLabel("Friendship Request from");
        
        JButton btnPlay = new JButton("Play");
        JButton btnChat = new JButton("Chat");
        JButton btnSendPost = new JButton("Send");
        JButton btnRequest = new JButton("Add Friend");
        JButton btnAccept = new JButton("Accept");
        JButton btnRefuse = new JButton("Refuse");
        
        JTextArea txtAPost = new JTextArea();
        JTextArea txtAInfo = new JTextArea();
        
        JTextField txtFPost = new JTextField();
        
        JPanel pnlFriend = new JPanel();
        JPanel pnlSongs = new JPanel();
        
        JScrollPane spFriend = new JScrollPane(pnlFriend);
        JScrollPane spSong = new JScrollPane(pnlSongs);
        
        lblFriends.setBounds(20,20,100,20);
        lblInfo.setBounds(200,20,100,20);
        lblSongs.setBounds(400,20,100,20);
        lblFriendPost.setBounds(20,260,100,20);
        lblUserPost.setBounds(20, 440, 100, 30);
        lblOnline.setBounds(50, 480, 200, 30);
        lblRequest.setBounds(320, 480, 200, 30);
        
        btnChat.setBounds(50, 220, 100, 30);
        btnPlay.setBounds(400, 220, 100, 30);
        btnSendPost.setBounds(465, 440, 100, 30);
        btnRequest.setBounds(180, 520, 100, 50);
        btnAccept.setBounds(480, 520, 100, 50);
        btnRefuse.setBounds(480, 590, 100, 50);
        
        txtAPost.setBounds(20, 280, 550, 150);
        txtAPost.setEditable(false);
        txtAInfo.setBounds(200, 50, 170, 150);
        txtAInfo.setEditable(false);
        
        txtFPost.setBounds(70, 440, 380, 30);
        
        spFriend.setBounds(20, 50, 170, 150);
        spSong.setBounds(400, 50, 170, 150);
        
        mainFrame.add(lblFriends);
        mainFrame.add(lblInfo);
        mainFrame.add(lblSongs);
        mainFrame.add(lblFriendPost);
        mainFrame.add(lblUserPost);
        mainFrame.add(lblOnline);
        mainFrame.add(lblRequest);
        
        mainFrame.add(btnChat);
        mainFrame.add(btnPlay);
        mainFrame.add(btnSendPost);
        mainFrame.add(btnAccept);
        mainFrame.add(btnRequest);
        mainFrame.add(btnRefuse);
        
        mainFrame.add(txtAPost);
        mainFrame.add(txtAInfo);
        
        mainFrame.add(txtFPost);
        
        mainFrame.add(spFriend);
        mainFrame.add(spSong);
        
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setBounds(100, 100, 600, 800);
        mainFrame.setVisible(true);
        try{
            socket = new Socket("localhost", 2000);
        } catch (IOException ex) { 
            Logger.getLogger(Member_client.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(new onlinUser(socket, pnlFriend, txtAInfo)).start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(new logout(socket, name)));
    }
    
    class logout implements Runnable{
        protected Socket socket;
        protected String name;
        logout(Socket socket, String name){
            this.socket = socket;
            this.name = name;
        }
        public void run() {
            try{
                    os = socket.getOutputStream();
                    oos = new ObjectOutputStream(os);
                    oos.writeUTF("offline");
                    oos.writeUTF(name);
                    oos.flush();
                } catch(Exception e) {System.out.println(e);}
        }
    }
    
    class onlinUser implements Runnable{
        protected Socket socket;
        protected JPanel panel;
        protected JTextArea area;
        public onlinUser (Socket socket, JPanel panel, JTextArea area){
            this.socket = socket;
            this.panel = panel;
            this.area = area;
        }
        public void run(){
            
            try{
                os = socket.getOutputStream();
                oos = new ObjectOutputStream(os);
                oos.writeUTF("online");
                oos.flush();
                is = socket.getInputStream();
                ois = new ObjectInputStream(is);
                ArrayList al = (ArrayList)ois.readObject();
                Iterator<String> iterator = al.iterator();
                JButton tempBtn;
                while (iterator.hasNext()) {
                    String username = iterator.next();
                    tempBtn = new JButton(username);
                    tempBtn.setBounds(0,0,100,100);
                    tempBtn.addActionListener((ActionEvent e) -> {
                        try {
                            os = socket.getOutputStream();
                            oos = new ObjectOutputStream(os);
                            oos.writeUTF(username);
                            oos.flush();
                            is = socket.getInputStream();
                            ois = new ObjectInputStream(is);
                            ArrayList info = (ArrayList)ois.readObject();
                            info.remove(info.size() - 1);
                            Iterator<String> iterateInfo = info.iterator();
                            area.setText(null);
                            while(iterateInfo.hasNext()){
                                area.append(iterateInfo.next()+System.getProperty("line.separator"));
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Member_client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    });
                    panel.add(tempBtn);
                }
            } catch (Exception e){System.out.println(e);}
        }      
    }
}
