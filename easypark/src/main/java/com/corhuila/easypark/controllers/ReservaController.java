package com.corhuila.easypark.controllers;

import com.corhuila.easypark.models.Pago;
import com.corhuila.easypark.models.Reserva;
import com.corhuila.easypark.services.IPagoService;
import com.corhuila.easypark.services.IReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {

    @Autowired
    private IReservaService reservaService;

    @Autowired
    private IPagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        return new ResponseEntity<>(reservaService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createReserva(@RequestBody Reserva reserva) {
        try {
            Reserva nuevaReserva = reservaService.saveReserva(reserva);
            return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<?> updateReserva(@PathVariable Long id, @PathVariable Long usuarioId, @RequestBody Reserva reserva) {
        try {
            reserva.setId(id);
            Reserva reservaActualizada = reservaService.updateReserva(reserva, usuarioId);
            return new ResponseEntity<>(reservaActualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id, @PathVariable Long usuarioId) {
        try {
            reservaService.deleteReserva(id, usuarioId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Reserva>> getReservasByUsuario(@PathVariable Long usuarioId) {
        List<Reserva> reservas = reservaService.getReservasByUsuarioId(usuarioId);
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    @PostMapping("/{reservaId}/generar-pago")
public ResponseEntity<Pago> generarPagoParaReserva(@PathVariable Long reservaId) {
    try {
        Pago pago = pagoService.crearPagoParaReserva(reservaId);
        return new ResponseEntity<>(pago, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
}
