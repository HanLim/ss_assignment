/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package social_network_server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Han Lim
 */
public class Social_network_server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ServerSocket ss = null;
        Socket socket = null;
        int port = 2000;
        try {
            ss = new ServerSocket(port); 
        }catch(Exception e){
            System.out.println(e);
        }
        while(true) {
            try {
                socket = ss.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            new Registration(socket).start();               
        }
        
    }
    
}

class Registration extends Thread {
    protected Socket socket;

    public Registration(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
       while(true) {
        try{
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            ArrayList al = (ArrayList)ois.readObject();
            try{
                Path p = Paths.get("users/info");
                if(!Files.exists(p)){
                    System.out.println("a");
                    (new File("users")).mkdir();
                }
                String name = "users/" + al.get(0).toString() + ".txt";
                File file = new File(name);
                file.createNewFile();
                try (PrintWriter  writer = new PrintWriter(file)) {
                    Iterator<String> iterator = al.iterator();
                    while (iterator.hasNext()) {
                        writer.println(iterator.next());
                    }
                    writer.println(socket.getRemoteSocketAddress().toString());
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {System.out.println(e);}
        } catch (Exception e){
               System.out.println(e);
               try {
                   socket.close();
               } catch (IOException ex) {
                   Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
               }
               return;
           }
       }
    }
}