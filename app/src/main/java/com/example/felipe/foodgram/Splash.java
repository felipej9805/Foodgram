package com.example.felipe.foodgram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread h = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    irALogin();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        h.start();
    }

    private void irALogin() {

        Intent i = new Intent(Splash.this, Login.class);
        startActivity(i);
        finish();
    }
}
