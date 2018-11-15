package com.example.felipe.foodgram;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.felipe.foodgram.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {

    EditText et_regisNombre;
    EditText et_regisCorreo;
    EditText et_regisContrasena;
    EditText et_regisConfirContrasena;
    CheckBox cb_terminos;

    RadioGroup rgRegisTipo;
    RadioButton rb_soyCocinero;
    RadioButton rb_soyChef;

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

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        bt_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        //Boton que registra al usuario y guarda la información segun si es chef o cocinero
        bt_registrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comprobarDatos();

                final String nombre = et_regisNombre.getText().toString();
                final String correo = et_regisCorreo.getText().toString();
                final String contrasena = et_regisContrasena.getText().toString();
                final String confirContra = et_regisConfirContrasena.getText().toString();
                boolean chef = rb_soyChef.isChecked();
                boolean cocinero = rb_soyCocinero.isChecked();
                final boolean terminos = cb_terminos.isChecked();
                
                if (chef) {
                    Usuario usuario = new Usuario("", "chef", nombre, correo);
                    registrarUsuario(usuario);
                } else if (cocinero) {
                    Usuario usuario = new Usuario("", "cocinero", nombre, correo);
                    registrarUsuario(usuario);
                }


            }
        });


    }

    //Este metodo se encarga de registrar a un usuario a Firebase
    public void registrarUsuario(final Usuario usuario) {


        auth.createUserWithEmailAndPassword(usuario.getEmail(), et_regisContrasena.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    usuario.setUid(auth.getCurrentUser().getUid());

                    if (rb_soyCocinero.isChecked()) {
                        DatabaseReference reference = db.getReference().child("usuarios").child("cocinero").child(usuario.getUid());
                        reference.setValue(usuario);
                    } else if (rb_soyChef.isChecked()) {
                        DatabaseReference reference = db.getReference().child("usuarios").child("chef").child(usuario.getUid());
                        reference.setValue(usuario);
                    }

                    Intent i = new Intent(Registro.this, Inicio.class);
                    startActivity(i);
                    finish();

                } else {

                 //   Toast.makeText(Registro.this, "" + task.getException(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    //Metodo que comprueba que los datos esten bien ingresados
    public void comprobarDatos() {
        String nombre = et_regisNombre.getText().toString();
        String correo = et_regisCorreo.getText().toString();
        String contrasena = et_regisContrasena.getText().toString();
        String confirContra = et_regisConfirContrasena.getText().toString();
        int rgTipo = rgRegisTipo.getCheckedRadioButtonId();
        boolean chef = rb_soyChef.isChecked();
        boolean cocinero = rb_soyCocinero.isChecked();

        boolean terminos = cb_terminos.isChecked();

        if (nombre != null && !nombre.isEmpty() && correo != null && !correo.isEmpty() && contrasena != null && !contrasena.isEmpty()
                && confirContra != null && !confirContra.isEmpty()&& terminos == true
                && rgTipo != -1) {

            if (contrasena.length() > 5) {

                if (contrasena.equals(confirContra)) {

                    if (terminos == true) {
                        Toast.makeText(getApplicationContext(), "TODO CORRECTO", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Debe aceptar terminos y condiciones", Toast.LENGTH_SHORT).show();
                        Log.e("terminos", "Debe aceptar terminos y condiciones");
                        Log.e("terminos",
                                nombre + "-" +
                                        correo + "-" +
                                        contrasena + "-" +
                                        confirContra + "-" +
                                        rgTipo + "-" +
                                        terminos
                        );
                        return;
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    Log.e("iguales", "Las contraseñas no coinciden");
                    Log.e("iguales",
                            nombre + "-" +
                                    correo + "-" +
                                    contrasena + "-" +
                                    confirContra + "-" +
                                    rgTipo + "-" +
                                    terminos
                    );
                    return;
                }
            } else {
                Toast.makeText(getApplicationContext(), "La contraseña debe ser de longitud 6 o mayor", Toast.LENGTH_SHORT).show();
                Log.e(">6", "La contraseña debe ser de longitud 6 o mayor");


                Log.e(">6",
                        nombre + "-" +
                                correo + "-" +
                                contrasena + "-" +
                                confirContra + "-" +
                                rgTipo + "-" +
                                terminos
                );

                return;
            }


        } else {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese todos los datos", Toast.LENGTH_LONG).show();
            Log.e("datos", "Por favor, ingrese todos los datos");
            Log.e("datos",
                    nombre + "-" +
                            correo + "-" +
                            contrasena + "-" +
                            confirContra + "-" +
                            rgTipo + "-" +
                            terminos
            );

            return;

        }


    }


    //Metodo que se encarga de inicializar los elementos del activity
    public void inicializarElementos() {

        et_regisNombre = (EditText) findViewById(R.id.et_regisNombre);
        et_regisCorreo = (EditText) findViewById(R.id.et_regisCorreo);
        et_regisContrasena = (EditText) findViewById(R.id.et_regisContrasena);
        et_regisConfirContrasena = (EditText) findViewById(R.id.et_regisConfirContrasena);

        cb_terminos = (CheckBox) findViewById(R.id.cb_terminos);

        rgRegisTipo = (RadioGroup) findViewById(R.id.rgRegisTipo);
        rb_soyCocinero = (RadioButton) findViewById(R.id.rb_soyCocinero);
        rb_soyChef = (RadioButton) findViewById(R.id.rb_soyChef);


        bt_registrarme = (Button) findViewById(R.id.bt_registrarme);
        bt_cancelar = (Button) findViewById(R.id.bt_cancelar);

    }


}
