import { clientesService } from '../services/clientesService.js';

document.addEventListener('DOMContentLoaded', () => {
    const tableBody = document.getElementById('clientes-table-body');
    const clienteForm = document.getElementById('cliente-form');
    const modalElement = document.getElementById('clienteModal');
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

    async function cargarClientes() {
        try {
            const clientes = await clientesService.obtenerTodos();
            tableBody.innerHTML = '';
            if (clientes.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No hay clientes registrados.</td></tr>';
                return;
            }
            clientes.forEach(cliente => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${cliente.idCliente}</td>
                    <td>${cliente.nombre} ${cliente.apellido}</td>
                    <td>${cliente.telefono}</td>
                    <td>${cliente.email}</td>
                    <td>${cliente.direccion}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary btn-edit" data-id="${cliente.idCliente}">Editar</button>
                        <button class="btn btn-sm btn-outline-danger btn-delete" data-id="${cliente.idCliente}">Eliminar</button>
                    </td>
                `;
                tableBody.appendChild(tr);
            });

            document.querySelectorAll('.btn-edit').forEach(btn => {
                btn.addEventListener('click', (e) => editarCliente(e.target.getAttribute('data-id')));
            });

            document.querySelectorAll('.btn-delete').forEach(btn => {
                btn.addEventListener('click', (e) => eliminarCliente(e.target.getAttribute('data-id')));
            });
        } catch (error) {
            showAlert(error.message, 'danger');
        }
    }

    async function editarCliente(id) {
        try {
            const cliente = await clientesService.obtenerPorId(id);
            document.getElementById('cliente-id').value = cliente.idCliente;
            document.getElementById('nombre').value = cliente.nombre;
            document.getElementById('apellido').value = cliente.apellido;
            document.getElementById('telefono').value = cliente.telefono;
            document.getElementById('email').value = cliente.email;
            document.getElementById('direccion').value = cliente.direccion;

            if (modalTitle) modalTitle.textContent = 'Editar Cliente';
            if (bsModal) bsModal.show();
        } catch (error) {
            showAlert(error.message, 'danger');
        }
    }

    async function eliminarCliente(id) {
        if (confirm('¿Está seguro de que desea eliminar este cliente?')) {
            try {
                await clientesService.eliminar(id);
                showAlert('Cliente eliminado correctamente.', 'success');
                cargarClientes();
            } catch (error) {
                showAlert(error.message, 'danger');
            }
        }
    }

    document.getElementById('btn-nuevo-cliente')?.addEventListener('click', () => {
        clienteForm.reset();
        document.getElementById('cliente-id').value = '';
        if (modalTitle) modalTitle.textContent = 'Nuevo Cliente';
        if (bsModal) bsModal.show();
    });

    clienteForm?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = document.getElementById('cliente-id').value;
        const payload = {
            nombre: document.getElementById('nombre').value,
            apellido: document.getElementById('apellido').value,
            telefono: document.getElementById('telefono').value,
            email: document.getElementById('email').value,
            direccion: document.getElementById('direccion').value
        };

        try {
            if (id) {
                await clientesService.actualizar(id, payload);
                showAlert('Cliente actualizado exitosamente.', 'success');
            } else {
                await clientesService.crear(payload);
                showAlert('Cliente creado exitosamente.', 'success');
            }
            if (bsModal) bsModal.hide();
            cargarClientes();
        } catch (error) {
            showAlert(error.message, 'danger');
        }
    });

    cargarClientes();
});
