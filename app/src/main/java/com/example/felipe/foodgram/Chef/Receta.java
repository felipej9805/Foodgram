package com.example.felipe.foodgram.Chef;

import android.widget.ImageView;

import com.example.felipe.foodgram.modelo.Usuario;

import java.util.ArrayList;

public class Receta {
    //Usuario que va a ser quien creo la receta
    private Usuario chef;

    // Identificador que nos permite diferenciar las recetas de las demas en Firebase
    private String uid;

    private String nombreReceta;

    private String imagenReceta;

    //La categoria es si la receta es postre , postre, ensalada, sopa, etc
    private String categoriaReceta;

    // Como se prepara la receta
    private String preparacionReceta;

    //Lista de Ingredientes con los objetos Ingrediente
    private ArrayList<Ingrediente> ListaIngredientesReceta;

    //Constructor
    public Receta() {
    }

    //Constructor
    public Receta(Usuario chef, String uid, String nombreReceta, String imagenReceta,
                  String categoriaReceta, String preparacionReceta, ArrayList<Ingrediente> listaIngredientesReceta) {
        this.chef = chef;
        this.uid = uid;
        this.nombreReceta = nombreReceta;
        this.imagenReceta = imagenReceta;
        this.categoriaReceta = categoriaReceta;
        this.preparacionReceta = preparacionReceta;
        ListaIngredientesReceta = listaIngredientesReceta;
    }

    //GET AND SET
    public Usuario getChef() {
        return chef;
    }

    public void setChef(Usuario chef) {
        this.chef = chef;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public String getImagenReceta() {
        return imagenReceta;
    }

    public void setImagenReceta(String imagenReceta) {
        this.imagenReceta = imagenReceta;
    }

    public String getCategoriaReceta() {
        return categoriaReceta;
    }

    public void setCategoriaReceta(String categoriaReceta) {
        this.categoriaReceta = categoriaReceta;
    }

    public String getPreparacionReceta() {
        return preparacionReceta;
    }

    public void setPreparacionReceta(String preparacionReceta) {
        this.preparacionReceta = preparacionReceta;
    }

    public ArrayList<Ingrediente> getListaIngredientesReceta() {
        return ListaIngredientesReceta;
    }

    public void setListaIngredientesReceta(ArrayList<Ingrediente> listaIngredientesReceta) {
        ListaIngredientesReceta = listaIngredientesReceta;
    }
}
