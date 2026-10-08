import { salonesService } from '../services/salonesService.js';

document.addEventListener('DOMContentLoaded', () => {
    const tableBody = document.getElementById('salones-table-body');
    const salonForm = document.getElementById('salon-form');
    const modalElement = document.getElementById('salonModal');
    const modalTitle = document.getElementById('modalTitle');
    const alertBox = document.getElementById('alert-message');

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
        }, 5000);
    }

    async function cargarSalones() {
        try {
            const salones = await salonesService.obtenerTodos();
            tableBody.innerHTML = '';
            if (salones.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No hay salones registrados.</td></tr>';
                return;
            }
            salones.forEach(salon => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${salon.idSalon}</td>
                    <td>${salon.nombreSalon}</td>
                    <td>${salon.capacidad}</td>
                    <td>$${Number(salon.precioRenta).toFixed(2)}</td>
                    <td>${salon.ubicacion}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary btn-edit" data-id="${salon.idSalon}">Editar</button>
                        <button class="btn btn-sm btn-outline-danger btn-delete" data-id="${salon.idSalon}">Eliminar</button>
                    </td>
                `;
                tableBody.appendChild(tr);
            });

            document.querySelectorAll('.btn-edit').forEach(btn => {
                btn.addEventListener('click', (e) => editarSalon(e.target.getAttribute('data-id')));
            });

            document.querySelectorAll('.btn-delete').forEach(btn => {
                btn.addEventListener('click', (e) => eliminarSalon(e.target.getAttribute('data-id')));
            });
        } catch (error) {
            showAlert(error.message, 'danger');
        }
    }

    async function editarSalon(id) {
        try {
            const salon = await salonesService.obtenerPorId(id);
            document.getElementById('salon-id').value = salon.idSalon;
            document.getElementById('nombreSalon').value = salon.nombreSalon;
            document.getElementById('capacidad').value = salon.capacidad;
            document.getElementById('precioRenta').value = salon.precioRenta;
            document.getElementById('ubicacion').value = salon.ubicacion;

            if (modalTitle) modalTitle.textContent = 'Editar Salón';
            if (bsModal) bsModal.show();
        } catch (error) {
            showAlert(error.message, 'danger');
        }
    }

    async function eliminarSalon(id) {
        if (confirm('¿Está seguro de que desea eliminar este salón?')) {
            try {
                await salonesService.eliminar(id);
                showAlert('Salón eliminado correctamente.', 'success');
                cargarSalones();
            } catch (error) {
                showAlert(error.message, 'danger');
            }
        }
    }

    document.getElementById('btn-nuevo-salon')?.addEventListener('click', () => {
        salonForm.reset();
        document.getElementById('salon-id').value = '';
        if (modalTitle) modalTitle.textContent = 'Nuevo Salón';
        if (bsModal) bsModal.show();
    });

    salonForm?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = document.getElementById('salon-id').value;
        const payload = {
            nombreSalon: document.getElementById('nombreSalon').value,
            capacidad: parseInt(document.getElementById('capacidad').value, 10),
            precioRenta: parseFloat(document.getElementById('precioRenta').value),
            ubicacion: document.getElementById('ubicacion').value
        };

        try {
            if (id) {
                await salonesService.actualizar(id, payload);
                showAlert('Salón actualizado exitosamente.', 'success');
            } else {
                await salonesService.crear(payload);
                showAlert('Salón creado exitosamente.', 'success');
            }
            if (bsModal) bsModal.hide();
            cargarSalones();
        } catch (error) {
            showAlert(error.message, 'danger');
        }
    });

    cargarSalones();
});
