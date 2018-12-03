package com.example.felipe.foodgram.Cocinero;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.felipe.foodgram.R;
import com.example.felipe.foodgram.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

/**
 * Created by Domiciano on 01/05/2018.
 */

/*
    PARA USAR ESTE ADAPTADOR DEBE TENER EL ARCHIVO renglon.xml EN LA CARPETA LAYOUT
    DEBE TAMBIÉN USAR 
*/
public class Adaptador extends BaseAdapter {
    ArrayList<Usuario> usuariosChef;
    FirebaseStorage storage;
    Context context;


    public Adaptador(Context context) {
        this.context = context;
        usuariosChef = new ArrayList<>();
        storage = FirebaseStorage.getInstance();


    }

    @Override
    public int getCount() {
        return usuariosChef.size();
    }

    @Override
    public Object getItem(int position) {
        return usuariosChef.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //se ejecuta tantas veces como elementos existan en la lista
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //aprovechamos el cache del ListView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.renglon, null);
        }

        final TextView tv_nombreChef = convertView.findViewById(R.id.tv_renglon_nombreChef);
        final TextView tv_telefono = convertView.findViewById(R.id.tv_renglon_telefono);
        final TextView tv_correo = convertView.findViewById(R.id.tv_renglon_correo);
        final TextView tv_disponibilidad = convertView.findViewById(R.id.tv_renglon_disponibilidadChef);
        final ImageView renglon_img = convertView.findViewById(R.id.renglon_img);

        //tv_disponibilidad.setText();

        //StorageReference ref = storage.getReference().child("usuarios");
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child("chef");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    myRef.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Log.e("Arreglo",""+usuariosChef.size());
                            String nombre=usuariosChef.get(position).getNombre();
                            String email=usuariosChef.get(position).getEmail();
                            String telef=usuariosChef.get(position).getTelefono();
                            //Usuario guardado=snapshot.getValue(Usuario.class);

                            Log.e("Datos",""+ nombre+ ""+email);

                            tv_nombreChef.setText(nombre);
                            tv_correo.setText(email);
                            tv_telefono.setText(telef);

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

        /**ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(renglon_img);

            }
        });**/
        return convertView;
    }


    public void agregarChef(Usuario c) {

        usuariosChef.add(c);
        notifyDataSetChanged();

    }

    /** public void refreshComment(Usuario c) {

     int index = usuariosChef.indexOf(c);
     Usuario viejo= usuariosChef.get(index);
     notifyDataSetChanged();
     }**/
}
