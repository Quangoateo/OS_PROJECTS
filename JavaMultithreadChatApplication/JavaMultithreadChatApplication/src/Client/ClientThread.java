/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author XPS
 */
public class ClientThread implements Runnable {
    
    //read the comments
    Socket socket;
    //instead of buffered reader and writer, refer directly to dataiostream directly
    //listening for incoming thread
    DataInputStream dis;
    DataOutputStream dos;
    ChatClientGui clientgui;
    //tokenizer is used to break strings into tokens
    //use in place of string.split()
    StringTokenizer st;
    //readUTF & writeUTF is used inplace of readline from readline() and println()
    //replace place PrintWriter & BufferedReader
    
    
    //constructor declaration 
    public ClientThread(Socket socket, ChatClientGui clientgui){
        this.clientgui = clientgui;
        this.socket = socket;
        try{
            dis = new DataInputStream(socket.getInputStream());
        }
        catch(IOException e){
            System.out.println(e);
            clientgui.displayMsg("IOException Error: " + e.getMessage(), "Error", Color.RED, Color.RED);
        }
    }
    @Override
    public void run(){
        try{
            //loop runs continuously when thread is not interrupted
            while(!Thread.currentThread().isInterrupted()){
                //read data from input stream
                //create new string token contains data
                //has multiple scenarios eg connect, chat
                String data = dis.readUTF();
                st = new StringTokenizer(data);
                String mode = st.nextToken();
                switch(mode){
                    case "MODE_MSG" -> {
                        String msg = "";
                        String token = st.nextToken();
                        while(st.hasMoreTokens()){
                            msg = msg + " " + st.nextToken();
                        }
                        clientgui.displayMsg(msg, token, Color.DARK_GRAY, Color.BLUE);
                        break;
                    }
                    case "MODE_ONLINE" -> {
                        //i just learned that arraylist 
                        //are not threadsafe
                        //so ig we need to go with Vector
                        //huh :))
                        Vector online = new Vector();
                        while(st.hasMoreTokens()){
                            String element = st.nextToken();
                            //same username, then don't append twice
                            if(!element.equalsIgnoreCase(clientgui.username)){
                                online.add(element);
                            }  
                        }
                        clientgui.printOnlineList(online);
                        break;
                    }
                    default -> clientgui.displayMsg("Mode not found: " + mode, "Mode Error", Color.RED, Color.BLACK);
                    
                }   
            }
        }
        catch(Exception e){
            System.out.println("lost connection to server " + e);
            clientgui.displayMsg("Lost connection to server","Error", Color.RED, Color.BLACK);
        }
    }
}
