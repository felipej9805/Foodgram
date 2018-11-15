package com.example.felipe.foodgram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {


    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();

        Thread h = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    irALogin();

/**
 if (auth.getCurrentUser() != null) {

 Intent i = new Intent(Splash.this, Inicio.class);
 startActivity(i);
 finish();
 } else {

 irALogin();
 }
 */
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
