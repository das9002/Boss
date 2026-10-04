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

    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> crearProyecto(
            @Valid @RequestBody ProyectoRequestDTO dto,
            Authentication authentication){

        String emailUsusario = authentication.getName();

        ProyectoResponseDTO creado = proyectoService.crearProyecto(dto, emailUsusario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
