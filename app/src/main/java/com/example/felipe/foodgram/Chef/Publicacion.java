package com.example.felipe.foodgram.Chef;

import android.widget.ImageView;

public class Publicacion {

    String path;
    String nombre;
    String categoria;
    String preparacion;
    String ingredientes;

    public Publicacion(){

    }

    public Publicacion(String path, String nombre, String categoria, String preparacion, String ingredientes) {
        this.path = path;
        this.nombre = nombre;
        this.categoria = categoria;
        this.preparacion = preparacion;
        this.ingredientes = ingredientes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(String preparacion) {
        this.preparacion = preparacion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }
}
