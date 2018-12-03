package com.example.felipe.foodgram.modelo;

public class Usuario {

    private String uid;
    private String nombre;
    private String email;
    private String telefono;
    // Define si el usuario es Cocinero o Chef
    private String tipo;


    public Usuario() {

    }

    //Metodo Constructor
    public Usuario(String uid, String tipo ,String nombre, String email, String telefono) {
        this.uid = uid;
        this.tipo = tipo;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }


    //METODOS DAR Y MODIFICAR
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telef) {
        this.telefono = telef;
    }
}
