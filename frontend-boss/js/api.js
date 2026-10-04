const API_BASE_URL = 'http://localhost:8080/api';

class ApiService {
    static getToken() {
        return localStorage.getItem('jwt_token');
    }

    static setToken(token) {
        localStorage.setItem('jwt_token', token);
    }

    static clearToken() {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_info');
    }

    static setUserInfo(user) {
        localStorage.setItem('user_info', JSON.stringify(user));
    }

    static getUserInfo() {
        const info = localStorage.getItem('user_info');
        return info ? JSON.parse(info) : null;
    }

    static async request(endpoint, options = {}) {
        const token = this.getToken();
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const config = {
            ...options,
            headers
        };

        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, config);

            if (response.status === 401 || response.status === 403) {
                // Si la sesión expira o es inválida
                this.clearToken();
                window.dispatchEvent(new Event('auth-expired'));
                throw new Error('Sesión expirada o no autorizada');
            }

            if (response.status === 204) {
                return null;
            }

            const data = await response.json();

            if (!response.ok) {
                const errorMsg = data.error || (typeof data === 'object' ? JSON.stringify(data) : 'Error en la petición');
                throw new Error(errorMsg);
            }

            return data;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    }

    // Auth
    static async login(email, password) {
        const data = await this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
        this.setToken(data.token);
        this.setUserInfo(data);
        return data;
    }

    static async register(nombre, email, password, rol = 'ROLE_USER') {
        const data = await this.request('/auth/register', {
            method: 'POST',
            body: JSON.stringify({ nombre, email, password, rol })
        });
        this.setToken(data.token);
        this.setUserInfo(data);
        return data;
    }

    // Proyectos
    static async getProyectos() {
        return await this.request('/proyectos');
    }

    static async getProyectoById(id) {
        return await this.request(`/proyectos/${id}`);
    }

    static async getProyectosByUsuario(usuarioId) {
        return await this.request(`/proyectos/usuario/${usuarioId}`);
    }

    static async createProyecto(proyectoData) {
        return await this.request('/proyectos', {
            method: 'POST',
            body: JSON.stringify(proyectoData)
        });
    }

    static async updateProyecto(id, proyectoData) {
        return await this.request(`/proyectos/${id}`, {
            method: 'PUT',
            body: JSON.stringify(proyectoData)
        });
    }

    static async deleteProyecto(id) {
        return await this.request(`/proyectos/${id}`, {
            method: 'DELETE'
        });
    }

    // Usuarios
    static async getUsuarios() {
        return await this.request('/usuarios');
    }

    static async getUsuarioById(id) {
        return await this.request(`/usuarios/${id}`);
    }

    static async cambiarRol(usuarioId, nuevoRol) {
        return await this.request(`/usuarios/${usuarioId}/rol`, {
            method: 'PUT',
            body: JSON.stringify({ nuevoRol })
        });
    }
}
