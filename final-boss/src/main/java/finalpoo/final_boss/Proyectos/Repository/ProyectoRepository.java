package finalpoo.final_boss.Proyectos.Repository;

import finalpoo.final_boss.Proyectos.Entity.ProyectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<ProyectoEntity, Long> {

    List<ProyectoEntity> findByUsuarioUsuarioId(Long usuarioId);
}
