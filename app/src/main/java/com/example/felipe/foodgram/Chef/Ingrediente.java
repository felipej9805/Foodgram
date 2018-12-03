package com.example.felipe.foodgram.Chef;

public class Ingrediente {

    String uid;
    String nombre;

    public Ingrediente() {
    }

    public Ingrediente(String uid, String nombre) {
        this.uid = uid;
        this.nombre = nombre;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
