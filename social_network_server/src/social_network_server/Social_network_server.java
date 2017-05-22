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
import java.nio.file.StandardOpenOption;
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
            new ServerThread(socket).start();               
        }
        
    }
    
}

class ServerThread extends Thread {
    protected Socket socket;

    public ServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
       String username = null;
       while(true) {
        try{
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            String cmd = ois.readUTF();
            System.out.println(cmd);
            if(cmd.equals("registration"))
            {
                ArrayList al = (ArrayList)ois.readObject();
                username = al.get(0).toString();
                System.out.println(username);
                try{
                    Path p = Paths.get("users/info");
                    if(!Files.exists(p)){
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
                    Path txtPath = Paths.get("online-users.txt");
                    if(!Files.exists(txtPath)){
                        File txt = new File("online-users.txt");
                        txt.createNewFile();
                    }
                    Files.write(Paths.get("online-users.txt"), (username+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {System.out.println(e);}
            }
            else if(cmd.equals("online")){
                ArrayList list = new ArrayList();
                BufferedReader file = new BufferedReader(new FileReader("online-users.txt"));
                    String user;
                    while ((user = file.readLine()) != null) {
                        list.add(user);
                    }
                oos.writeObject(list);
                oos.flush();
            }
        } catch (Exception e){
               System.out.println(e);
               try {
                    BufferedReader file = new BufferedReader(new FileReader("online-users.txt"));
                    String line;
                    String input = "";
                    while ((line = file.readLine()) != null) {
                        if (line.contains(username)) {
                            line = "";
                            break;
                        }
                        input += line + System.getProperty("line.separator");
                    }
                    FileOutputStream File = new FileOutputStream("online-users.txt");
                    File.write(input.getBytes());
                    file.close();
                    File.close();
                    socket.close();
               } catch (IOException ex) {
                   Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
               }
               return;
           }
       }
    }
}