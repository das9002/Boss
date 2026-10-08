package finalpoo.final_boss.Salones.Controller;

import finalpoo.final_boss.Salones.DTO.SalonRequestDTO;
import finalpoo.final_boss.Salones.DTO.SalonResponseDTO;
import finalpoo.final_boss.Salones.Service.SalonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salones")
public class SalonController {

    private final SalonService salonService;

    public SalonController(SalonService salonService) {
        this.salonService = salonService;
    }

    @GetMapping
    public ResponseEntity<List<SalonResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(salonService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalonResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(salonService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<SalonResponseDTO> crear(@Valid @RequestBody SalonRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salonService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalonResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody SalonRequestDTO dto) {
        return ResponseEntity.ok(salonService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        salonService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
