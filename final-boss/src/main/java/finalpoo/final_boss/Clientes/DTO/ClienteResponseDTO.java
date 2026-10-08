package finalpoo.final_boss.Clientes.DTO;

public record ClienteResponseDTO(
        Long idCliente,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String direccion
) {
}
