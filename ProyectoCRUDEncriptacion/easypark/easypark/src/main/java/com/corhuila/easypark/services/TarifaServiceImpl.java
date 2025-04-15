package com.corhuila.easypark.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corhuila.easypark.models.Rol;
import com.corhuila.easypark.models.Tarifa;
import com.corhuila.easypark.models.User;
import com.corhuila.easypark.repositories.ITarifaRepository;
import com.corhuila.easypark.repositories.IUserRepository;

@Service
public class TarifaServiceImpl implements ITarifaService {


     @Autowired
    ITarifaRepository tarifaRepository; // Cambiado de tarifaService a tarifaRepository
    
    @Autowired
    IUserRepository userRepository;

    @Override
    public List<Tarifa> getAllTarifas() {
        return tarifaRepository.findAll();
    }

    @Override
    public Tarifa saveTarifa(Tarifa tarifa, String emailCreador) {
        User creador = userRepository.findByEmail(emailCreador);
        if (creador == null || creador.getRol() != Rol.ADMIN) {
            throw new RuntimeException("No tiene permisos para crear tarifas");
        }
        tarifa.setCreadoPor(creador);
        return tarifaRepository.save(tarifa);
    }

    @Override
    public Tarifa updateTarifa(Tarifa tarifa, String emailEditor) {
        User editor = userRepository.findByEmail(emailEditor);
        if (editor == null || editor.getRol() != Rol.ADMIN) {
            throw new RuntimeException("No tiene permisos para actualizar tarifas");
        }
        
        Tarifa tarifaExistente = tarifaRepository.findById(tarifa.getId())
            .orElseThrow(() -> new RuntimeException("Tarifa no encontrada"));
        
        tarifaExistente.setTipoVehiculo(tarifa.getTipoVehiculo());
        tarifaExistente.setTipoTarifa(tarifa.getTipoTarifa());
        tarifaExistente.setValor(tarifa.getValor());
        
        return tarifaRepository.save(tarifaExistente);
    }

    @Override
    public void deleteTarifa(Long id, String emailEliminador) {
        User eliminador = userRepository.findByEmail(emailEliminador);
        if (eliminador == null || eliminador.getRol() != Rol.ADMIN) {
            throw new RuntimeException("No tiene permisos para eliminar tarifas");
        }
        tarifaRepository.deleteById(id);
    }
    
}
