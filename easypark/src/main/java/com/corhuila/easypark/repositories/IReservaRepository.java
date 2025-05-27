package com.corhuila.easypark.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.corhuila.easypark.models.Reserva;

@Repository
public interface IReservaRepository extends JpaRepository<Reserva,Long>{
    List<Reserva> findByUsuarioId(Long usuarioId);
}
