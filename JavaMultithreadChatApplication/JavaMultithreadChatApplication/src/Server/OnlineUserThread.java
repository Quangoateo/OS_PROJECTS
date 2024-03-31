/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author XPS
 */
public class OnlineUserThread implements Runnable {
    //call to the gui
    ChatServerGui servergui;
    
    //constructor
    public OnlineUserThread(ChatServerGui servergui){
        this.servergui = servergui;
    }
    
    @Override
    public void run(){
        //loop runs continuously 
        //when thread is not interrupted
        try{
            while(!Thread.interrupted()){
                String msg = "";
                //loop client list
                for(int i = 0; i < servergui.clientlist.size(); i++){
                    msg = msg + " " + servergui.clientlist.elementAt(i);
                }
                //loop socket list
                for(int i = 0; i < servergui.socketlist.size(); i++){
                    Socket csoc = (Socket) servergui.socketlist.elementAt(i);
                    DataOutputStream dos = new DataOutputStream(csoc.getOutputStream());
                    //MODE_CONNECT:
                    if(msg.length() > 0){
                        dos.writeUTF("MODE_ONLINE " + msg);
                    }
                }
                Thread.sleep(1000);
            }
        }
        catch(Exception e){
            servergui.displayMsg("Error  Interrupted Exception: " + e.getMessage());
        }
    }
}
