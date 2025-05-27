package com.corhuila.easypark.services;

import java.util.*;


import com.corhuila.easypark.models.Tarifa;

public interface ITarifaService {
    //Mostrar tarifas
    List<Tarifa> getAllTarifas();

    //Guardar Tarifa
    Tarifa saveTarifa(Tarifa tarifa, String emailCreador);

    //Actualizar Tarifa
    Tarifa updateTarifa(Tarifa tarifa, String emailEditor);

    //Eliminar Tarifa
    void deleteTarifa(Long id, String emailEliminador);




}
