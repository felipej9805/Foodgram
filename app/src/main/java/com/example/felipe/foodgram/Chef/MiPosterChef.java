package com.example.felipe.foodgram.Chef;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.felipe.foodgram.R;
import com.example.felipe.foodgram.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MiPosterChef extends Fragment implements View.OnClickListener {


    Button btn_publicarChef;
    Button btn_estado;
    Boolean estadoActivo;
    View vista;
    ImageView img_posterChef;
    TextView tv_correoPosterChef;
    TextView tv_telPosterChef;


    DatabaseReference referencia;

    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;

    public MiPosterChef() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        estadoActivo = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_mi_poster_chef, container, false);
        btn_estado = vista.findViewById(R.id.estado);
        btn_publicarChef = vista.findViewById(R.id.btn_publicarChef);
        btn_estado.setOnClickListener(this);
        btn_publicarChef.setOnClickListener(this);


        img_posterChef = vista.findViewById(R.id.img_posterChef);
        tv_correoPosterChef = vista.findViewById(R.id.tv_correoPosterChef);
        tv_telPosterChef = vista.findViewById(R.id.tv_telPosterChef);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();


        referencia = db.getReference().child("usuarios").child("cocinero").child(auth.getCurrentUser().getUid());

        final StorageReference refer = storage.getReference().child("usuarios")
                .child(auth.getCurrentUser().getUid());


        referencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Usuario cocinero = dataSnapshot.getValue(Usuario.class);
                    //Log.e("valores", usua.getNombre() + " ");
                    tv_telPosterChef.setText("");
                    tv_correoPosterChef.setText(cocinero.getEmail());

                    refer.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(MiPosterChef.this).load(uri).into(img_posterChef);
                            img_posterChef.getAdjustViewBounds();

                        }
                    });
                } else {
                    DatabaseReference segundoIntento = db.getReference().child("usuarios").child("chef").child(auth.getCurrentUser().getUid());
                    segundoIntento.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e(">>>", dataSnapshot.toString());
                            if (dataSnapshot.getValue() != null) {
                                Usuario chef = dataSnapshot.getValue(Usuario.class);
                                tv_telPosterChef.setText("");
                                tv_correoPosterChef.setText(chef.getEmail());

                                refer.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(MiPosterChef.this).load(uri).into(img_posterChef);
                                        img_posterChef.getAdjustViewBounds();

                                    }
                                });

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




        return vista;


    }


    @Override
    public void onClick(View v) {
        //actualizar estado
        if (v.getId() == R.id.estado) {

            if (estadoActivo) {
                btn_estado.setBackground(getResources().getDrawable(R.drawable.ic_estado_off));
                estadoActivo = false;
            } else {
                btn_estado.setBackground(getResources().getDrawable(R.drawable.ic_estado_on));
                estadoActivo = true;
            }

        } else {
            Intent inten = new Intent(v.getContext(), PublicarChef.class);
            startActivity(inten);


        }
    }
}
