package finalpoo.final_boss.Auth.DTO;

public record AuthResponse(
        String token,
        String email,
        String nombre,
        String rol
) {}