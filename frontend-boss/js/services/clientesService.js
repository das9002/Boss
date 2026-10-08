const API_CLIENTES_URL = 'http://localhost:8080/api/clientes';

export const clientesService = {
    async obtenerTodos() {
        const response = await fetch(API_CLIENTES_URL);
        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.error || 'Error al obtener clientes');
        }
        return await response.json();
    },

    async obtenerPorId(id) {
        const response = await fetch(`${API_CLIENTES_URL}/${id}`);
        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.error || 'Error al obtener cliente');
        }
        return await response.json();
    },

    async crear(clienteData) {
        const response = await fetch(API_CLIENTES_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(clienteData)
        });
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            throw new Error(data.error || Object.values(data).join(', ') || 'Error al crear cliente');
        }
        return data;
    },

    async actualizar(id, clienteData) {
        const response = await fetch(`${API_CLIENTES_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(clienteData)
        });
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            throw new Error(data.error || Object.values(data).join(', ') || 'Error al actualizar cliente');
        }
        return data;
    },

    async eliminar(id) {
        const response = await fetch(`${API_CLIENTES_URL}/${id}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.error || 'Error al eliminar cliente');
        }
    }
};
