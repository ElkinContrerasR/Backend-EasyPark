package com.corhuila.easypark.services;

import java.math.BigDecimal;
import java.util.List;
import com.corhuila.easypark.models.Pago;
import com.corhuila.easypark.models.TipoTarifa;
import com.corhuila.easypark.models.TipoVehiculo;

public interface IPagoService {
    Pago crearPagoParaReserva(Long reservaId);
    Pago procesarPago(Long pagoId, Pago.MetodoPago metodoPago);
    Pago rechazarPago(Long pagoId);
    List<Pago> obtenerPagosPorUsuario(Long usuarioId);
    List<Pago> obtenerPagosPorEstado(Pago.EstadoPago estado);
    String generarComprobantePago(Long pagoId);
    

    //metodo para obtener pagos sin reserva
    List<Pago> obtenerPagosDirectos();

    //Obtener pagos
    List<Pago> obtenerTodosLosPagos();

    
    Pago crearPagoDirecto(
        BigDecimal monto,
        String placaVehiculo,
        TipoVehiculo tipoVehiculo,
        Pago.EstadoPago estado,  // Nuevo parámetro
        Pago.MetodoPago metodoPago,
        TipoTarifa tipoTarifa
    );
}