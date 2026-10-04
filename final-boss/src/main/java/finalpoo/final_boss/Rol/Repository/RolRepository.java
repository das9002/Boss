package finalpoo.final_boss.Rol.Repository;

import finalpoo.final_boss.Rol.Entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {

    Optional<RolEntity> findByNombreRol(String nombreRol);
}
