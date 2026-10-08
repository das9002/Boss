package finalpoo.final_boss.Salones.Repository;

import finalpoo.final_boss.Salones.Entity.SalonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalonRepository extends JpaRepository<SalonEntity, Long> {
    boolean existsByNombreSalon(String nombreSalon);
    Optional<SalonEntity> findByNombreSalon(String nombreSalon);
}
