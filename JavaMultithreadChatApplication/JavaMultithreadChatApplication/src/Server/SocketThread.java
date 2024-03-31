/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 *
 * @author XPS
 */
public class SocketThread implements Runnable{
    
    Socket socket;
    ChatServerGui servergui;
    DataInputStream dis;
    StringTokenizer st;
    String client;
    
    //init constructor
    public SocketThread(Socket socket, ChatServerGui servergui){
        this.servergui = servergui;
        this.socket = socket;
        try{
            dis = new DataInputStream(socket.getInputStream());
        }
        catch(Exception e){
            servergui.displayMsg("Socket Thread Exception:" + e.getMessage());
        }
    }
    
    @Override
    public void run(){
        //recieve data from client
        try{
            while(true){
                //loop runs continuosly
                String data = dis.readUTF();
                st = new StringTokenizer(data);
                String mode = st.nextToken();
                //switch btw different command scenarios
                
                switch(mode){
                    case "MODE_JOIN" -> {
                        //TOKEN: clientusername
                        //client joins the chat
                        //client first time connects
                        String clientusername = st.nextToken();
                        client = clientusername;
                        servergui.addSocketList(socket);
                        servergui.addClientList(clientusername);
                        servergui.displayMsg("Client: " + clientusername + " has joined the chat");
                        break;
                    }
                    case "MODE_CHAT" -> {
                        //TOKEN:  sender | reciever | msg
                        String sender = st.nextToken();
                        String reciever = st.nextToken();
                        String item = "";
                        while(st.hasMoreTokens()){
                            //item
                            item = item + " " + st.nextToken();
                        }
                        Socket csoc = servergui.printClientList(reciever);
                        try{
                            DataOutputStream dos = new DataOutputStream(csoc.getOutputStream());
                            String msg = sender + ": " + item;
                            dos.writeUTF("MODE_MSG " + msg);
                            servergui.displayMsg(sender + " to " + reciever + " : " + item); 
                        }
                        catch(Exception e){
                            servergui.displayMsg("Error Exception: " + e.getMessage());
                        }
                        break;
                    }
                    case "MODE_BROADCAST" ->{
                        String brc_sender = st.nextToken();
                        String brc_msg = "";
                        while(st.hasMoreTokens()){
                            brc_msg = brc_msg + " " + st.nextToken();
                        }
                        //package
                        String brc_content = brc_sender + " " + brc_msg;
                        for(int i = 0; i < servergui.clientlist.size(); i++){
                            if(!servergui.clientlist.elementAt(i).equals(brc_sender)){
                                try{
                                    Socket csoc2  = (Socket) servergui.socketlist.elementAt(i);
                                    DataOutputStream dos2 = new DataOutputStream(csoc2.getOutputStream())  ;
                                    dos2.writeUTF("MODE_MSG " + brc_content);
                                    
                                }
                                catch(Exception e){
                                    servergui.displayMsg("Broadcast exception: " + e.getMessage());
                                }
                            }
                        }
                        servergui.displayMsg("Broadcast:  " + brc_content);
                        break;
                    }
                    default -> servergui.displayMsg("MODE not found " + mode);
                }
            }
        }
        catch(Exception e){
            System.out.println(client);
            servergui.removeClientFromList(client);
            servergui.displayMsg("Socket Thread Error: Client connection closed");
        }
    }
}
