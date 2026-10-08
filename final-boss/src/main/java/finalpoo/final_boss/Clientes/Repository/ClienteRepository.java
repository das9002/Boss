package finalpoo.final_boss.Clientes.Repository;

import finalpoo.final_boss.Clientes.Entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    boolean existsByEmail(String email);
    Optional<ClienteEntity> findByEmail(String email);
}
