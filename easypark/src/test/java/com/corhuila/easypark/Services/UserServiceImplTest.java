package com.corhuila.easypark.Services;

import com.corhuila.easypark.models.User;
import com.corhuila.easypark.repositories.IUserRepository;
import com.corhuila.easypark.services.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada prueba. Esto es esencial para que @Mock y @InjectMocks funcionen.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_encryptsPassword() {
        // Prueba que al guardar un usuario, la contraseña se encripta correctamente.

        // 1. Prepara los datos de prueba:
        User user = new User();
        user.setPassword("123456"); // Contraseña sin encriptar

        // 2. Define el comportamiento del mock:
        // Cuando userRepository.save() sea llamado con cualquier objeto User,
        // Mockito debe devolver el mismo objeto User que se le pasó como argumento.
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // 3. Ejecuta el método a probar:
        User savedUser = userService.saveUser(user);

        // 4. Verifica los resultados:
        // Asegura que la contraseña guardada no es la original (significa que fue encriptada).
        assertNotEquals("123456", savedUser.getPassword());
        // Asegura que la longitud de la contraseña encriptada es 64 caracteres (típico para SHA-256).
        assertEquals(64, savedUser.getPassword().length());
    }

    @Test
    void testGetAllUsers() {
        // Prueba que se pueden obtener todos los usuarios de la base de datos.

        // 1. Prepara los datos de prueba:
        // Crea una lista simulada de dos usuarios.
        List<User> list = List.of(new User(), new User());

        // 2. Define el comportamiento del mock:
        // Cuando userRepository.findAll() sea llamado, debe devolver la lista simulada.
        when(userRepository.findAll()).thenReturn(list);

        // 3. Ejecuta el método a probar:
        List<User> result = userService.getAllUsers();

        // 4. Verifica los resultados:
        // Asegura que el tamaño de la lista devuelta es 2, como se simuló.
        assertEquals(2, result.size());
    }

    @Test
    void testLogin_Successful() {
        // Prueba que el inicio de sesión es exitoso con credenciales correctas.

        // 1. Prepara los datos de prueba:
        User user = new User();
        user.setEmail("test@mail.com");
        // Encripta una contraseña para el usuario simulado, igual que lo haría el servicio.
        user.setPassword(userService.encryptPassword("password"));

        // 2. Define el comportamiento del mock:
        // Cuando userRepository.findByEmail() sea llamado con "test@mail.com", debe devolver el usuario simulado.
        when(userRepository.findByEmail("test@mail.com")).thenReturn(user);

        // 3. Ejecuta el método a probar:
        // Intenta iniciar sesión con el email y la contraseña correcta.
        User result = userService.login("test@mail.com", "password");

        // 4. Verifica los resultados:
        // Asegura que se devuelve un objeto User (inicio de sesión exitoso).
        assertNotNull(result);
    }

    @Test
    void testSendEmail() {
        // Prueba que el método de envío de correo electrónico se ejecuta sin lanzar excepciones.

        // 1. Define el comportamiento del mock:
        // Cuando mailSender.send() sea llamado con cualquier SimpleMailMessage, no debe hacer nada (simula el envío).
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // 2. Ejecuta el método a probar y verifica que no lanza excepciones:
        // Llama a sendEmail y asegura que no se produce ninguna excepción durante su ejecución.
        assertDoesNotThrow(() -> userService.sendEmail("dest@example.com", "Asunto", "Mensaje"));
    }

    @Test
    void testGenerateTemporaryPassword() {
        // Prueba que el método de generación de contraseña temporal crea una contraseña de la longitud esperada.

        // 1. Ejecuta el método a probar:
        String password = userService.generateTemporaryPassword();

        // 2. Verifica los resultados:
        // Asegura que la longitud de la contraseña generada es 8 caracteres.
        assertEquals(8, password.length());
    }
}