package com.corhuila.easypark.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "pagos")
public class Pago {

    public enum EstadoPago {
        PENDIENTE, COMPLETADO, RECHAZADO, REEMBOLSADO
    }

    public enum MetodoPago {
        EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Reserva reserva;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    @Column
    private LocalDateTime fechaPago;

    @Column(unique = true)
    private String codigoTransaccion;

    @Column
    private String referenciaPago;

    @Column
    private String detalles;

    // Constructores
    public Pago() {
        this.estado = EstadoPago.PENDIENTE;
        this.codigoTransaccion = "PAY-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public Pago(Reserva reserva) {
        this();
        this.reserva = reserva;
        this.monto = reserva.getValorFinal();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }

    public String getReferenciaPago() {
        return referenciaPago;
    }

    public void setReferenciaPago(String referenciaPago) {
        this.referenciaPago = referenciaPago;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    // Métodos de Pago con reserva
    public void procesarPago(MetodoPago metodo) {
        this.metodoPago = metodo;
        this.estado = EstadoPago.COMPLETADO;
        this.fechaPago = LocalDateTime.now();
        if (this.reserva != null) {
            this.reserva.setEstado("CONFIRMADA");
        }
    }

    public void rechazarPago() {
        this.estado = EstadoPago.RECHAZADO;
        if (this.reserva != null) {
            this.reserva.setEstado("CANCELADA");
        }
    }

    //Métodos de pago sin reserva
    public boolean esPagoDirecto() {
    return this.reserva == null && this.detalles != null && this.detalles.startsWith("DIRECTO|");
        }

    public void configurarComoPagoDirecto(String placa, TipoVehiculo tipo, BigDecimal monto) {
            this.monto = monto;
            this.detalles = String.format("DIRECTO|%s|%s|%s", 
                placa, 
                tipo.name(), 
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public String getPlacaDirecto() {
            if (!esPagoDirecto()) return null;
            return detalles.split("\\|")[1];
    }

    public TipoVehiculo getTipoVehiculoDirecto() {
            if (!esPagoDirecto()) return null;
            return TipoVehiculo.valueOf(detalles.split("\\|")[2]);
    }

    public LocalDateTime getFechaEntradaDirecto() {
            if (!esPagoDirecto()) return null;
            return LocalDateTime.parse(detalles.split("\\|")[3]);
    }


    // Método para generar un comprobante básico
    public String generarComprobante() {
        if (this.esPagoDirecto()) {
        return String.format(
            "COMPROBANTE DE PAGO DIRECTO #%s\n" +
            "Placa: %s\n" +
            "Tipo Vehículo: %s\n" +
            "Monto: $%,.2f\n" +
            "Fecha Entrada: %s\n" +
            "Estado: %s\n" +
            "Código Transacción: %s",
            this.id,
            this.getPlacaDirecto(),
            this.getTipoVehiculoDirecto(),
            this.monto,
            this.getFechaEntradaDirecto(),
            this.estado,
            this.codigoTransaccion
        );
    } else {
        return String.format(
            "COMPROBANTE DE PAGO #%s\n" +
            "Reserva: %s\n" +
            "Placa: %s\n" +
            "Tipo Vehículo: %s\n" +
            "Tarifa: %s\n" +
            "Horas/Días: %s/%s\n" +
            "Monto: $%,.2f\n" +
            "Método: %s\n" +
            "Fecha Pago: %s\n" +
            "Código Transacción: %s",
            this.id,
            this.reserva != null ? this.reserva.getId() : "N/A",
            this.reserva != null ? this.reserva.getPlacaVehiculo() : "N/A",
            this.reserva != null ? this.reserva.getTipoVehiculo().toString() : "N/A",
            this.reserva != null ? this.reserva.getTipoTarifa().toString() : "N/A",
            this.reserva != null && this.reserva.getCantidadHoras() != null ? this.reserva.getCantidadHoras() : 0,
            this.reserva != null && this.reserva.getCantidadDias() != null ? this.reserva.getCantidadDias() : 0,
            this.monto,
            this.metodoPago != null ? this.metodoPago.toString() : "NO APLICA",
            this.fechaPago != null ? this.fechaPago.toString() : "PENDIENTE",
            this.codigoTransaccion
        );
    }
    }

    public String getTipoTarifaDirecto() {
        if (!esPagoDirecto()) return "DIRECTO"; // Valor por defecto
        
        String[] partes = detalles.split("\\|");
        // El tipo de tarifa está en la posición 4 del array (índice 4)
        return partes.length > 4 ? partes[4] : "DIRECTO";
    }
}