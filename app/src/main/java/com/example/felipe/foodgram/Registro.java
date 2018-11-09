package com.example.felipe.foodgram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.felipe.foodgram.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {

    EditText et_regisNombre;
    EditText et_regisCorreo;
    EditText et_regisContrasena;
    EditText et_regisConfirContrasena;
    CheckBox cb_terminos;

    Button bt_registrarme;
    Button bt_cancelar;


    FirebaseDatabase db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

//Metodo que se encarga de inicializar los elementos
        inicializarElementos();

        bt_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        bt_registrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


    }


    //Metodo que se encarga de inicializar los elementos del activity
    public void inicializarElementos() {

        et_regisNombre = findViewById(R.id.et_regisNombre);
        et_regisCorreo = findViewById(R.id.et_regisCorreo);
        et_regisContrasena = findViewById(R.id.et_regisContrasena);
        et_regisConfirContrasena = findViewById(R.id.et_regisConfirContrasena);

        cb_terminos = findViewById(R.id.cb_terminos);

        bt_registrarme = findViewById(R.id.bt_registrarme);

        bt_cancelar = findViewById(R.id.bt_cancelar);

    }
}
