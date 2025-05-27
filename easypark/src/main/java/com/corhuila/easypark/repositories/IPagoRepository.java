package com.corhuila.easypark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.corhuila.easypark.models.Pago;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByReservaUsuarioId(Long usuarioId);
    List<Pago> findByEstado(Pago.EstadoPago estado);
    Optional<Pago> findByCodigoTransaccion(String codigoTransaccion);

    //metodo para obtener pagos sin reserva
    List<Pago> findByReservaIsNull();

    
}