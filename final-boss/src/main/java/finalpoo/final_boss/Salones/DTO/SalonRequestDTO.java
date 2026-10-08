package finalpoo.final_boss.Salones.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SalonRequestDTO(
        @NotBlank(message = "El nombre del salón es obligatorio")
        String nombreSalon,

        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad debe ser mayor a 0")
        Integer capacidad,

        @NotNull(message = "El precio de renta es obligatorio")
        @Min(value = 0, message = "El precio de renta no puede ser negativo")
        BigDecimal precioRenta,

        String ubicacion
) {
}
