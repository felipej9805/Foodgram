package com.example.felipe.foodgram.Chef;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.felipe.foodgram.R;



public class MiPosterChef extends Fragment {


    Button btn_publicarChef;
    Button btn_estado;
    Boolean estadoActivo;
    View vista;


    public MiPosterChef() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        estadoActivo=false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_mi_poster_chef, container, false);
        btn_estado=vista.findViewById(R.id.estado);
        //actualizar estado
        btn_estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(estadoActivo) {
                    btn_estado.setBackground(getResources().getDrawable(R.drawable.ic_estado_off));
                    estadoActivo=false;
                }else{
                    btn_estado.setBackground(getResources().getDrawable(R.drawable.ic_estado_on));
                    estadoActivo=true;
                }
            }
        });

        return vista;


    }



}
