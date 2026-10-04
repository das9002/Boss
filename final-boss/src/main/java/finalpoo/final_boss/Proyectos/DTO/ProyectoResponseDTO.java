package finalpoo.final_boss.Proyectos.DTO;

import java.math.BigDecimal;

public record ProyectoResponseDTO(

        Long proyectoId,
        String nombreProyecto,
        String descripcion,
        BigDecimal presupuesto,
        String estado,
        String nombreUsuarioResponsable
) {}