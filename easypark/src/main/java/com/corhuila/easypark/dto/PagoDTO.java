package com.corhuila.easypark.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.corhuila.easypark.models.Pago;

public class PagoDTO {
    private Long id;
    private Long reservaId;
    private BigDecimal monto;
    private String estado;
    private String metodoPago;
    private LocalDateTime fechaPago;
    private String codigoTransaccion;
    private String placaVehiculo;
    private String tipoVehiculo;
    private String tipoTarifa;
    private LocalDateTime fechaEntrada; // Solo para pagos directos
    private boolean esDirecto;

    public PagoDTO(Pago pago) {
          this.id = pago.getId();
    this.reservaId = pago.getReserva() != null ? pago.getReserva().getId() : null;
    this.monto = pago.getMonto();
    this.estado = pago.getEstado().toString();
    this.metodoPago = pago.getMetodoPago() != null ? pago.getMetodoPago().toString() : null;
    this.fechaPago = pago.getFechaPago();
    this.codigoTransaccion = pago.getCodigoTransaccion();
    this.esDirecto = pago.esPagoDirecto();

    if (this.esDirecto) {
        // Para pagos directos
        this.placaVehiculo = pago.getPlacaDirecto();
        this.tipoVehiculo = pago.getTipoVehiculoDirecto() != null ? 
                           pago.getTipoVehiculoDirecto().toString() : null;
        this.tipoTarifa = pago.getTipoTarifaDirecto(); // Usamos el método que obtiene de los detalles
        this.fechaEntrada = pago.getFechaEntradaDirecto();
    } else {
        // Para pagos con reserva
        this.placaVehiculo = pago.getReserva() != null ? 
                            pago.getReserva().getPlacaVehiculo() : null;
        this.tipoVehiculo = pago.getReserva() != null ? 
                           pago.getReserva().getTipoVehiculo().toString() : null;
        this.tipoTarifa = pago.getReserva() != null ? 
                         pago.getReserva().getTipoTarifa().toString() : null;
        this.fechaEntrada = null;
    }
    }

    // Getters (sin setters para inmutabilidad)
    public Long getId() {
        return id;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getEstado() {
        return estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public boolean isEsDirecto() {
        return esDirecto;
    }
}