const API_SALONES_URL = 'http://localhost:8080/api/salones';

export const salonesService = {
    async obtenerTodos() {
        const response = await fetch(API_SALONES_URL);
        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.error || 'Error al obtener salones');
        }
        return await response.json();
    },

    async obtenerPorId(id) {
        const response = await fetch(`${API_SALONES_URL}/${id}`);
        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.error || 'Error al obtener salón');
        }
        return await response.json();
    },

    async crear(salonData) {
        const response = await fetch(API_SALONES_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(salonData)
        });
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            throw new Error(data.error || Object.values(data).join(', ') || 'Error al crear salón');
        }
        return data;
    },

    async actualizar(id, salonData) {
        const response = await fetch(`${API_SALONES_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(salonData)
        });
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            throw new Error(data.error || Object.values(data).join(', ') || 'Error al actualizar salón');
        }
        return data;
    },

    async eliminar(id) {
        const response = await fetch(`${API_SALONES_URL}/${id}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.error || 'Error al eliminar salón');
        }
    }
};
