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

    public ProyectoResponseDTO obtenerPorId(Long id) {
        ProyectoEntity proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
        return convertirADto(proyecto);
    }

    public List<ProyectoResponseDTO> obtenerPorUsuarioId(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
        return proyectoRepository.findByUsuarioUsuarioId(usuarioId).stream()
                .map(this::convertirADto)
                .toList();
    }

    // Guardar un nuevo proyecto vinculándolo al usuario autenticado
    public ProyectoResponseDTO crearProyecto(ProyectoRequestDTO dto, String emailUsuario) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + emailUsuario));

        ProyectoEntity proyecto = new ProyectoEntity();
        proyecto.setNombreProyecto(dto.nombreProyecto());
        proyecto.setDescripcion(dto.descripcion());
        proyecto.setPresupuesto(dto.presupuesto());
        proyecto.setEstado(dto.estado() != null && !dto.estado().isBlank() ? dto.estado() : "PENDIENTE");
        proyecto.setUsuario(usuario);

        ProyectoEntity guardado = proyectoRepository.save(proyecto);
        return convertirADto(guardado);
    }

    public ProyectoResponseDTO actualizarProyecto(Long id, ProyectoRequestDTO dto) {
        ProyectoEntity proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));

        proyecto.setNombreProyecto(dto.nombreProyecto());
        proyecto.setDescripcion(dto.descripcion());
        proyecto.setPresupuesto(dto.presupuesto());
        if (dto.estado() != null && !dto.estado().isBlank()) {
            proyecto.setEstado(dto.estado());
        }

        ProyectoEntity actualizado = proyectoRepository.save(proyecto);
        return convertirADto(actualizado);
    }

    public void eliminarProyecto(Long id) {
        ProyectoEntity proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
        proyectoRepository.delete(proyecto);
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
