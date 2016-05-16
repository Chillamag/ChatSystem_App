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

        login datLogin = new login();
        datLogin.execute();
        //ListenThread();

        return root;
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
                    writer.println(username + ":" + password + ":Login");
                    writer.flush();
                } catch (Exception ex){
                    System.out.println("Flush error..");
                    //error(3);
                }

            }catch(Exception ex){
                System.out.println("Socket error..");
                ex.printStackTrace();
                //error(2);
            } return null;
        }
        protected void onPostExecute(Void result){

        }
    }

    public static void userAdd(String data){
        users.add(data);
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
}
