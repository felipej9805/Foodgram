package com.example.felipe.foodgram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Gustos extends AppCompatActivity {
    Button bt_continuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gustos);

        bt_continuar = findViewById(R.id.bt_continuar);
    }


    public void GustosContinuar(View view) {
        Intent i = new Intent(this, Inicio.class);
        startActivity(i);
        finish();
    }
}
