package finalpoo.final_boss.Security;

import finalpoo.final_boss.Rol.Entity.RolEntity;
import finalpoo.final_boss.Rol.Repository.RolRepository;
import finalpoo.final_boss.Usuarios.Entity.UsuarioEntity;
import finalpoo.final_boss.Usuarios.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear Rol si no existe
        // Forma correcta de usar la instancia inyectada:
        RolEntity rolUser = rolRepository.findByNombreRol("ROLE_USER")
                .orElseGet(() -> rolRepository.save(new RolEntity("ROLE_USER")));

        // Crear Usuario de prueba si no existe
        if (!usuarioRepository.existsByEmail("admin@empresa.com")) {
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setNombre("Carlos Mendoza");
            usuario.setEmail("admin@empresa.com");
            // Cifra la contraseña "123456" usando BCrypt
            usuario.setPasswordHash(new BCryptPasswordEncoder().encode("123456"));
            usuario.setRol(rolUser);
            usuarioRepository.save(usuario);
        }
    }
}