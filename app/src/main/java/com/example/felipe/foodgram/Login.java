package com.example.felipe.foodgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.MessageDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Login extends AppCompatActivity {

    EditText et_email;
    EditText et_contraseña;

    Button bt_iniciarSesion;
    Button bt_sinRegistro;

    TextView tv_registrate;

    FirebaseDatabase db;
    FirebaseAuth auth;

    CallbackManager callbackManager;
    LoginButton login_facebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicializa todos los elementos
        inicializarElementos();

        auth = FirebaseAuth.getInstance();


        //Login Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //Callback
        callbackManager = CallbackManager.Factory.create();

        //Permisos para obteneer la información de facebook
        login_facebook.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday", "user_friends"));

        login_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Intent intent = new Intent(Login.this, Gustos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login Facebook Cancelado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Ocurrio un error al ingresar", Toast.LENGTH_SHORT).show();

            }
        });


        //Este metodo se encarga de cuando se haga click en registrarse me lleve a la actividad Registro
        tv_registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Registro.class);
                startActivity(intent);
                finish();
            }
        });


        bt_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().isEmpty() || et_contraseña.getText().toString().isEmpty()) {

                    Toast.makeText(Login.this, "Ingresa los campos requeridos", Toast.LENGTH_SHORT).show();

                } else {
                    loginUsuario(et_email.getText().toString(), et_contraseña.getText().toString());
                }


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Metodo que se encarga del login del Usuario
    public void loginUsuario(String email, String password) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(Login.this, Gustos.class);
                    startActivity(i);
                    finish();

                    Toast.makeText(Login.this, "Login correcto", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(Login.this, "Error en el correo y/o contraseña", Toast.LENGTH_SHORT).show();
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

        login_facebook = (LoginButton) findViewById(R.id.login_facebook);

    }
}
