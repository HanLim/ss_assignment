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
import static java.lang.Math.toIntExact;
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
            
            new Thread(new ServerThread(socket)).start();
        }
        
    }
    
}

class ServerThread implements Runnable {
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
                try{
                    Path p = Paths.get("users");
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
                ArrayList postList = new ArrayList();
                BufferedReader postFile = new BufferedReader(new FileReader("posts.txt"));
                    String post;
                    while ((post = postFile.readLine()) != null) {
                        postList.add(post);
                    }
                oos.writeObject(postList);
                ArrayList musicList = new ArrayList();
                File[] files = new File("music").listFiles();
                for (File mf : files) {
                    if (mf.isFile()) {
                        musicList.add(mf.getAbsolutePath());
                    }
                }
                oos.writeObject(musicList);
                oos.flush();
            }
            else if(cmd.equals("offline")) {
                username = ois.readUTF();
                BufferedReader file = new BufferedReader(new FileReader("online-users.txt"));
                    String line;
                    String input = "";
                    while ((line = file.readLine()) != null) {
                        if (line.equals(username) == false) {
                            input += line + System.getProperty("line.separator");
                        }
                    }
                    FileOutputStream File = new FileOutputStream("online-users.txt");
                    File.write(input.getBytes());
                    file.close();
                    File.close();
            }
            else if(cmd.equals("post")){
                try{
                    Path p = Paths.get("posts.txt");
                    if(!Files.exists(p)){
                        File txt = new File("posts.txt");
                        txt.createNewFile();
                    }
                    String post = ois.readUTF();
                    Files.write(Paths.get("posts.txt"), (post+ System.getProperty("line.separator")).getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {System.out.println(e);}
            }
            else if(cmd.equals("upload")){
                Path p = Paths.get("music");
                if(!Files.exists(p)){
                    (new File("music")).mkdir();
                }
                String filename = ois.readUTF();
                int filesize = toIntExact((long)ois.readObject());
                DataInputStream dis = new DataInputStream(socket.getInputStream());
		FileOutputStream fos = new FileOutputStream("music/"+filename);
		byte[] buffer = new byte[16*1024];
		
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			fos.write(buffer, 0, read);
		}
            }
            else {
                ArrayList list = new ArrayList();
                String filename = "users/" + cmd + ".txt";
                BufferedReader file = new BufferedReader(new FileReader(filename));
                    String user;
                    while ((user = file.readLine()) != null) {
                        list.add(user);
                    }
                oos.writeObject(list);
            }
        } catch (Exception e){
               System.out.println(e);
               try {
                    socket.close();
               } catch (IOException ex) {
                   Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
               }
               return;
           }
       }
    }
}