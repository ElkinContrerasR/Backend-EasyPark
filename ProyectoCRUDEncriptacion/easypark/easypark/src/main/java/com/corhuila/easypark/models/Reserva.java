package com.corhuila.easypark.models;

import jakarta.persistence.*;


@Entity
@Table(name = "reservas")
public class Reserva {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String fecha;






    
}
