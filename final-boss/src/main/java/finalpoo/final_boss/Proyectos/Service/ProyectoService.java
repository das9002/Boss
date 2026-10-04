package finalpoo.final_boss.Proyectos.Service;

import finalpoo.final_boss.Proyectos.DTO.ProyectoRequestDTO;
import finalpoo.final_boss.Proyectos.DTO.ProyectoResponseDTO;
import finalpoo.final_boss.Proyectos.Entity.ProyectoEntity;
import finalpoo.final_boss.Proyectos.Repository.ProyectoRepository;
import finalpoo.final_boss.Usuarios.Entity.UsuarioEntity;
import finalpoo.final_boss.Usuarios.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProyectoService(ProyectoRepository proyectoRepository, UsuarioRepository usuarioRepository) {
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ProyectoResponseDTO> obtenerTodos() {
    return proyectoRepository.findAll().stream()
        .map(this::convertirADto)
        .toList();
    }

        // Guardar un nuevo proyecto vinculándolo al usuario autenticado
    public ProyectoResponseDTO crearProyecto(ProyectoRequestDTO dto, String emailUsuario) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailUsuario)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ProyectoEntity proyecto = new ProyectoEntity();
        proyecto.setNombreProyecto(dto.nombreProyecto());
        proyecto.setDescripcion(dto.descripcion());
        proyecto.setPresupuesto(dto.presupuesto());
        proyecto.setEstado(dto.estado() != null ? dto.estado() : "PENDIENTE");
        proyecto.setUsuario(usuario);

        ProyectoEntity guardado = proyectoRepository.save(proyecto);
        return convertirADto(guardado);
    }

        // Mapeador auxiliar interno: Entidad -> DTO
    private ProyectoResponseDTO convertirADto(ProyectoEntity p) {
        String nombreResponsable = (p.getUsuario() != null) ? p.getUsuario().getNombre() : "Sin Asignar";
        return new ProyectoResponseDTO(
            p.getProyectoId(),
            p.getNombreProyecto(),
            p.getDescripcion(),
            p.getPresupuesto(),
            p.getEstado(),
            nombreResponsable
        );
    }
}
