package finalpoo.final_boss.Usuarios.Service;

import finalpoo.final_boss.Rol.Entity.RolEntity;
import finalpoo.final_boss.Rol.Repository.RolRepository;
import finalpoo.final_boss.Usuarios.Entity.UsuarioEntity;
import finalpoo.final_boss.Usuarios.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public List<UsuarioEntity> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public UsuarioEntity obtenerPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
    }

    public void cambiarRolUsuario(Long usuarioId, String nuevoRol) {

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        RolEntity rol = rolRepository.findByNombreRol(nuevoRol)
                .orElseThrow(() -> new RuntimeException("El rol '" + nuevoRol + "' no existe en el sistema"));

        usuario.setRol(rol);

        usuarioRepository.save(usuario);
    }
}
