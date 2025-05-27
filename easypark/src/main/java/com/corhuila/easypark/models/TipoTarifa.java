package com.corhuila.easypark.models;

public enum TipoTarifa {
    MINUTO("Por minuto"),
    HORA("Por hora"),
    DIA("Por día"),
   
    MES("Por mes");

    private final String descripcion;

    TipoTarifa(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}