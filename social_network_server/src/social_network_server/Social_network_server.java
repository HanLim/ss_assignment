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
            new EchoThread(socket).start();               
        }
        
    }
    
}

class EchoThread extends Thread {
    protected Socket socket;

    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
       while(true) {
           try{
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            ArrayList al = (ArrayList)ois.readObject();
            System.out.println(al);
            System.out.println(socket.getRemoteSocketAddress().toString());
           } catch (Exception e){
               System.out.println(e);
               try {
                   socket.close();
               } catch (IOException ex) {
                   Logger.getLogger(EchoThread.class.getName()).log(Level.SEVERE, null, ex);
               }
               return;
           }
       }
    }
}