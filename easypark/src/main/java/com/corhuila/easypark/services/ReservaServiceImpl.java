package com.corhuila.easypark.services;


import com.corhuila.easypark.models.Pago;
import com.corhuila.easypark.models.Reserva;
import com.corhuila.easypark.models.Tarifa;

import com.corhuila.easypark.repositories.ITarifaRepository;
import com.corhuila.easypark.repositories.IPagoRepository;
import com.corhuila.easypark.repositories.IReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaServiceImpl implements IReservaService {

    @Autowired
    private IReservaRepository reservaRepository;
    
    @Autowired
    private ITarifaRepository tarifaRepository;

    @Autowired
    private IPagoRepository pagoRepository;

     

    @Override
    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }

    @Override
    @Transactional
    public Reserva saveReserva(Reserva reserva) {
        // Validaciones básicas
        if (reserva.getUsuario() == null || reserva.getUsuario().getId() == null) {
            throw new IllegalArgumentException("La reserva debe estar asociada a un usuario válido");
        }
        
        if (reserva.getTipoTarifa() == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de tarifa");
        }
        
        if (reserva.getTipoVehiculo() == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de vehículo");
        }
        
        if (reserva.getFechaHoraInicio() == null) {
            throw new IllegalArgumentException("Debe especificar fecha y hora de inicio");
        }
        
        // Validaciones específicas por tipo de tarifa
        switch (reserva.getTipoTarifa()) {
            case HORA:
                if (reserva.getCantidadHoras() == null || reserva.getCantidadHoras() <= 0) {
                    throw new IllegalArgumentException("Para tarifa por hora debe especificar cantidad de horas válida");
                }
                if (reserva.getPlacaVehiculo() == null || reserva.getPlacaVehiculo().isEmpty()) {
                    throw new IllegalArgumentException("Para tarifa por hora debe especificar la placa del vehículo");
                }
                break;
                
            case DIA:
                if (reserva.getCantidadDias() == null || reserva.getCantidadDias() <= 0) {
                    throw new IllegalArgumentException("Para tarifa por día debe especificar cantidad de días válida");
                }
                break;
                
            case MES:
                // No necesita validaciones adicionales específicas
                break;
                
            default:
                throw new IllegalArgumentException("Tipo de tarifa no soportado");
        }
        
        // Calcular fechas y valores según el tipo de tarifa
        calcularFechasYValor(reserva);
        
        // Establecer estado inicial si no viene
        if (reserva.getEstado() == null || reserva.getEstado().isEmpty()) {
            reserva.setEstado("ACTIVA");
        }
        Reserva reservaGuardada = reservaRepository.save(reserva);

        // 2. Crear y guardar el pago con la reserva ya guardada
        Pago pago = new Pago(reservaGuardada); 
        pagoRepository.save(pago);

        // 3. Retornar la reserva ya con pago asociado si deseas
        return reservaGuardada;
        
        
        
    }

    private void calcularFechasYValor(Reserva reserva) {
        // Obtener la tarifa correspondiente
        Tarifa tarifa = tarifaRepository.findByTipoVehiculoAndTipoTarifa(
            reserva.getTipoVehiculo(), 
            reserva.getTipoTarifa()
        ).orElseThrow(() -> new IllegalArgumentException("No se encontró tarifa para el tipo de vehículo y tarifa seleccionados"));
        
        // Calcular según el tipo de tarifa
        switch (reserva.getTipoTarifa()) {
            case HORA:
                reserva.setFechaHoraSalida(reserva.getFechaHoraInicio().plusHours(reserva.getCantidadHoras()));
                reserva.setValorFinal(tarifa.getValor().multiply(BigDecimal.valueOf(reserva.getCantidadHoras())));
                break;
                
            case DIA:
                reserva.setFechaHoraSalida(reserva.getFechaHoraInicio().plusDays(reserva.getCantidadDias()));
                reserva.setValorFinal(tarifa.getValor().multiply(BigDecimal.valueOf(reserva.getCantidadDias())));
                break;
                
            case MES:
                // Para tarifa mensual, asumimos 1 mes de duración
                reserva.setFechaVencimiento(reserva.getFechaHoraInicio().toLocalDate().plusMonths(1));
                reserva.setValorFinal(tarifa.getValor());
                break;
        }
    }

    @Override
public Reserva updateReserva(Reserva reserva, Long usuarioId) {
    Optional<Reserva> existingReserva = reservaRepository.findById(reserva.getId());
    if (existingReserva.isEmpty()) {
        throw new IllegalArgumentException("Reserva no encontrada con ID: " + reserva.getId());
    }

    Reserva toUpdate = existingReserva.get();

    if (!toUpdate.getUsuario().getId().equals(usuarioId) && !toUpdate.getUsuario().getRol().equals("ADMIN")) {
        throw new IllegalArgumentException("No tienes permisos para modificar esta reserva");
    }

    if (reserva.getTipoTarifa() != null && !reserva.getTipoTarifa().equals(toUpdate.getTipoTarifa())) {
        throw new IllegalArgumentException("No se puede cambiar el tipo de tarifa");
    }

    if (reserva.getEstado() != null) {
        toUpdate.setEstado(reserva.getEstado());
    }

    
    return reservaRepository.save(toUpdate);
}

@Override
public void deleteReserva(Long id, Long usuarioId) {
    Optional<Reserva> reserva = reservaRepository.findById(id);
    if (reserva.isEmpty()) {
        throw new IllegalArgumentException("Reserva no encontrada con ID: " + id);
    }

    Reserva r = reserva.get();

    if (!r.getUsuario().getId().equals(usuarioId) && !r.getUsuario().getRol().equals("ADMIN")) {
        throw new IllegalArgumentException("No tienes permisos para eliminar esta reserva");
    }

    reservaRepository.deleteById(id);
}

    
    public List<Reserva> getReservasByUsuarioId(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }
}

