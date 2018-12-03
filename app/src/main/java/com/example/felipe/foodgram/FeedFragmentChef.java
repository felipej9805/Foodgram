package com.example.felipe.foodgram;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.felipe.foodgram.Chef.AdaptadorChef;
import com.example.felipe.foodgram.Chef.Publicacion;
import com.example.felipe.foodgram.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;


public class FeedFragmentChef extends Fragment {

    private AdaptadorChef adaptadorChef;
    ListView lista_publicaciones;
    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_feed_chef, container, false);

        lista_publicaciones = vista.findViewById(R.id.publicaciones_chef);
        adaptadorChef = new AdaptadorChef(vista.getContext());
        lista_publicaciones.setAdapter(adaptadorChef);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();



        /**
         publicaciones_ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for(final DataSnapshot snapshot : dataSnapshot.getChildren())
        if (dataSnapshot.getValue() != null) {
        publicaciones_ref.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
        @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Publicacion p = snapshot.getValue(Publicacion.class);
        adaptadorChef.agregarPublicacion(p);
        }

        @Override public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });
        }

        }
        @Override public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });

         */
        final DatabaseReference publicaciones_ref = db.getReference().child("publicaciones");

        publicaciones_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Publicacion p = dataSnapshot.getValue(Publicacion.class); //deserializar
                adaptadorChef.agregarPublicacion(p);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Publicacion p = dataSnapshot.getValue(Publicacion.class);
                adaptadorChef.refreshPublicacion(p);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return vista;
    }


}