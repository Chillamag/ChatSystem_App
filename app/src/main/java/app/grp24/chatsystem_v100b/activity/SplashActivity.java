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

//import android.view.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import java.lang.ref.WeakReference;

import app.grp24.chatsystem_v100b.R;

public class SplashActivity extends AppCompatActivity {

    private Animation animation;
    private RelativeLayout logo;
    private TextView title1, title2;
    private Handler loadHandler;
    private static final int MSG_LOAD_COMPLETE = 1;

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadHandler = new LoadHandler();

        loadHandler.post(new LoadConfig());

        logo = (RelativeLayout) findViewById(R.id.splash_center_circle);
        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.step_number_fader));
        title2 = (TextView) findViewById(R.id.splash_text_left);
        title1 = (TextView) findViewById(R.id.splash_text_right);

        if (savedInstanceState == null) flyIn();
    }

    private void flyIn() {
//        animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
//        logo.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.app_name_animation);
        title1.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.pro_animation);
        title2.startAnimation(animation);
    }

    private void endSplash() {
        logo.setAnimation(null);
        // just use YoYo for the nice rotate out :-)
        YoYo.with(Techniques.FlipOutY).duration(400).withListener(new SplashEndAnimatorListener(this)).playOn(logo);

//        animation = AnimationUtils.loadAnimation(this, R.anim.step_number_back);
//        logo.startAnimation(animation);

//        animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation_back);
//        logo.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.app_name_animation_back);
        title1.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.pro_animation_back);
        title2.startAnimation(animation);

//        animation.setAnimationListener(new SplashEndAnimationListener(this));
    }

    private class LoadHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (msg != null && msg.what == MSG_LOAD_COMPLETE) {
                loadHandler.postDelayed(new EndSplash(), 5000);
            }
        }
    }

    private class LoadConfig implements Runnable {

        @Override
        public void run() {

            final Message message = loadHandler.obtainMessage(MSG_LOAD_COMPLETE);
            message.sendToTarget();

        }
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setScreenDimension(this);
            //WindowLayout.setImmersiveMode(getWindow());

        }
    }

    private static class SplashEndAnimatorListener implements Animator.AnimatorListener {

        private final WeakReference<SplashActivity> splashActivityWeakReference;

        public SplashEndAnimatorListener(final SplashActivity splashActivity) {
            splashActivityWeakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void onAnimationStart(final Animator animation) { }

        @Override
        public void onAnimationEnd(final Animator animation) {
            final SplashActivity splashActivity = splashActivityWeakReference.get();
            if (splashActivity != null) {

                //Next class to be shown is the MainActivity.class

                final Intent intent = new Intent(splashActivity, LoginActivity.class);

                splashActivity.startActivity(intent);
                splashActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                splashActivity.finish();
            }
        }

        @Override
        public void onAnimationCancel(final Animator animation) { }

        @Override
        public void onAnimationRepeat(final Animator animation) { }


    }

    private class EndSplash implements Runnable {
        @Override
        public void run() { endSplash(); }
    }

    private static void setScreenDimension(final AppCompatActivity appCompatActivity) {
        final Display display = appCompatActivity.getWindowManager().getDefaultDisplay();
        //display.getSize(WindowLayout.screenDimension);
    }
}
