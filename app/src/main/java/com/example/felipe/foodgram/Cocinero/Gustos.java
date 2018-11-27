package com.example.felipe.foodgram.Cocinero;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.felipe.foodgram.Chef.InicioChef;
import com.example.felipe.foodgram.R;
import com.example.felipe.foodgram.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Gustos extends AppCompatActivity {
    Button bt_continuar;
    FirebaseDatabase db;
    DatabaseReference referencia;
    DatabaseReference usuario;
    Usuario actual;
    FirebaseAuth auth;

    String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gustos);
        auth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance();
        bt_continuar = findViewById(R.id.bt_continuar);


        referencia = db.getReference().child("usuarios").child("cocinero").child(auth.getCurrentUser().getUid());

        referencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Usuario cocinero = dataSnapshot.getValue(Usuario.class);

                    tipo = cocinero.getTipo().toString();

                } else {
                    DatabaseReference segundoIntento = db.getReference().child("usuarios").child("chef").child(auth.getCurrentUser().getUid());
                    segundoIntento.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e(">>>", dataSnapshot.toString());
                            if (dataSnapshot.getValue() != null) {
                                Usuario chef = dataSnapshot.getValue(Usuario.class);

                                tipo = chef.getTipo().toString();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void GustosContinuar(View view) {
         if (tipo.equals("cocinero")) {
            Intent i = new Intent(this, Inicio.class);
            startActivity(i);
            finish();

        } else if (tipo.equals("chef")) {

            Intent i = new Intent(this, InicioChef.class);
            startActivity(i);
            finish();

        }


    }
}
