package com.corhuila.easypark.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "users")
@Entity

public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (nullable = false)
    private Integer id;

    @Column (nullable = false)
    private String nombres;

    @Column (nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String direccion;

    @Column (unique = true, length = 100, nullable = false )
    private String email;

    @Column(nullable = false) 
    private String password;

    public User() {
    }

    public User(String nombres, String apellidos, String telefono, String direccion, String email, String password) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.password = password; 
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }



    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
