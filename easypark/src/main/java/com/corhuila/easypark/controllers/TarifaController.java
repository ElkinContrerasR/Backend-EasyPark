package com.corhuila.easypark.controllers;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corhuila.easypark.models.Tarifa;
import com.corhuila.easypark.models.TipoTarifa;
import com.corhuila.easypark.models.TipoVehiculo;
import com.corhuila.easypark.repositories.IUserRepository;
import com.corhuila.easypark.services.ITarifaService;

@RestController
@RequestMapping("/api/tarifas")
@CrossOrigin(origins = "http://localhost:4200")
public class TarifaController {
    @Autowired
    ITarifaService tarifaService;
    
    @Autowired
    IUserRepository userRepository;

    // Todos pueden ver las tarifas
    @GetMapping("getAll")
    public ResponseEntity<List<Tarifa>> getAllTarifa() {
        return ResponseEntity.ok(tarifaService.getAllTarifas());
    }

    // Solo ADMIN puede crear
    @PostMapping
    public ResponseEntity<?> saveTarifa(@RequestBody Tarifa tarifa, 
                                       @RequestHeader("X-User-Email") String email) {
        try {
            return ResponseEntity.ok(tarifaService.saveTarifa(tarifa, email));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // Solo ADMIN puede actualizar
    @PutMapping
    public ResponseEntity<?> updateTarifa(@RequestBody Tarifa tarifa,
                                        @RequestHeader("X-User-Email") String email) {
        try {
            return ResponseEntity.ok(tarifaService.updateTarifa(tarifa, email));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // Solo ADMIN puede eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTarifa(@PathVariable Long id,
                                        @RequestHeader("X-User-Email") String email) {
        try {
            tarifaService.deleteTarifa(id, email);
            return ResponseEntity.ok("Tarifa eliminada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    
    // Endpoints para obtener los enums
    @GetMapping("/tipos-vehiculo")
    public ResponseEntity<List<Map<String, String>>> getTiposVehiculo() {
        return ResponseEntity.ok(
            Arrays.stream(TipoVehiculo.values())
                .map(tipo -> Map.of(
                    "codigo", tipo.name(),
                    "nombre", tipo.getNombre()
                ))
                .collect(Collectors.toList())
        );
    }
    
    @GetMapping("/tipos-tarifa")
    public ResponseEntity<List<Map<String, String>>> getTiposTarifa() {
        return ResponseEntity.ok(
            Arrays.stream(TipoTarifa.values())
                .map(tipo -> Map.of(
                    "codigo", tipo.name(),
                    "descripcion", tipo.getDescripcion()
                ))
                .collect(Collectors.toList())
        );
    }

}
