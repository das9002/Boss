package finalpoo.final_boss.Eventos.DTO;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EventoRequestDTO(
        @NotNull(message = "El id del cliente es obligatorio")
        Long idCliente,

        @NotNull(message = "El id del salón es obligatorio")
        Long idSalon,

        @NotBlank(message = "El nombre del evento es obligatorio")
        String nombreEvento,

        @NotNull(message = "La fecha del evento es obligatoria")
        LocalDate fechaEvento,

        @NotNull(message = "La cantidad de personas es obligatoria")
        @Min(value = 1, message = "La cantidad de personas debe ser al menos 1")
        Integer cantidadPersonas,

        @NotNull(message = "La cantidad de horas es obligatoria")
        @Min(value = 1, message = "La cantidad de horas debe ser al menos 1")
        @Max(value = 24, message = "La cantidad de horas no puede exceder 24")
        Integer cantidadHoras,

        @NotBlank(message = "El estado es obligatorio")
        @Pattern(regexp = "^(PENDIENTE|CONFIRMADO|CANCELADO|FINALIZADO)$", message = "El estado debe ser PENDIENTE, CONFIRMADO, CANCELADO o FINALIZADO")
        String estado,

        @NotNull(message = "El total de pago es obligatorio")
        @Min(value = 0, message = "El total de pago no puede ser negativo")
        BigDecimal totalPago
) {
}
