package com.corhuila.easypark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corhuila.easypark.models.Tarifa;

public interface ITarifaRepository extends JpaRepository<Tarifa,Long>{
    
}
