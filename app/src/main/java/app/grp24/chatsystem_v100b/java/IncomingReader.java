/*
 * Chat System
 * Lavet af Gruppe 24 - DTU 2016
 * --------------------
 * Magnus Andrias Nielsen, s141899
 * --------------------
 */

package app.grp24.chatsystem_v100b.java;

/**
 * Created by Magnus A. Nielsen on 16-05-2016.
 */

import app.grp24.chatsystem_v100b.fragment.ChatRoom_Frag;

public class IncomingReader implements Runnable {

    //Kan ikke access Main UI fra en anden Thread..
    // (txtChat.append(data[0] + ": " + data[1] + "\n");)
    // Virker derfor ikke efter hensigten

    @Override
    public void run(){
        String[] data;
        String stream, done = "Done", connect = "Connect", disconnect = "Disconnect",
                chat = "Chat", login = "Login", firstLogin = "firstLogin", loginError = "LoginError";

        try{

            while((stream = ChatRoom_Frag.reader.readLine()) != null){
                data = stream.split(":");

                if(data[2].equals(chat)){
                    ChatRoom_Frag.txtChat.append(data[0] + ": " + data[1] + "\n");

                }else if(data[2].equals(connect)){
                    //ChatRoom_Frag.txtChat.removeAll();
                    ChatRoom_Frag.userAdd(data[0]);

                }else if(data[2].equals(firstLogin)){
                    //ChatRoom_Frag.userAdd(data[0]);
                    ChatRoom_Frag.txtChat.append(data[0] + ": " + data[1] + "\n");

                }else if(data[2].equals(disconnect)){
                    ChatRoom_Frag.userRemove(data[0]);

                }else if(data[2].equals(loginError)){

                }else if(data[2].equals(login)){
                    ChatRoom_Frag.txtChat.append(data[0] + ": " + data[1] + "\n");
                    //ChatRoom_Frag.userAdd(data[0]);

                }else if(data[2].equals(done)){
                    ChatRoom_Frag.writeUsers();
                    ChatRoom_Frag.users.clear();
                }

            }

        }catch (Exception ex) {
            System.out.println("Reader error..");
            ex.printStackTrace();
            //ChatRoom_Frag.txtChat.append("Error in IncomingReader..\n");
        }

    }

}
