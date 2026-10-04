package finalpoo.final_boss.Proyectos.Controller;

import finalpoo.final_boss.Proyectos.DTO.ProyectoRequestDTO;
import finalpoo.final_boss.Proyectos.DTO.ProyectoResponseDTO;
import finalpoo.final_boss.Proyectos.Service.ProyectoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<List<ProyectoResponseDTO>> listarProyectos() {
        return ResponseEntity.ok(proyectoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> obtenerProyectoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ProyectoResponseDTO>> listarProyectosPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(proyectoService.obtenerPorUsuarioId(usuarioId));
    }

    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> crearProyecto(
            @Valid @RequestBody ProyectoRequestDTO dto,
            Authentication authentication){

        String emailUsuario = authentication.getName();

        ProyectoResponseDTO creado = proyectoService.crearProyecto(dto, emailUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> actualizarProyecto(
            @PathVariable Long id,
            @Valid @RequestBody ProyectoRequestDTO dto) {
        ProyectoResponseDTO actualizado = proyectoService.actualizarProyecto(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
        proyectoService.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }
}
