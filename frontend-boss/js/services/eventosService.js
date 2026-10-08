const API_EVENTOS_URL = 'http://localhost:8080/api/eventos';

export const eventosService = {
    async obtenerTodos() {
        const response = await fetch(API_EVENTOS_URL);
        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.error || 'Error al obtener eventos');
        }
        return await response.json();
    },

    async obtenerPorId(id) {
        const response = await fetch(`${API_EVENTOS_URL}/${id}`);
        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.error || 'Error al obtener evento');
        }
        return await response.json();
    },

    async crear(eventoData) {
        const response = await fetch(API_EVENTOS_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(eventoData)
        });
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            throw new Error(data.error || Object.values(data).join(', ') || 'Error al crear evento');
        }
        return data;
    },

    async actualizar(id, eventoData) {
        const response = await fetch(`${API_EVENTOS_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(eventoData)
        });
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            throw new Error(data.error || Object.values(data).join(', ') || 'Error al actualizar evento');
        }
        return data;
    },

    async eliminar(id) {
        const response = await fetch(`${API_EVENTOS_URL}/${id}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.error || 'Error al eliminar evento');
        }
    }
};
