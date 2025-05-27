package com.corhuila.easypark.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.corhuila.easypark.dto.PagoDTO;
import com.corhuila.easypark.models.Pago;
import com.corhuila.easypark.models.TipoTarifa;
import com.corhuila.easypark.models.TipoVehiculo;
import com.corhuila.easypark.services.IPagoService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "http://localhost:4200")
public class PagoController {

    @Autowired
    private IPagoService pagoService;

    @PostMapping("/reserva/{reservaId}")
    public ResponseEntity<Pago> crearPago(@PathVariable Long reservaId) {
        Pago pago = pagoService.crearPagoParaReserva(reservaId);
        return new ResponseEntity<>(pago, HttpStatus.CREATED);
    }

    //Endpoint para guardar pago sin reserva
    @PostMapping("/directo")
    public ResponseEntity<Map<String, Object>> crearPagoDirecto(
        @RequestParam BigDecimal monto,
        @RequestParam String placaVehiculo,
        @RequestParam String tipoVehiculo,
        @RequestParam Pago.EstadoPago estado,  // Ahora es requerido
        @RequestParam(required = false) Pago.MetodoPago metodoPago,
        @RequestParam(required = false) TipoTarifa tipoTarifa) {
        
        // Validar tipoVehiculo
        TipoVehiculo tipo;
        try {
            tipo = TipoVehiculo.valueOf(tipoVehiculo.toUpperCase());
        } catch (IllegalArgumentException e) {
            StringBuilder errorMsg = new StringBuilder("Tipo de vehículo inválido. Opciones válidas: ");
            for (TipoVehiculo tv : TipoVehiculo.values()) {
                errorMsg.append(tv.name()).append(", ");
            }
            errorMsg.setLength(errorMsg.length() - 2);
            throw new IllegalArgumentException(errorMsg.toString());
        }

        Pago pago = pagoService.crearPagoDirecto(
            monto,
            placaVehiculo,
            tipo,
            estado,       // Nuevo parámetro
            metodoPago,
            tipoTarifa
        );
        
        // Crear respuesta personalizada
        Map<String, Object> response = new HashMap<>();
        response.put("id", pago.getId());
        response.put("monto", pago.getMonto());
        response.put("estado", pago.getEstado().toString());
        response.put("placa", pago.getPlacaDirecto());
        response.put("tipoVehiculo", pago.getTipoVehiculoDirecto().toString());
        response.put("tipoTarifa", pago.getTipoTarifaDirecto());
        response.put("codigoTransaccion", pago.getCodigoTransaccion());
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{pagoId}/procesar")
    public ResponseEntity<Pago> procesarPago(
            @PathVariable Long pagoId,
            @RequestParam Pago.MetodoPago metodoPago) {
        
        Pago pago = pagoService.procesarPago(pagoId, metodoPago);
        return new ResponseEntity<>(pago, HttpStatus.OK);
    }

    @PostMapping("/{pagoId}/rechazar")
    public ResponseEntity<Pago> rechazarPago(@PathVariable Long pagoId) {
        Pago pago = pagoService.rechazarPago(pagoId);
        return new ResponseEntity<>(pago, HttpStatus.OK);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PagoDTO>> obtenerPagosUsuario(@PathVariable Long usuarioId) {
        List<Pago> pagos = pagoService.obtenerPagosPorUsuario(usuarioId);
        List<PagoDTO> pagosDTO = pagos.stream()
                                    .map(PagoDTO::new)
                                    .collect(Collectors.toList());
        return new ResponseEntity<>(pagosDTO, HttpStatus.OK);
    }


    //metodo para obtener pagos sin reserva
    @GetMapping("/directos")
    public ResponseEntity<List<Pago>> obtenerPagosDirectos() {
        List<Pago> pagos = pagoService.obtenerPagosDirectos();
        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }

    //método para obtener el comprobante por idpago
    @GetMapping("/{pagoId}/comprobante")
    public ResponseEntity<String> generarComprobante(@PathVariable Long pagoId) {
        String comprobante = pagoService.generarComprobantePago(pagoId);
        return new ResponseEntity<>(comprobante, HttpStatus.OK);
 
    }

    //Método para obtener los pagos sin enviar idusuario
    @GetMapping
    public ResponseEntity<List<PagoDTO>> obtenerTodosLosPagos() {
        List<Pago> pagos = pagoService.obtenerTodosLosPagos();
        List<PagoDTO> pagosDTO = pagos.stream()
                                    .map(PagoDTO::new)
                                    .collect(Collectors.toList());
        return new ResponseEntity<>(pagosDTO, HttpStatus.OK);
    }
}