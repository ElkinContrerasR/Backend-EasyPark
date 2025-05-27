package com.corhuila.easypark.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;


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

    @Column
    @JsonIgnore
    private String document;

     @Enumerated(EnumType.STRING) // Guarda el valor como String en la BD
    @Column(nullable = false)
    private Rol rol = Rol.USER; // Valor por defecto: USER

    @JsonIgnore
    @OneToMany(mappedBy = "creadoPor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarifa> tarifasCreadas = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();

    public User() {
    }

    public User(String nombres, String apellidos, String telefono, String direccion, String email, String password, 
    String document, Rol rol) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.password = password; 
        this.document=document;
        this.rol = rol;
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
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
    
    //GET Y SET DE ENUM
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Métodos para manejar la relación bidireccional (opcionales pero recomendados)
    
    public void addTarifa(Tarifa tarifa) {
        tarifasCreadas.add(tarifa);
        tarifa.setCreadoPor(this);
    }
    
    public void removeTarifa(Tarifa tarifa) {
        tarifasCreadas.remove(tarifa);
        tarifa.setCreadoPor(null);
    }

    // Getter para tarifasCreadas
    public List<Tarifa> getTarifasCreadas() {
        return tarifasCreadas;
    }

    public void setTarifasCreadas(List<Tarifa> tarifasCreadas) {
        this.tarifasCreadas = tarifasCreadas;
    }

    // Nuevos métodos para manejar la relación con Reservas
    public void addReserva(Reserva reserva) {
        reservas.add(reserva);
        reserva.setUsuario(this);
    }
    
    public void removeReserva(Reserva reserva) {
        reservas.remove(reserva);
        reserva.setUsuario(null);
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

}
