package finalpoo.final_boss.Eventos.Entity;

import finalpoo.final_boss.Clientes.Entity.ClienteEntity;
import finalpoo.final_boss.Salones.Entity.SalonEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "eventos")
public class EventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long idEvento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_salon", nullable = false)
    private SalonEntity salon;

    @Column(name = "nombre_evento", nullable = false, length = 100)
    private String nombreEvento;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDate fechaEvento = LocalDate.now();

    @Column(name = "cantidad_personas", nullable = false)
    private Integer cantidadPersonas = 1;

    @Column(name = "cantidad_horas", nullable = false)
    private Integer cantidadHoras = 1;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "total_pago", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalPago = BigDecimal.ZERO;

    public EventoEntity() {
    }

    public EventoEntity(Long idEvento, ClienteEntity cliente, SalonEntity salon, String nombreEvento, LocalDate fechaEvento, Integer cantidadPersonas, Integer cantidadHoras, String estado, BigDecimal totalPago) {
        this.idEvento = idEvento;
        this.cliente = cliente;
        this.salon = salon;
        this.nombreEvento = nombreEvento;
        this.fechaEvento = fechaEvento;
        this.cantidadPersonas = cantidadPersonas;
        this.cantidadHoras = cantidadHoras;
        this.estado = estado;
        this.totalPago = totalPago;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public SalonEntity getSalon() {
        return salon;
    }

    public void setSalon(SalonEntity salon) {
        this.salon = salon;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public LocalDate getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public Integer getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(Integer cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public Integer getCantidadHoras() {
        return cantidadHoras;
    }

    public void setCantidadHoras(Integer cantidadHoras) {
        this.cantidadHoras = cantidadHoras;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }
}
