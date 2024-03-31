/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author XPS
 */
public class ServerThread implements Runnable{
    ServerSocket server;
    ChatServerGui servergui;
    public boolean condition = true;
    
    //init constructor
    public ServerThread(int port, ChatServerGui servergui){
        servergui.displayMsg("Server starting at port " + port);
        //servergui.displayMsg();
        try{
            this.servergui = servergui;
            server = new ServerSocket(port);
            servergui.displayMsg("Server started");
        }
        catch(Exception e){
            servergui.displayMsg("Exception: " + e.getMessage());
        }
    }
    
    @Override
    public void run(){
        try{
            while(condition){
                //awaits connection from all users
                Socket socket = server.accept();
                //init new socket thread
                //start thread
                new Thread(new SocketThread(socket,servergui)).start();
            }
        }
        catch(Exception e){
            servergui.displayMsg("Server Thread Exception: " + e.getMessage());
        }
    }
    
    public void stop(){
        try{
            server.close();
            //stop listening
            //break out from while loop
            condition = false;
            System.out.println("Server has closed");
            //exit program
            System.exit(0);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
