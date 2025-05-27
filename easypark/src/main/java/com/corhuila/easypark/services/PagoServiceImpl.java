package com.corhuila.easypark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.corhuila.easypark.models.*;
import com.corhuila.easypark.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class PagoServiceImpl implements IPagoService {

    @Autowired
    private IPagoRepository pagoRepository;

    @Autowired
    private IReservaRepository reservaRepository;

    @Override
    public Pago crearPagoParaReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
            .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        
        if (reserva.getPago() != null) {
            return reserva.getPago();
        }
        
        Pago nuevoPago = reserva.crearPagoAsociado();
        return pagoRepository.save(nuevoPago);
    }

    @Override
    public Pago crearPagoDirecto(
        BigDecimal monto,
        String placaVehiculo,
        TipoVehiculo tipoVehiculo,
        Pago.EstadoPago estado,
        Pago.MetodoPago metodoPago,
        TipoTarifa tipoTarifa) {

        Pago pago = new Pago();
        
        // Configurar campos recibidos
        pago.setMonto(monto);
        pago.setEstado(estado);  // Usamos el estado recibido
        pago.setMetodoPago(metodoPago);
        
        // Autogenerar campos
        pago.setFechaPago(LocalDateTime.now());
        pago.setReferenciaPago("REF-" + UUID.randomUUID().toString().substring(0, 8));
        
        // Configurar detalles
        String tarifa = tipoTarifa != null ? tipoTarifa.name() : "DIRECTO";
        pago.setDetalles(
            String.format("DIRECTO|%s|%s|%s|%s", 
                placaVehiculo, 
                tipoVehiculo.name(), 
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                tarifa)
        );
        
        return pagoRepository.save(pago);
    }

    @Override
    public Pago procesarPago(Long pagoId, Pago.MetodoPago metodoPago) {
        Pago pago = pagoRepository.findById(pagoId)
            .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));
        
        pago.procesarPago(metodoPago);
        return pagoRepository.save(pago);
    }

    @Override
    public Pago rechazarPago(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
            .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));
        
        pago.rechazarPago();
        return pagoRepository.save(pago);
    }

    @Override
    public List<Pago> obtenerPagosPorUsuario(Long usuarioId) {
        return pagoRepository.findByReservaUsuarioId(usuarioId);
    }

    @Override
    public List<Pago> obtenerPagosPorEstado(Pago.EstadoPago estado) {
        return pagoRepository.findByEstado(estado);
    }

    @Override
    public String generarComprobantePago(Long pagoId) {

    Pago pago = pagoRepository.findById(pagoId)
        .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));
    return pago.generarComprobante();
    
    }

     //metodo para obtener pagos sin reserva
    @Override
    public List<Pago> obtenerPagosDirectos() {
        return pagoRepository.findByReservaIsNull();
    }

    @Override
    public List<Pago> obtenerTodosLosPagos() {
        return pagoRepository.findAll(); // Usa el método de JpaRepository
    }
}