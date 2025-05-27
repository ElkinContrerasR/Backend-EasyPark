package com.corhuila.easypark.Services;

import com.corhuila.easypark.models.*;
import com.corhuila.easypark.repositories.*;
import com.corhuila.easypark.services.TarifaServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TarifaServiceImplTest {

    @InjectMocks
    private TarifaServiceImpl tarifaService;

    @Mock
    private ITarifaRepository tarifaRepository;

    @Mock
    private IUserRepository userRepository; // Necesario para pruebas de permisos

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada prueba.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTarifas() {
        // Prueba que se pueden obtener todas las tarifas existentes.

        // 1. Prepara los datos de prueba:
        // Simula una lista con dos objetos Tarifa.
        when(tarifaRepository.findAll()).thenReturn(List.of(new Tarifa(), new Tarifa()));

        // 2. Ejecuta el método a probar:
        List<Tarifa> tarifas = tarifaService.getAllTarifas();

        // 3. Verifica los resultados:
        // Asegura que la lista de tarifas obtenida tiene un tamaño de 2.
        assertEquals(2, tarifas.size());
    }

    @Test
    void testSaveTarifa_WithAdmin() {
        // Prueba que una tarifa se puede guardar exitosamente si el usuario es ADMINISTRADOR.

        // 1. Prepara los datos de prueba:
        User admin = new User();
        admin.setRol(Rol.ADMIN); // Configura el rol del usuario como ADMIN
        Tarifa tarifa = new Tarifa(); // Tarifa a guardar

        // 2. Define el comportamiento de los mocks:
        // Cuando userRepository.findByEmail() es llamado con "admin@mail.com", devuelve el usuario admin.
        when(userRepository.findByEmail("admin@mail.com")).thenReturn(admin);
        // Cuando tarifaRepository.save() es llamado con cualquier Tarifa, devuelve esa misma tarifa (simula el guardado).
        when(tarifaRepository.save(any(Tarifa.class))).thenReturn(tarifa);

        // 3. Ejecuta el método a probar:
        Tarifa result = tarifaService.saveTarifa(tarifa, "admin@mail.com");

        // 4. Verifica los resultados:
        // Asegura que se devuelve un objeto Tarifa no nulo.
        assertNotNull(result);
        // Verifica que el método save() del repositorio de tarifas fue llamado exactamente una vez.
        verify(tarifaRepository).save(any(Tarifa.class));
    }

    @Test
    void testSaveTarifa_WithNoAdmin_ThrowsException() {
        // Prueba que se lanza una excepción si se intenta guardar una tarifa sin permisos de administrador.

        // 1. Prepara los datos de prueba:
        User normalUser = new User();
        normalUser.setRol(Rol.USER); // Configura el rol del usuario como USER (no admin)

        // 2. Define el comportamiento del mock:
        // Cuando userRepository.findByEmail() es llamado con "user@mail.com", devuelve el usuario normal.
        when(userRepository.findByEmail("user@mail.com")).thenReturn(normalUser);

        // 3. Ejecuta el método a probar y verifica la excepción:
        // Intenta guardar una tarifa con un usuario sin permisos y verifica que se lanza una RuntimeException.
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            tarifaService.saveTarifa(new Tarifa(), "user@mail.com")
        );

        // 4. Verifica los resultados:
        // Asegura que el mensaje de la excepción contiene la frase "No tiene permisos".
        assertTrue(ex.getMessage().contains("No tiene permisos"));
    }

    @Test
    void testDeleteTarifa_WithAdmin() {
        // Prueba que una tarifa se puede eliminar exitosamente si el usuario es ADMINISTRADOR.

        // 1. Prepara los datos de prueba:
        User admin = new User();
        admin.setRol(Rol.ADMIN); // Configura el rol del usuario como ADMIN

        // 2. Define el comportamiento del mock:
        // Cuando userRepository.findByEmail() es llamado con "admin@mail.com", devuelve el usuario admin.
        when(userRepository.findByEmail("admin@mail.com")).thenReturn(admin);

        // 3. Ejecuta el método a probar y verifica que no lanza excepciones:
        // Llama a deleteTarifa y asegura que no se produce ninguna excepción durante su ejecución.
        assertDoesNotThrow(() -> tarifaService.deleteTarifa(1L, "admin@mail.com"));

        // 4. Verifica los resultados:
        // Verifica que el método deleteById() del repositorio de tarifas fue llamado exactamente una vez con el ID 1L.
        verify(tarifaRepository).deleteById(1L);
    }

    @Test
    void testUpdateTarifa_WithAdmin() {
        // Prueba que una tarifa se puede actualizar exitosamente si el usuario es ADMINISTRADOR.

        // 1. Prepara los datos de prueba:
        User admin = new User();
        admin.setRol(Rol.ADMIN); // Configura el rol del usuario como ADMIN

        Tarifa tarifa = new Tarifa(); // Tarifa con datos actualizados
        tarifa.setId(1L);
        tarifa.setValor(BigDecimal.valueOf(1000));

        Tarifa existing = new Tarifa(); // Tarifa existente en la "base de datos"
        existing.setId(1L);

        // 2. Define el comportamiento de los mocks:
        // Cuando userRepository.findByEmail() es llamado con "admin@mail.com", devuelve el usuario admin.
        when(userRepository.findByEmail("admin@mail.com")).thenReturn(admin);
        // Cuando tarifaRepository.findById() es llamado con 1L, devuelve la tarifa existente.
        when(tarifaRepository.findById(1L)).thenReturn(Optional.of(existing));
        // Cuando tarifaRepository.save() es llamado con cualquier Tarifa, devuelve la tarifa existente (simula el guardado).
        when(tarifaRepository.save(any(Tarifa.class))).thenReturn(existing);

        // 3. Ejecuta el método a probar:
        Tarifa updated = tarifaService.updateTarifa(tarifa, "admin@mail.com");

        // 4. Verifica los resultados:
        // Asegura que se devuelve un objeto Tarifa no nulo.
        assertNotNull(updated);
        // Verifica que el método save() del repositorio de tarifas fue llamado exactamente una vez.
        verify(tarifaRepository).save(any(Tarifa.class));
    }
}