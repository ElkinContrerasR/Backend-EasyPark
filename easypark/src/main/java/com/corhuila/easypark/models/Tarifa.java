package com.corhuila.easypark.models;
import jakarta.persistence.*;
import java.math.BigDecimal;
//import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tarifas")
public class Tarifa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoVehiculo tipoVehiculo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTarifa tipoTarifa;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User creadoPor;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    // Constructores
    public Tarifa() {}
    
    public Tarifa(TipoVehiculo tipoVehiculo, TipoTarifa tipoTarifa, BigDecimal valor, User creadoPor) {
        this.tipoVehiculo = tipoVehiculo;
        this.tipoTarifa = tipoTarifa;
        this.valor = valor;
        /*this.creadoPor = creadoPor;*/
    }
    
    // Getters y Setters (generados automáticamente o manualmente)
    // ... (usar tu IDE para generar estos métodos)
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public TipoTarifa getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(TipoTarifa tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public User getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(User creadoPor) {
        this.creadoPor = creadoPor;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


}