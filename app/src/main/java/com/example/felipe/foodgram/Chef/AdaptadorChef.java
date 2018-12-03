package com.example.felipe.foodgram.Chef;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.felipe.foodgram.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdaptadorChef extends BaseAdapter {

    ArrayList<Publicacion> publicaciones;
    Activity activity;
    FirebaseStorage storage;
    Context context;

    public AdaptadorChef(Context context) {
        this.context = context;
        publicaciones = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater = LayoutInflater.from(activity);
        //View renglon = inflater.inflate(R.layout.renglon_publicacion_chef, null, false);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //aprovechamos el cache del ListView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.renglon_publicacion_chef, null);
        }
        final ImageView img_re = convertView.findViewById(R.id.img_re);
        TextView titulo_receta = convertView.findViewById(R.id.titulo_receta);
        TextView categoria_receta = convertView.findViewById(R.id.categoria_receta);
        TextView preparacion_receta = convertView.findViewById(R.id.preparacion_receta);
        TextView ingrediente_receta = convertView.findViewById(R.id.ingrediente_receta);
        TextView usuario_receta = convertView.findViewById(R.id.usuario_receta);

        String usuario = publicaciones.get(position).getReceta().getChef().getUid();
        String id = publicaciones.get(position).getUid();

        StorageReference ref = storage.getReference().child("recetas").child(usuario).child(id);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(img_re);
            }
        });

        titulo_receta.setText(publicaciones.get(position).getReceta().getNombreReceta());
        categoria_receta.setText(publicaciones.get(position).getReceta().getCategoriaReceta());
        preparacion_receta.setText(publicaciones.get(position).getReceta().getPreparacionReceta());
        usuario_receta.setText(publicaciones.get(position).getReceta().getChef().getNombre());
        ArrayList<Ingrediente> lista = publicaciones.get(position).getReceta().getListaIngredientesReceta();


        String ingredientes = "";
        for (int i = 0; i < lista.size(); i++) {
            ingredientes += lista.get(i).getNombre() + " ";
        }
        ingrediente_receta.setText(ingredientes);

        return convertView;
    }

    public void agregarPublicacion(Publicacion p) {
        publicaciones.add(p);
        notifyDataSetChanged();

    }

    public void clear() {

        publicaciones.clear();
        notifyDataSetChanged();
    }

    public void refreshPublicacion(Publicacion p) {
        int index = publicaciones.indexOf(p);
        Publicacion viejo = publicaciones.get(index);
        viejo.setReceta(p.getReceta());
        notifyDataSetChanged();
    }
}