package com.example.felipe.foodgram;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    EditText et_email;
    EditText et_contraseña;

    Button bt_iniciarSesion;
    Button bt_sinRegistro;

    TextView tv_registrate;

    FirebaseDatabase db;
    FirebaseAuth auth;


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

        auth = FirebaseAuth.getInstance();

        /**
         if (auth.getCurrentUser() != null) {
         Intent i = new Intent(this, Inicio.class);
         startActivity(i);
         finish();
         return;
         }
         */
        bt_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUsuario(et_email.getText().toString(), et_contraseña.getText().toString());
            }
        });


    }

    public void loginUsuario(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(Login.this, Inicio.class);
                    startActivity(i);
                    finish();

                    Toast.makeText(Login.this, "Login correcto", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                }
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
