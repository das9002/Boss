package finalpoo.final_boss.Eventos.Service;

import finalpoo.final_boss.Clientes.Entity.ClienteEntity;
import finalpoo.final_boss.Clientes.Repository.ClienteRepository;
import finalpoo.final_boss.Eventos.DTO.EventoRequestDTO;
import finalpoo.final_boss.Eventos.DTO.EventoResponseDTO;
import finalpoo.final_boss.Eventos.Entity.EventoEntity;
import finalpoo.final_boss.Eventos.Repository.EventoRepository;
import finalpoo.final_boss.Salones.Entity.SalonEntity;
import finalpoo.final_boss.Salones.Repository.SalonRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final ClienteRepository clienteRepository;
    private final SalonRepository salonRepository;

    public EventoService(EventoRepository eventoRepository, ClienteRepository clienteRepository, SalonRepository salonRepository) {
        this.eventoRepository = eventoRepository;
        this.clienteRepository = clienteRepository;
        this.salonRepository = salonRepository;
    }

    public List<EventoResponseDTO> obtenerTodos() {
        return eventoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public EventoResponseDTO obtenerPorId(Long id) {
        EventoEntity entity = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));
        return mapToDTO(entity);
    }

    public EventoResponseDTO crear(EventoRequestDTO dto) {
        ClienteEntity cliente = clienteRepository.findById(dto.idCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + dto.idCliente()));

        SalonEntity salon = salonRepository.findById(dto.idSalon())
                .orElseThrow(() -> new RuntimeException("Salón no encontrado con id: " + dto.idSalon()));

        if (dto.cantidadPersonas() > salon.getCapacidad()) {
            throw new RuntimeException("La cantidad de personas (" + dto.cantidadPersonas() + ") supera la capacidad máxima del salón (" + salon.getCapacidad() + ").");
        }

        if (eventoRepository.existsByCliente_IdClienteAndNombreEventoAndFechaEvento(dto.idCliente(), dto.nombreEvento(), dto.fechaEvento())) {
            throw new RuntimeException("Ya existe un evento registrado con el mismo cliente, nombre y fecha.");
        }

        BigDecimal totalCalculado = salon.getPrecioRenta().multiply(BigDecimal.valueOf(dto.cantidadHoras()));

        EventoEntity entity = new EventoEntity();
        entity.setCliente(cliente);
        entity.setSalon(salon);
        entity.setNombreEvento(dto.nombreEvento());
        entity.setFechaEvento(dto.fechaEvento());
        entity.setCantidadPersonas(dto.cantidadPersonas());
        entity.setCantidadHoras(dto.cantidadHoras());
        entity.setEstado(dto.estado() != null ? dto.estado() : "PENDIENTE");
        entity.setTotalPago(totalCalculado);

        EventoEntity guardado = eventoRepository.save(entity);
        return mapToDTO(guardado);
    }

    public EventoResponseDTO actualizar(Long id, EventoRequestDTO dto) {
        EventoEntity entity = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));

        ClienteEntity cliente = clienteRepository.findById(dto.idCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + dto.idCliente()));

        SalonEntity salon = salonRepository.findById(dto.idSalon())
                .orElseThrow(() -> new RuntimeException("Salón no encontrado con id: " + dto.idSalon()));

        if (dto.cantidadPersonas() > salon.getCapacidad()) {
            throw new RuntimeException("La cantidad de personas (" + dto.cantidadPersonas() + ") supera la capacidad máxima del salón (" + salon.getCapacidad() + ").");
        }

        boolean cambioLlaveUnica = !entity.getCliente().getIdCliente().equals(dto.idCliente())
                || !entity.getNombreEvento().equalsIgnoreCase(dto.nombreEvento())
                || !entity.getFechaEvento().equals(dto.fechaEvento());

        if (cambioLlaveUnica && eventoRepository.existsByCliente_IdClienteAndNombreEventoAndFechaEvento(dto.idCliente(), dto.nombreEvento(), dto.fechaEvento())) {
            throw new RuntimeException("Ya existe un evento registrado con el mismo cliente, nombre y fecha.");
        }

        BigDecimal totalCalculado = salon.getPrecioRenta().multiply(BigDecimal.valueOf(dto.cantidadHoras()));

        entity.setCliente(cliente);
        entity.setSalon(salon);
        entity.setNombreEvento(dto.nombreEvento());
        entity.setFechaEvento(dto.fechaEvento());
        entity.setCantidadPersonas(dto.cantidadPersonas());
        entity.setCantidadHoras(dto.cantidadHoras());
        if (dto.estado() != null) {
            entity.setEstado(dto.estado());
        }
        entity.setTotalPago(totalCalculado);

        EventoEntity actualizado = eventoRepository.save(entity);
        return mapToDTO(actualizado);
    }

    public void eliminar(Long id) {
        EventoEntity entity = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));
        eventoRepository.delete(entity);
    }

    private EventoResponseDTO mapToDTO(EventoEntity entity) {
        String nombreCliente = entity.getCliente() != null
                ? entity.getCliente().getNombre() + " " + entity.getCliente().getApellido()
                : null;
        String nombreSalon = entity.getSalon() != null
                ? entity.getSalon().getNombreSalon()
                : null;

        return new EventoResponseDTO(
                entity.getIdEvento(),
                entity.getCliente() != null ? entity.getCliente().getIdCliente() : null,
                nombreCliente,
                entity.getSalon() != null ? entity.getSalon().getIdSalon() : null,
                nombreSalon,
                entity.getNombreEvento(),
                entity.getFechaEvento(),
                entity.getCantidadPersonas(),
                entity.getCantidadHoras(),
                entity.getEstado(),
                entity.getTotalPago()
        );
    }
}
