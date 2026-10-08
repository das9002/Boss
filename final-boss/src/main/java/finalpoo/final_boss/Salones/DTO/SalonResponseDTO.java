package finalpoo.final_boss.Salones.DTO;

import java.math.BigDecimal;

public record SalonResponseDTO(
        Long idSalon,
        String nombreSalon,
        Integer capacidad,
        BigDecimal precioRenta,
        String ubicacion
) {
}
