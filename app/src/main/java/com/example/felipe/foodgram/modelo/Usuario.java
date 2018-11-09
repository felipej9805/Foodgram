package com.example.felipe.foodgram.modelo;

public class Usuario {

    private String uid;
    private String nombre;
    private String email;


    public Usuario() {

    }

    //Metodo Constructor
    public Usuario(String uid, String nombre, String email) {
        this.uid = uid;
        this.nombre = nombre;
        this.email = email;
    }


    //METODOS DAR Y MODIFICAR
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
