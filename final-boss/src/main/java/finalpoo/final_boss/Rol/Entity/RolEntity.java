package finalpoo.final_boss.Rol.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TB_ROLES")
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROL_ID")
    private Long rolId;

    @Column(name = "NOMBRE_ROL", nullable = false, unique = true, length = 30)
    private String nombreRol;

    public RolEntity() {}

    public RolEntity(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public RolEntity(Long rolId, String nombreRol) {
        this.rolId = rolId;
        this.nombreRol = nombreRol;
    }

    public Long getRolId() {
        return rolId;
    }
    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}
