/*
 * Chat System
 * Lavet af Gruppe 24 - DTU 2016
 * --------------------
 * Magnus Andrias Nielsen, s141899
 * --------------------
 */

package app.grp24.chatsystem_v100b.fragment;

/**
 * Created by Magnus A. Nielsen on 16-05-2016.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import app.grp24.chatsystem_v100b.R;
import app.grp24.chatsystem_v100b.java.IncomingReader;


public class ChatRoom_Frag extends Fragment implements View.OnClickListener{

    public static TextView txtChat;
    public static EditText txtMessage;
    public Button btnSend;

    public static Editable username, password;
    public static String address = "52.24.17.151";
    public static ArrayList<String> users = new ArrayList<>();
    public int port = 1108;
    public Socket sock;
    public static BufferedReader reader;
    public static PrintWriter writer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_chat_room, container, false);

        txtChat = (TextView) root.findViewById(R.id.txtChat);
        txtMessage = (EditText) root.findViewById(R.id.txtMessage);
        btnSend = (Button) root.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        //login datLogin = new login();
        //datLogin.execute();

        incomingReader incRead = new incomingReader();
        incRead.execute();

        //IncomingReader();
        return root;
    }


    //Kan ikke access Main UI fra en anden Thread..
    private void ListenThread() {
        Thread IncomingReader = new Thread(new IncomingReader());
        IncomingReader.start();
    }


    private class login extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try{
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                writer = new PrintWriter(sock.getOutputStream());

                try{
                    //writer.println(username + ":" + password + ":firstLogin");
                    //writer.flush();
                } catch (Exception ex){
                    System.out.println("Flush error..");
                    error(0);
                }

            }catch(Exception ex){
                System.out.println("Socket error..");
                ex.printStackTrace();
                error(1);

            } return null;

        }
        protected void onPostExecute(Void result){
            //ListenThread();
        }
    }

    private class incomingReader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect",
                    chat = "Chat", login = "Login", firstLogin = "firstLogin", loginError = "LoginError";

            try{
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                writer = new PrintWriter(sock.getOutputStream());

                try{



                } catch (Exception ex){
                    System.out.println("Flush error..");
                    error(0);
                }

            }catch(Exception ex){
                System.out.println("Socket error..");
                ex.printStackTrace();
                error(1);

            } return null;

        }
        protected void onPostExecute(Void result){
            IncomingReader();
        }
    }

    public void error(int number){
        String errorCode = "";
        switch (number){
            case 1:
                errorCode = "Cannot connect! Try again.";
                break;
            case 2:
                errorCode = "Server down! Try again later..";
                break;
            default:
                errorCode = "Unexpected error..";
                break;
        }
        txtChat.append(errorCode + "\n");
    }

    public static void userAdd(String data){
        users.add(data);
    }

    public static void userRemove(String data){
        txtChat.append(data + " is now offline. \n");
    }

    public static void writeUsers(){
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);
        for (String token:tempList){
            //users.append(token + "\n");
        }
    }

    public void sendDisconnect(){
        String bye = (username + " (Mobile)" + ": :Disconnect");
        try{
            writer.println(bye);
            writer.flush();
        }catch (Exception ex){
            txtChat.append("Could not send disconnect message. \n");
        }
    }

    public void Disconnect(){
        try{
            txtChat.append("Disconnected. \n");
            sock.close();
        }catch (Exception ex){
            txtChat.append("Failed to disconnect. \n");
        }
        System.exit(0);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSend) {
            String nothing ="";
            if((txtMessage.getText()).equals(nothing)){
                txtMessage.setText("");
                txtMessage.requestFocus();
            }else{
                try{
                    writer.println(username + " (Mobile)" + ":" + txtMessage.getText() + ":" + "Chat");
                    writer.flush();
                }catch (Exception ex){
                    txtChat.append("Message was not sent. \n");
                }
                txtMessage.setText("");
                txtMessage.requestFocus();
            }
            txtMessage.setText("");
            txtMessage.requestFocus();

        }
    }

    public void IncomingReader(){
        String[] data;
        String stream, done = "Done", connect = "Connect", disconnect = "Disconnect",
                chat = "Chat", login = "Login", firstLogin = "firstLogin", loginError = "LoginError";

        try{


            //reader laver en NullPointer Exception
            //Får også en NetworkOnMainThreadException
            while((stream = reader.readLine()) != null){
                data = stream.split(":");

                if(data[2].equals(chat)){
                    txtChat.append(data[0] + ": " + data[1] + "\n");

                }else if(data[2].equals(connect)){
                    //ChatRoom_Frag.txtChat.removeAll();
                    userAdd(data[0]);

                }else if(data[2].equals(firstLogin)){
                    userAdd(data[0]);
                    //ChatRoom_Frag.txtChat.append(data[0] + ": " + data[1] + "\n");

                }else if(data[2].equals(disconnect)){
                    userRemove(data[0]);

                }else if(data[2].equals(loginError)){

                }else if(data[2].equals(login)){
                    txtChat.append(data[0] + ": " + data[1] + "\n");
                    //ChatRoom_Frag.userAdd(data[0]);

                }else if(data[2].equals(done)){
                    writeUsers();
                    users.clear();
                }

            }

        }catch(Exception ex){
            System.out.println("Reader error..");
            ex.printStackTrace();
            txtChat.append("Error in IncomingReader..\n");
        }
    }

}
