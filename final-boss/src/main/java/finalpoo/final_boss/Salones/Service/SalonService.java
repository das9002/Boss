package finalpoo.final_boss.Salones.Service;

import finalpoo.final_boss.Salones.DTO.SalonRequestDTO;
import finalpoo.final_boss.Salones.DTO.SalonResponseDTO;
import finalpoo.final_boss.Salones.Entity.SalonEntity;
import finalpoo.final_boss.Salones.Repository.SalonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalonService {

    private final SalonRepository salonRepository;

    public SalonService(SalonRepository salonRepository) {
        this.salonRepository = salonRepository;
    }

    public List<SalonResponseDTO> obtenerTodos() {
        return salonRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SalonResponseDTO obtenerPorId(Long id) {
        SalonEntity entity = salonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salón no encontrado con id: " + id));
        return mapToDTO(entity);
    }

    public SalonResponseDTO crear(SalonRequestDTO dto) {
        if (salonRepository.existsByNombreSalon(dto.nombreSalon())) {
            throw new RuntimeException("El nombre del salón ya existe: " + dto.nombreSalon());
        }
        SalonEntity entity = new SalonEntity();
        entity.setNombreSalon(dto.nombreSalon());
        entity.setCapacidad(dto.capacidad());
        entity.setPrecioRenta(dto.precioRenta());
        entity.setUbicacion(dto.ubicacion() != null && !dto.ubicacion().trim().isEmpty() ? dto.ubicacion() : "SIN ESPECIFICAR");

        SalonEntity guardado = salonRepository.save(entity);
        return mapToDTO(guardado);
    }

    public SalonResponseDTO actualizar(Long id, SalonRequestDTO dto) {
        SalonEntity entity = salonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salón no encontrado con id: " + id));

        if (!entity.getNombreSalon().equalsIgnoreCase(dto.nombreSalon()) && salonRepository.existsByNombreSalon(dto.nombreSalon())) {
            throw new RuntimeException("El nombre del salón ya existe: " + dto.nombreSalon());
        }

        entity.setNombreSalon(dto.nombreSalon());
        entity.setCapacidad(dto.capacidad());
        entity.setPrecioRenta(dto.precioRenta());
        if (dto.ubicacion() != null && !dto.ubicacion().trim().isEmpty()) {
            entity.setUbicacion(dto.ubicacion());
        }

        SalonEntity actualizado = salonRepository.save(entity);
        return mapToDTO(actualizado);
    }

    public void eliminar(Long id) {
        SalonEntity entity = salonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salón no encontrado con id: " + id));
        salonRepository.delete(entity);
    }

    private SalonResponseDTO mapToDTO(SalonEntity entity) {
        return new SalonResponseDTO(
                entity.getIdSalon(),
                entity.getNombreSalon(),
                entity.getCapacidad(),
                entity.getPrecioRenta(),
                entity.getUbicacion()
        );
    }
}
