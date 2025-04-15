package com.corhuila.easypark.models;

public enum TipoVehiculo {
    AUTOL("Auto Liviano"),
    AUTOP("Auto pesado"),
    MOTO("Motocicleta"),
    BICICLETA("Bicicleta");

    private final String nombre;

    TipoVehiculo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}
