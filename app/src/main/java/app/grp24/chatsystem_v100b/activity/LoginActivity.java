/*
 * Chat System
 * Lavet af Gruppe 24 - DTU 2016
 * --------------------
 * Magnus Andrias Nielsen, s141899
 * --------------------
 */

package app.grp24.chatsystem_v100b.activity;

/**
 * Created by Magnus A. Nielsen on 16-05-2016.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;

import app.grp24.chatsystem_v100b.R;
import app.grp24.chatsystem_v100b.fragment.ChatRoom_Frag;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{

    private TextView title1, title2;
    public static EditText myUsername, myPassword;
    public ProgressBar loadingBar;
    private Button loginBotton;
    public RelativeLayout loginLogo, titleLayout;
    public static String address = "52.24.17.151";
    public static ArrayList<String> users = new ArrayList<>();
    public int port = 1108;
    public Boolean isConnected = false;
    private String firstName = "", lastname ="";

    public Socket sock;
    public static BufferedReader reader;
    public static PrintWriter writer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        title2 = (TextView) findViewById(R.id.title_left);
        title1 = (TextView) findViewById(R.id.title_right);
        myUsername = (EditText) findViewById(R.id.txtUsername);
        myPassword = (EditText) findViewById(R.id.txtPassword);
        loginBotton = (Button) findViewById(R.id.btnLogin);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        loginLogo = (RelativeLayout) findViewById(R.id.loginLogo);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);

        loginBotton.setOnClickListener(this);
        loginBotton.setVisibility(View.VISIBLE);
        loadingBar.setVisibility(View.GONE);

        socketStarer sockStart = new socketStarer();
        sockStart.execute();

    }


    @Override
    public void onClick(View v) {
        if (v == loginBotton) {
            ChatRoom_Frag.username = myUsername.getText();
            ChatRoom_Frag.password = myPassword.getText();

            login datLogin = new login();
            datLogin.execute();

            loginBotton.setVisibility(View.GONE);
            loadingBar.setVisibility(View.VISIBLE);

            YoYo.with(Techniques.FadeOut).duration(3000).withListener(new SplashEndAnimatorListener(this)).playOn(titleLayout);

        }
    }

    public void error(int number){

        String errorTitle ="", errorMessage ="", errorPos ="", errorNeg ="";

        switch (number){
            case 1:
                errorTitle ="Try again!";
                errorMessage ="Wrong Username or Password!";
                errorPos ="Okay";
                break;
            case 2:
                errorTitle ="Oops!";
                errorMessage ="Our server is down.. Try again later..";
                errorPos ="Okay";
                break;
            default:
                errorTitle ="Oops!";
                errorMessage ="Something went wrong! Try again.";
                errorPos ="Okay";
                break;
        }

        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(errorTitle)
                .setMessage(errorMessage)
                .setPositiveButton(errorPos, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(errorNeg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        loginBotton.setVisibility(View.VISIBLE);
        loadingBar.setVisibility(View.GONE);
    }

    private class login extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try{
                //sock = new Socket(address, port);
                //InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                //reader = new BufferedReader(streamreader);
                //writer = new PrintWriter(sock.getOutputStream());

                try{
                    writer.println(ChatRoom_Frag.username + ":" + ChatRoom_Frag.password + ":Login");
                    writer.flush();
                }catch (Exception ex){
                    System.out.println("Flush error..");
                    error(3);
                }

            }catch(Exception ex){
                System.out.println("Socket error..");
                ex.printStackTrace();
                error(2);
            } return null;
        }
        protected void onPostExecute(Void result){

        }
    }

    private class socketStarer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try{
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                writer = new PrintWriter(sock.getOutputStream());

            }catch(Exception ex){
                System.out.println("Socket error..");
                ex.printStackTrace();
                error(2);
            } return null;
        }
        protected void onPostExecute(Void result){

        }
    }

    private class SplashEndAnimatorListener implements Animator.AnimatorListener {

        private final WeakReference<LoginActivity> loginActivityWeakReference;

        public SplashEndAnimatorListener(final LoginActivity logAct) {
            loginActivityWeakReference = new WeakReference<>(logAct);
        }
        @Override
        public void onAnimationStart(final Animator animation) { }

        @Override
        public void onAnimationEnd(final Animator animation) {
            final LoginActivity logAct = loginActivityWeakReference.get();
            if (logAct != null) {

                //Next class to be shown is the MainActivity.class

                final Intent intent = new Intent(logAct, MainActivity.class);

                logAct.startActivity(intent);
                logAct.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                logAct.finish();
            }
        }

        @Override
        public void onAnimationCancel(final Animator animation) { }

        @Override
        public void onAnimationRepeat(final Animator animation) { }
    }
}
