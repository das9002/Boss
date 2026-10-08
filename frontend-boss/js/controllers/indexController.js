import { eventosService } from '../services/eventosService.js';
import { clientesService } from '../services/clientesService.js';
import { salonesService } from '../services/salonesService.js';

document.addEventListener('DOMContentLoaded', () => {
    const tableBody = document.getElementById('eventos-table-body');
    const eventoForm = document.getElementById('evento-form');
    const modalElement = document.getElementById('eventoModal');
    const modalTitle = document.getElementById('modalTitle');
    const alertBox = document.getElementById('alert-message');

    const selectCliente = document.getElementById('idCliente');
    const selectSalon = document.getElementById('idSalon');

    let bsModal = null;
    if (modalElement && window.bootstrap) {
        bsModal = new bootstrap.Modal(modalElement);
    }

    function showAlert(message, type = 'success') {
        if (!alertBox) return;
        alertBox.className = `alert alert-${type} alert-dismissible fade show`;
        alertBox.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
        alertBox.classList.remove('d-none');
        setTimeout(() => {
            alertBox.classList.add('d-none');
        }, 6000);
    }

    async function cargarCombos() {
        try {
            const [clientes, salones] = await Promise.all([
                clientesService.obtenerTodos(),
                salonesService.obtenerTodos()
            ]);

            if (selectCliente) {
                selectCliente.innerHTML = '<option value="">Seleccione un cliente...</option>';
                clientes.forEach(c => {
                    selectCliente.innerHTML += `<option value="${c.idCliente}">${c.nombre} ${c.apellido}</option>`;
                });
            }

            if (selectSalon) {
                selectSalon.innerHTML = '<option value="">Seleccione un salón...</option>';
                salones.forEach(s => {
                    selectSalon.innerHTML += `<option value="${s.idSalon}">${s.nombreSalon} (Cap: ${s.capacidad}, $${Number(s.precioRenta).toFixed(2)}/h)</option>`;
                });
            }
        } catch (error) {
            showAlert('Error al cargar opciones de clientes y salones: ' + error.message, 'danger');
        }
    }

    async function cargarEventos() {
        try {
            const eventos = await eventosService.obtenerTodos();
            tableBody.innerHTML = '';
            if (eventos.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="10" class="text-center text-muted">No hay eventos registrados.</td></tr>';
                return;
            }
            eventos.forEach(evento => {
                const tr = document.createElement('tr');
                const badgeClass = evento.estado === 'CONFIRMADO' ? 'bg-primary' :
                                   evento.estado === 'FINALIZADO' ? 'bg-success' :
                                   evento.estado === 'CANCELADO' ? 'bg-danger' : 'bg-warning text-dark';
                tr.innerHTML = `
                    <td>${evento.idEvento}</td>
                    <td>${evento.nombreEvento}</td>
                    <td>${evento.nombreCliente || ('ID: ' + evento.idCliente)}</td>
                    <td>${evento.nombreSalon || ('ID: ' + evento.idSalon)}</td>
                    <td>${evento.fechaEvento}</td>
                    <td>${evento.cantidadPersonas}</td>
                    <td>${evento.cantidadHoras} hrs</td>
                    <td><span class="badge ${badgeClass}">${evento.estado}</span></td>
                    <td class="fw-bold">$${Number(evento.totalPago).toFixed(2)}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary btn-edit" data-id="${evento.idEvento}">Editar</button>
                        <button class="btn btn-sm btn-outline-danger btn-delete" data-id="${evento.idEvento}">Eliminar</button>
                    </td>
                `;
                tableBody.appendChild(tr);
            });

            document.querySelectorAll('.btn-edit').forEach(btn => {
                btn.addEventListener('click', (e) => editarEvento(e.target.getAttribute('data-id')));
            });

            document.querySelectorAll('.btn-delete').forEach(btn => {
                btn.addEventListener('click', (e) => eliminarEvento(e.target.getAttribute('data-id')));
            });
        } catch (error) {
            showAlert(error.message, 'danger');
        }
    }

    async function editarEvento(id) {
        try {
            await cargarCombos();
            const evento = await eventosService.obtenerPorId(id);
            document.getElementById('evento-id').value = evento.idEvento;
            document.getElementById('idCliente').value = evento.idCliente;
            document.getElementById('idSalon').value = evento.idSalon;
            document.getElementById('nombreEvento').value = evento.nombreEvento;
            document.getElementById('fechaEvento').value = evento.fechaEvento;
            document.getElementById('cantidadPersonas').value = evento.cantidadPersonas;
            document.getElementById('cantidadHoras').value = evento.cantidadHoras;
            document.getElementById('estado').value = evento.estado;

            if (modalTitle) modalTitle.textContent = 'Editar Evento';
            if (bsModal) bsModal.show();
        } catch (error) {
            showAlert(error.message, 'danger');
        }
    }

    async function eliminarEvento(id) {
        if (confirm('¿Está seguro de que desea eliminar este evento?')) {
            try {
                await eventosService.eliminar(id);
                showAlert('Evento eliminado correctamente.', 'success');
                cargarEventos();
            } catch (error) {
                showAlert(error.message, 'danger');
            }
        }
    }

    document.getElementById('btn-nuevo-evento')?.addEventListener('click', async () => {
        await cargarCombos();
        eventoForm.reset();
        document.getElementById('evento-id').value = '';
        document.getElementById('fechaEvento').value = new Date().toISOString().split('T')[0];
        if (modalTitle) modalTitle.textContent = 'Nuevo Evento';
        if (bsModal) bsModal.show();
    });

    eventoForm?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = document.getElementById('evento-id').value;
        const payload = {
            idCliente: parseInt(document.getElementById('idCliente').value, 10),
            idSalon: parseInt(document.getElementById('idSalon').value, 10),
            nombreEvento: document.getElementById('nombreEvento').value,
            fechaEvento: document.getElementById('fechaEvento').value,
            cantidadPersonas: parseInt(document.getElementById('cantidadPersonas').value, 10),
            cantidadHoras: parseInt(document.getElementById('cantidadHoras').value, 10),
            estado: document.getElementById('estado').value
        };

        try {
            if (id) {
                await eventosService.actualizar(id, payload);
                showAlert('Evento actualizado exitosamente.', 'success');
            } else {
                await eventosService.crear(payload);
                showAlert('Evento creado exitosamente con el cálculo automático de pago.', 'success');
            }
            if (bsModal) bsModal.hide();
            cargarEventos();
        } catch (error) {
            showAlert(error.message, 'danger');
        }
    });

    cargarEventos();
});
