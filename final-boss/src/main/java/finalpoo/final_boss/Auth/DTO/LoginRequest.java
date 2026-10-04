package finalpoo.final_boss.Auth.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato de email no es valido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}
