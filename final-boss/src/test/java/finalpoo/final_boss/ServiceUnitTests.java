package finalpoo.final_boss;

import finalpoo.final_boss.Auth.DTO.AuthResponse;
import finalpoo.final_boss.Auth.DTO.LoginRequest;
import finalpoo.final_boss.Auth.DTO.RegisterRequest;
import finalpoo.final_boss.Auth.Service.AuthService;
import finalpoo.final_boss.Proyectos.DTO.ProyectoRequestDTO;
import finalpoo.final_boss.Proyectos.DTO.ProyectoResponseDTO;
import finalpoo.final_boss.Proyectos.Entity.ProyectoEntity;
import finalpoo.final_boss.Proyectos.Repository.ProyectoRepository;
import finalpoo.final_boss.Proyectos.Service.ProyectoService;
import finalpoo.final_boss.Rol.Entity.RolEntity;
import finalpoo.final_boss.Rol.Repository.RolRepository;
import finalpoo.final_boss.Security.JwtUtils;
import finalpoo.final_boss.Usuarios.Entity.UsuarioEntity;
import finalpoo.final_boss.Usuarios.Repository.UsuarioRepository;
import finalpoo.final_boss.Usuarios.Service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceUnitTests {

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private JwtUtils jwtUtils;

    private ProyectoService proyectoService;
    private UsuarioService usuarioService;
    private AuthService authService;

    private RolEntity rolUser;
    private UsuarioEntity usuarioMock;
    private ProyectoEntity proyectoMock;

    @BeforeEach
    void setUp() {
        proyectoService = new ProyectoService(proyectoRepository, usuarioRepository);
        usuarioService = new UsuarioService(usuarioRepository, rolRepository);
        authService = new AuthService(usuarioRepository, rolRepository, jwtUtils);

        rolUser = new RolEntity(1L, "ROLE_USER");

        usuarioMock = new UsuarioEntity();
        usuarioMock.setUsuarioId(1L);
        usuarioMock.setNombre("Test User");
        usuarioMock.setEmail("test@example.com");
        usuarioMock.setPasswordHash(new BCryptPasswordEncoder().encode("123456"));
        usuarioMock.setRol(rolUser);

        proyectoMock = new ProyectoEntity();
        proyectoMock.setProyectoId(10L);
        proyectoMock.setNombreProyecto("Proyecto Alfa");
        proyectoMock.setDescripcion("Descripcion de prueba");
        proyectoMock.setPresupuesto(new BigDecimal("5000.00"));
        proyectoMock.setEstado("PENDIENTE");
        proyectoMock.setUsuario(usuarioMock);
    }

    // --- PROYECTO SERVICE TESTS ---

    @Test
    void testObtenerTodosProyectos() {
        when(proyectoRepository.findAll()).thenReturn(List.of(proyectoMock));

        List<ProyectoResponseDTO> lista = proyectoService.obtenerTodos();

        assertEquals(1, lista.size());
        assertEquals("Proyecto Alfa", lista.get(0).nombreProyecto());
        assertEquals("Test User", lista.get(0).nombreUsuarioResponsable());
    }

    @Test
    void testObtenerProyectoPorId_Exitoso() {
        when(proyectoRepository.findById(10L)).thenReturn(Optional.of(proyectoMock));

        ProyectoResponseDTO response = proyectoService.obtenerPorId(10L);

        assertNotNull(response);
        assertEquals(10L, response.proyectoId());
        assertEquals("Proyecto Alfa", response.nombreProyecto());
    }

    @Test
    void testObtenerProyectoPorId_NoEncontrado() {
        when(proyectoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> proyectoService.obtenerPorId(99L));
        assertTrue(exception.getMessage().contains("Proyecto no encontrado"));
    }

    @Test
    void testCrearProyecto_Exitoso() {
        ProyectoRequestDTO dto = new ProyectoRequestDTO("Nuevo Proyecto", "Desc", new BigDecimal("1000.00"), "EN_PROGRESO");

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuarioMock));
        when(proyectoRepository.save(any(ProyectoEntity.class))).thenAnswer(invocation -> {
            ProyectoEntity p = invocation.getArgument(0);
            p.setProyectoId(20L);
            return p;
        });

        ProyectoResponseDTO creado = proyectoService.crearProyecto(dto, "test@example.com");

        assertNotNull(creado);
        assertEquals(20L, creado.proyectoId());
        assertEquals("Nuevo Proyecto", creado.nombreProyecto());
        assertEquals("EN_PROGRESO", creado.estado());
    }

    @Test
    void testActualizarProyecto_Exitoso() {
        ProyectoRequestDTO dtoActualizacion = new ProyectoRequestDTO("Proyecto Modificado", "Nueva Desc", new BigDecimal("7000.00"), "FINALIZADO");

        when(proyectoRepository.findById(10L)).thenReturn(Optional.of(proyectoMock));
        when(proyectoRepository.save(any(ProyectoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProyectoResponseDTO actualizado = proyectoService.actualizarProyecto(10L, dtoActualizacion);

        assertNotNull(actualizado);
        assertEquals("Proyecto Modificado", actualizado.nombreProyecto());
        assertEquals("FINALIZADO", actualizado.estado());
    }

    @Test
    void testEliminarProyecto_Exitoso() {
        when(proyectoRepository.findById(10L)).thenReturn(Optional.of(proyectoMock));
        doNothing().when(proyectoRepository).delete(proyectoMock);

        assertDoesNotThrow(() -> proyectoService.eliminarProyecto(10L));
        verify(proyectoRepository, times(1)).delete(proyectoMock);
    }

    // --- USUARIO SERVICE TESTS ---

    @Test
    void testObtenerTodosUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioMock));

        List<UsuarioEntity> usuarios = usuarioService.obtenerTodos();

        assertEquals(1, usuarios.size());
        assertEquals("test@example.com", usuarios.get(0).getEmail());
    }

    @Test
    void testCambiarRolUsuario_Exitoso() {
        RolEntity rolAdmin = new RolEntity(2L, "ROLE_ADMIN");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(rolRepository.findByNombreRol("ROLE_ADMIN")).thenReturn(Optional.of(rolAdmin));

        usuarioService.cambiarRolUsuario(1L, "ROLE_ADMIN");

        assertEquals(rolAdmin, usuarioMock.getRol());
        verify(usuarioRepository, times(1)).save(usuarioMock);
    }

    // --- AUTH SERVICE TESTS ---

    @Test
    void testLogin_Exitoso() {
        LoginRequest loginReq = new LoginRequest("test@example.com", "123456");

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuarioMock));
        when(jwtUtils.generarToken("test@example.com", "ROLE_USER")).thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(loginReq);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.token());
        assertEquals("test@example.com", response.email());
    }

    @Test
    void testRegister_Exitoso() {
        RegisterRequest registerReq = new RegisterRequest("Nuevo User", "nuevo@example.com", "123456", "ROLE_USER");

        when(usuarioRepository.existsByEmail("nuevo@example.com")).thenReturn(false);
        when(rolRepository.findByNombreRol("ROLE_USER")).thenReturn(Optional.of(rolUser));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(invocation -> {
            UsuarioEntity u = invocation.getArgument(0);
            u.setUsuarioId(5L);
            return u;
        });
        when(jwtUtils.generarToken("nuevo@example.com", "ROLE_USER")).thenReturn("fake-jwt-token");

        AuthResponse response = authService.register(registerReq);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.token());
        assertEquals("nuevo@example.com", response.email());
    }
}
