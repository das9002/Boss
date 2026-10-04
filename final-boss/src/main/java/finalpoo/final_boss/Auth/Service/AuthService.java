package finalpoo.final_boss.Auth.Service;

import finalpoo.final_boss.Auth.DTO.AuthResponse;
import finalpoo.final_boss.Auth.DTO.LoginRequest;
import finalpoo.final_boss.Auth.DTO.RegisterRequest;
import finalpoo.final_boss.Rol.Entity.RolEntity;
import finalpoo.final_boss.Rol.Repository.RolRepository;
import finalpoo.final_boss.Security.JwtUtils;
import finalpoo.final_boss.Usuarios.Entity.UsuarioEntity;
import finalpoo.final_boss.Usuarios.Repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository, JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public AuthResponse login(LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtils.generarToken(usuario.getEmail(), usuario.getRol().getNombreRol());

        return new AuthResponse(
                token,
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getRol().getNombreRol()
        );
    }

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya se encuentra registrado");
        }

        // 1. Evalúa si enviaron un rol en la petición. Si viene nulo o vacío, asigna ROLE_USER por defecto
        String nombreRolSolicitado = (request.rol() != null && !request.rol().isBlank())
                ? request.rol()
                : "ROLE_USER";

        // 2. Busca el rol dinámico en la base de datos
        RolEntity rolAsignado = rolRepository.findByNombreRol(nombreRolSolicitado)
                .orElseThrow(() -> new RuntimeException("Error: El rol '" + nombreRolSolicitado + "' no existe en la BD"));

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        usuario.setRol(rolAsignado);

        UsuarioEntity guardado = usuarioRepository.save(usuario);

        String token = jwtUtils.generarToken(guardado.getEmail(), guardado.getRol().getNombreRol());

        return new AuthResponse(
                token,
                guardado.getEmail(),
                guardado.getNombre(),
                guardado.getRol().getNombreRol()
        );
    }
}