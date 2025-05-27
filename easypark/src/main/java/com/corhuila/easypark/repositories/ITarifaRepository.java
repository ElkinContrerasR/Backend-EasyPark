package com.corhuila.easypark.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.corhuila.easypark.models.Tarifa;
import com.corhuila.easypark.models.TipoTarifa;
import com.corhuila.easypark.models.TipoVehiculo;
@Repository
public interface ITarifaRepository extends JpaRepository<Tarifa,Long>{
     Optional<Tarifa> findByTipoVehiculoAndTipoTarifa(TipoVehiculo tipoVehiculo, TipoTarifa tipoTarifa);
}
