package finalpoo.final_boss.Usuarios.Controller;

import finalpoo.final_boss.Rol.DTO.CambiarRolRequest;
import finalpoo.final_boss.Usuarios.Entity.UsuarioEntity;
import finalpoo.final_boss.Usuarios.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // 1. Declarar la instancia inyectada
    private final UsuarioService usuarioService;

    // 2. Inyectar mediante el constructor
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<?> cambiarRol(
            @PathVariable Long id,
            @Valid @RequestBody CambiarRolRequest request) {

        // 3. Llamar a la instancia 'usuarioService' (en minúscula)
        usuarioService.cambiarRolUsuario(id, request.nuevoRol());
        return ResponseEntity.ok(Map.of("mensaje", "Rol actualizado exitosamente"));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }
}