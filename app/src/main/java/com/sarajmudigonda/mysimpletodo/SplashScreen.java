package com.sarajmudigonda.mysimpletodo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        // Splash screen timer
        int SPLASH_TIME_OUT = 1500;

        new Handler().postDelayed(new Runnable() {


            //Showing splash screen with a timer.

            @Override
            public void run() {
                // This method will be executed once the timer is over and start app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
