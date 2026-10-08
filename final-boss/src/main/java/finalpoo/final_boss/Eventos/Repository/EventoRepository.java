package finalpoo.final_boss.Eventos.Repository;

import finalpoo.final_boss.Eventos.Entity.EventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<EventoEntity, Long> {
    boolean existsByCliente_IdClienteAndNombreEventoAndFechaEvento(Long idCliente, String nombreEvento, LocalDate fechaEvento);
}
