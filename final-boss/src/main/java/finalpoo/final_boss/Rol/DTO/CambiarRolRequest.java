package finalpoo.final_boss.Rol.DTO;

import jakarta.validation.constraints.NotBlank;

public record CambiarRolRequest(
        @NotBlank(message = "El nuevo rol es obligatorio")
        String nuevoRol
) {}
