package com.example.felipe.foodgram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    EditText et_email;
    EditText et_contraseña;

    Button bt_iniciarSesion;
    Button bt_sinRegistro;

    TextView tv_registrate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicializa todos los elementos
        inicializarElementos();

        //Este metodo se encarga de cuando se haga click en registrarse me lleve a la actividad Registro
        tv_registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Registro.class);
                startActivity(intent);
                finish();
            }
        });

    }


    // Este metodo se encarga de inicializar todos los elementos
    public void inicializarElementos() {
        et_email = findViewById(R.id.et_email);
        et_contraseña = findViewById(R.id.et_contraseña);
        bt_iniciarSesion = findViewById(R.id.bt_iniciarSesion);
        bt_sinRegistro = findViewById(R.id.bt_sinRegistro);
        tv_registrate = findViewById(R.id.tv_registrate);
    }
}
