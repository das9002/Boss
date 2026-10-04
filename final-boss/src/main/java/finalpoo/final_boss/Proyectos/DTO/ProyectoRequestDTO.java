package finalpoo.final_boss.Proyectos.DTO;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProyectoRequestDTO (

        @NotBlank(message = "El nombre del proyecto es obligatorio")
        String nombreProyecto,

        String descripcion,

        @Positive(message = "El presupuesto debe ser mayor a cero")
        BigDecimal presupuesto,

        String estado
) {}