package com.example.felipe.foodgram.Chef;

import android.widget.ImageView;

public class Publicacion {

    String uid;
    Receta receta;

    public Publicacion() {
    }

    public Publicacion(String uid, Receta receta) {
        this.uid = uid;
        this.receta = receta;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }
}
