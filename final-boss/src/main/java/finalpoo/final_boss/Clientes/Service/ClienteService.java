package finalpoo.final_boss.Clientes.Service;

import finalpoo.final_boss.Clientes.DTO.ClienteRequestDTO;
import finalpoo.final_boss.Clientes.DTO.ClienteResponseDTO;
import finalpoo.final_boss.Clientes.Entity.ClienteEntity;
import finalpoo.final_boss.Clientes.Repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponseDTO> obtenerTodos() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO obtenerPorId(Long id) {
        ClienteEntity entity = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        return mapToDTO(entity);
    }

    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("El email ya se encuentra registrado: " + dto.email());
        }
        ClienteEntity entity = new ClienteEntity();
        entity.setNombre(dto.nombre());
        entity.setApellido(dto.apellido());
        entity.setTelefono(dto.telefono());
        entity.setEmail(dto.email());
        entity.setDireccion(dto.direccion() != null && !dto.direccion().trim().isEmpty() ? dto.direccion() : "SIN ESPECIFICAR");

        ClienteEntity guardado = clienteRepository.save(entity);
        return mapToDTO(guardado);
    }

    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        ClienteEntity entity = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        if (!entity.getEmail().equalsIgnoreCase(dto.email()) && clienteRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("El email ya se encuentra registrado: " + dto.email());
        }

        entity.setNombre(dto.nombre());
        entity.setApellido(dto.apellido());
        entity.setTelefono(dto.telefono());
        entity.setEmail(dto.email());
        if (dto.direccion() != null && !dto.direccion().trim().isEmpty()) {
            entity.setDireccion(dto.direccion());
        }

        ClienteEntity actualizado = clienteRepository.save(entity);
        return mapToDTO(actualizado);
    }

    public void eliminar(Long id) {
        ClienteEntity entity = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        clienteRepository.delete(entity);
    }

    private ClienteResponseDTO mapToDTO(ClienteEntity entity) {
        return new ClienteResponseDTO(
                entity.getIdCliente(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getTelefono(),
                entity.getEmail(),
                entity.getDireccion()
        );
    }
}
