package com.corhuila.easypark.services;

import java.util.*;
import com.corhuila.easypark.models.Reserva;

public interface IReservaService {
    List<Reserva> getAll();
    Reserva saveReserva(Reserva reserva);
    Reserva updateReserva(Reserva reserva, Long usuarioId);
    void deleteReserva(Long id, Long usuarioId);
    List<Reserva> getReservasByUsuarioId(Long usuarioId);
}
