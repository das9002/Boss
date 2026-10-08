package finalpoo.final_boss.Eventos.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EventoResponseDTO(
        Long idEvento,
        Long idCliente,
        String nombreCliente,
        Long idSalon,
        String nombreSalon,
        String nombreEvento,
        LocalDate fechaEvento,
        Integer cantidadPersonas,
        Integer cantidadHoras,
        String estado,
        BigDecimal totalPago
) {
}
