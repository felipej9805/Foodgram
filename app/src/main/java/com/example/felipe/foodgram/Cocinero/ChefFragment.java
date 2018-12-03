package com.example.felipe.foodgram.Cocinero;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.felipe.foodgram.R;
import com.example.felipe.foodgram.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;


public class ChefFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;

    ListView listaChefs;
    Adaptador adaptador;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista= inflater.inflate(R.layout.fragment_chef, container, false);
        listaChefs=vista.findViewById(R.id.list_chefs);
        adaptador= new Adaptador(vista.getContext());
        listaChefs.setAdapter(adaptador);


        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        //storage = FirebaseStorage.getInstance();

       // final StorageReference refer= storage.getReference().child("usuarios");
        DatabaseReference listchef=db.getReference().child("usuarios").child("chef");

        listchef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Usuario chef = dataSnapshot.getValue(Usuario.class);
                        adaptador.agregarChef(chef);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return vista;
    }
}
