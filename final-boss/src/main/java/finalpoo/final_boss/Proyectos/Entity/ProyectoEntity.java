package finalpoo.final_boss.Proyectos.Entity;

import finalpoo.final_boss.Usuarios.Entity.UsuarioEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_PROYECTOS")
public class ProyectoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROYECTO_ID")
    private Long proyectoId;

    @Column(name = "NOMBRE_PROYECTO", nullable = false, length = 100)
    private String nombreProyecto;

    @Column(length = 255)
    private String descripcion;

    @Column(precision = 10, scale = 2)
    private BigDecimal presupuesto;

    @Column(length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID")
    private UsuarioEntity usuario;

    public ProyectoEntity() {}

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }
}
