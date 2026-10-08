package finalpoo.final_boss.Salones.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "salones")
public class SalonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salon")
    private Long idSalon;

    @Column(name = "nombre_salon", nullable = false, unique = true, length = 100)
    private String nombreSalon;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad = 1;

    @Column(name = "precio_renta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioRenta = BigDecimal.ZERO;

    @Column(name = "ubicacion", nullable = false, length = 100)
    private String ubicacion = "SIN ESPECIFICAR";

    public SalonEntity() {
    }

    public SalonEntity(Long idSalon, String nombreSalon, Integer capacidad, BigDecimal precioRenta, String ubicacion) {
        this.idSalon = idSalon;
        this.nombreSalon = nombreSalon;
        this.capacidad = capacidad;
        this.precioRenta = precioRenta;
        this.ubicacion = ubicacion;
    }

    public Long getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(Long idSalon) {
        this.idSalon = idSalon;
    }

    public String getNombreSalon() {
        return nombreSalon;
    }

    public void setNombreSalon(String nombreSalon) {
        this.nombreSalon = nombreSalon;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public BigDecimal getPrecioRenta() {
        return precioRenta;
    }

    public void setPrecioRenta(BigDecimal precioRenta) {
        this.precioRenta = precioRenta;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
