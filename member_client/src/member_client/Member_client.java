/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package member_client;

import com.sun.javafx.application.PlatformImpl;
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
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


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
    static MediaPlayer mediaPlayer;
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
                al.add(txtName.getText().trim());
                al.add(txtPlace.getText().trim());
                al.add(txtDob.getText().trim());
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
                    mainApp(txtName.getText().trim());
                }catch(IOException ex){
                    System.out.println(ex);
                    JOptionPane.showMessageDialog(null, "Connection refused, please check your connection", "Connection refused", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnCancel.setBounds(150, 250, 100, 20);
        btnCancel.addActionListener((ActionEvent e) -> {
            System.exit(0);
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
        JScrollPane spPost = new JScrollPane(txtAPost, 
   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
        btnPlay.addActionListener((ActionEvent e) -> {
            new Thread(new UploadMusic()).start();
        });
        btnSendPost.setBounds(465, 440, 100, 30);
        btnSendPost.addActionListener((ActionEvent e) -> {
            if(!txtFPost.getText().trim().isEmpty()){
                String post = name + ": " + txtFPost.getText().trim();
                new Thread(new sendPost(socket, post)).start();
                txtFPost.setText(null);
            }
        });
        btnRequest.setBounds(180, 520, 100, 50);
        btnAccept.setBounds(480, 520, 100, 50);
        btnRefuse.setBounds(480, 590, 100, 50);
        
        spPost.setBounds(20, 280, 550, 150);
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
        
        mainFrame.add(spPost);
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
        onlineData scheduled = new onlineData(socket, pnlFriend, txtAInfo, txtAPost, spPost, pnlSongs);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(scheduled, 0, 2*1000);
        Runtime.getRuntime().addShutdownHook(new Thread(new logout(socket, name)));
    }
    class UploadMusic implements Runnable {
        public void run(){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(mainFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
               File selectedFile = fileChooser.getSelectedFile();
                try {
                    Socket s = new Socket("localhost", 2000);                    
                    OutputStream os = s.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    oos.writeUTF("upload");
                    oos.flush();
                    oos.writeUTF(selectedFile.getName());
                    oos.flush();
                    oos.writeObject(selectedFile.length());
                    oos.flush();
                    System.out.println("selected");
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    FileInputStream fis = new FileInputStream(selectedFile);
                    byte[] buffer = new byte[4096];

                    while (fis.read(buffer) > 0) {
                            dos.write(buffer);
                    }
                    dos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Member_client.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        
    }
    class sendPost implements Runnable{
        protected Socket socket;
        protected String post;
        sendPost(Socket socket, String post){
            this.socket = socket;
            this.post = post;
        }
        public void run() {
            try{
                Socket s = new Socket("localhost", 2000);
                    OutputStream os = s.getOutputStream();
                ObjectOutputStream    oos = new ObjectOutputStream(os);
                    oos.writeUTF("post");
                    oos.writeUTF(post);
                    oos.flush();
                } catch(Exception e) {System.out.println(e);}
        }
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
                    oos.close();
                    os.close();
                    socket.close();
                } catch(Exception e) {System.out.println(e);}
           
        }
    }
    
    class onlineData extends TimerTask implements Runnable{
        protected Socket socket;
        protected JPanel panel;
        protected JTextArea area;
        protected JTextArea postArea;
        protected JScrollPane postpane;
        protected JPanel songpane; 
        public onlineData (Socket socket, JPanel panel, JTextArea area, JTextArea postArea, JScrollPane postpane, JPanel songpane){
            this.socket = socket;
            this.panel = panel;
            this.area = area;
            this.postArea = postArea;
            this.postpane = postpane;
            this.songpane = songpane;
        }
        @Override
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
                panel.removeAll();
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
                panel.revalidate();
                panel.repaint();
                ArrayList al2 = (ArrayList)ois.readObject();
                Iterator<String>iterator2 = al2.iterator();
                postArea.setText(null);
                while (iterator2.hasNext()) {
                    postArea.append(iterator2.next() + System.getProperty("line.separator"));
                }
                postArea.setCaretPosition(postArea.getDocument().getLength());
                
                ArrayList musiclist = (ArrayList)ois.readObject();
                Iterator<String> musicIterator = musiclist.iterator();
                songpane.removeAll();
                while (musicIterator.hasNext()) {
                    String songname = musicIterator.next();
                    tempBtn = new JButton(songname);
                    tempBtn.setBounds(0,0,100,100);
                    tempBtn.addActionListener((ActionEvent e) -> {
                        try {
                            PlatformImpl.startup(() -> {});
                            Media hit = new Media(new File(songname).toURI().toString());
                            mediaPlayer = new MediaPlayer(hit);
                            mediaPlayer.play();
                        } catch (Exception ex) {
                            Logger.getLogger(Member_client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    });
                    songpane.add(tempBtn);
                }
                songpane.revalidate();
            } catch (Exception e){System.out.println(e);}
        }      
    }
}
