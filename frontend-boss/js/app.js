document.addEventListener('DOMContentLoaded', () => {
    // UI Elements
    const authSection = document.getElementById('auth-section');
    const mainSection = document.getElementById('main-section');
    const userHeader = document.getElementById('user-header');
    const currentUserName = document.getElementById('current-user-name');
    const currentUserRole = document.getElementById('current-user-role');
    const logoutBtn = document.getElementById('logout-btn');

    // Auth Forms
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const tabLogin = document.getElementById('tab-login');
    const tabRegister = document.getElementById('tab-register');
    const authAlert = document.getElementById('auth-alert');

    // Navigation
    const navProyectos = document.getElementById('nav-proyectos');
    const navUsuarios = document.getElementById('nav-usuarios');
    const viewProyectos = document.getElementById('view-proyectos');
    const viewUsuarios = document.getElementById('view-usuarios');

    // Proyectos Elements
    const proyectosTableBody = document.getElementById('proyectos-table-body');
    const btnNewProyecto = document.getElementById('btn-new-proyecto');
    const proyectoModal = document.getElementById('proyecto-modal');
    const proyectoForm = document.getElementById('proyecto-form');
    const closeModalBtn = document.getElementById('close-modal');
    const modalTitle = document.getElementById('modal-title');
    const proyectoIdInput = document.getElementById('proyecto-id');
    const inputNombreProyecto = document.getElementById('nombreProyecto');
    const inputDescripcionProyecto = document.getElementById('descripcion');
    const inputPresupuestoProyecto = document.getElementById('presupuesto');
    const selectEstadoProyecto = document.getElementById('estado');

    // Usuarios Elements
    const usuariosTableBody = document.getElementById('usuarios-table-body');
    const rolModal = document.getElementById('rol-modal');
    const rolForm = document.getElementById('rol-form');
    const closeRolModalBtn = document.getElementById('close-rol-modal');
    const rolUserIdInput = document.getElementById('rol-user-id');
    const selectNuevoRol = document.getElementById('nuevoRol');

    // App State
    let currentEditingProyectoId = null;

    // Check Auth State
    checkAuth();

    function checkAuth() {
        const user = ApiService.getUserInfo();
        if (user && ApiService.getToken()) {
            authSection.classList.add('hidden');
            mainSection.classList.remove('hidden');
            userHeader.classList.remove('hidden');
            currentUserName.textContent = user.nombre || user.email;
            currentUserRole.textContent = user.rol;

            loadProyectos();
            loadUsuarios();
        } else {
            authSection.classList.remove('hidden');
            mainSection.classList.add('hidden');
            userHeader.classList.add('hidden');
        }
    }

    // --- AUTH EVENTS ---
    tabLogin.addEventListener('click', () => {
        tabLogin.classList.add('active');
        tabRegister.classList.remove('active');
        loginForm.classList.remove('hidden');
        registerForm.classList.add('hidden');
        authAlert.classList.add('hidden');
    });

    tabRegister.addEventListener('click', () => {
        tabRegister.classList.add('active');
        tabLogin.classList.remove('active');
        registerForm.classList.remove('hidden');
        loginForm.classList.add('hidden');
        authAlert.classList.add('hidden');
    });

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = document.getElementById('login-email').value;
        const password = document.getElementById('login-password').value;

        try {
            await ApiService.login(email, password);
            Swal.fire({
                icon: 'success',
                title: '¡Bienvenido!',
                text: 'Inicio de sesión exitoso',
                timer: 1500,
                showConfirmButton: false
            });
            checkAuth();
            loginForm.reset();
        } catch (err) {
            showAuthAlert(err.message, 'error');
        }
    });

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const nombre = document.getElementById('reg-nombre').value;
        const email = document.getElementById('reg-email').value;
        const password = document.getElementById('reg-password').value;
        const rol = document.getElementById('reg-rol').value;

        try {
            await ApiService.register(nombre, email, password, rol);
            Swal.fire({
                icon: 'success',
                title: '¡Registro Exitoso!',
                text: 'Tu cuenta ha sido creada correctamente',
                timer: 1500,
                showConfirmButton: false
            });
            checkAuth();
            registerForm.reset();
        } catch (err) {
            showAuthAlert(err.message, 'error');
        }
    });

    logoutBtn.addEventListener('click', () => {
        Swal.fire({
            title: '¿Cerrar sesión?',
            text: 'Tendrás que ingresar tus credenciales de nuevo',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Sí, salir',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                ApiService.clearToken();
                checkAuth();
            }
        });
    });

    window.addEventListener('auth-expired', () => {
        checkAuth();
        showAuthAlert('Su sesión ha caducado. Inicie sesión nuevamente.', 'warning');
    });

    function showAuthAlert(msg, type = 'error') {
        Swal.fire({
            icon: type,
            title: type === 'error' ? 'Error' : 'Aviso',
            text: msg
        });
    }

    // --- NAVIGATION EVENTS ---
    navProyectos.addEventListener('click', (e) => {
        e.preventDefault();
        navProyectos.classList.add('active');
        navUsuarios.classList.remove('active');
        viewProyectos.classList.remove('hidden');
        viewUsuarios.classList.add('hidden');
        loadProyectos();
    });

    navUsuarios.addEventListener('click', (e) => {
        e.preventDefault();
        navUsuarios.classList.add('active');
        navProyectos.classList.remove('active');
        viewUsuarios.classList.remove('hidden');
        viewProyectos.classList.add('hidden');
        loadUsuarios();
    });

    // --- PROYECTOS LOGIC ---
    async function loadProyectos() {
        try {
            const proyectos = await ApiService.getProyectos();
            renderProyectos(proyectos);
        } catch (err) {
            console.error('Error al cargar proyectos:', err);
        }
    }

    function renderProyectos(proyectos) {
        proyectosTableBody.innerHTML = '';
        if (proyectos.length === 0) {
            proyectosTableBody.innerHTML = '<tr><td colspan="6" style="text-align:center;">No hay proyectos registrados</td></tr>';
            return;
        }

        proyectos.forEach(p => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${p.proyectoId}</td>
                <td><strong>${escapeHtml(p.nombreProyecto)}</strong></td>
                <td>${escapeHtml(p.descripcion || '-')}</td>
                <td>$${p.presupuesto ? Number(p.presupuesto).toFixed(2) : '0.00'}</td>
                <td><span class="status-badge status-${p.estado}">${escapeHtml(p.estado)}</span></td>
                <td>${escapeHtml(p.nombreUsuarioResponsable || 'Sin Asignar')}</td>
                <td>
                    <button class="btn btn-secondary btn-sm edit-btn" data-id="${p.proyectoId}">Editar</button>
                    <button class="btn btn-danger btn-sm delete-btn" data-id="${p.proyectoId}">Eliminar</button>
                </td>
            `;
            proyectosTableBody.appendChild(tr);
        });

        // Add event listeners for edit and delete
        document.querySelectorAll('.edit-btn').forEach(btn => {
            btn.addEventListener('click', (e) => openEditProyectoModal(e.target.dataset.id));
        });

        document.querySelectorAll('.delete-btn').forEach(btn => {
            btn.addEventListener('click', (e) => handleDeleteProyecto(e.target.dataset.id));
        });
    }

    btnNewProyecto.addEventListener('click', () => {
        currentEditingProyectoId = null;
        modalTitle.textContent = 'Nuevo Proyecto';
        proyectoForm.reset();
        proyectoModal.style.display = 'flex';
    });

    closeModalBtn.addEventListener('click', () => {
        proyectoModal.style.display = 'none';
    });

    async function openEditProyectoModal(id) {
        try {
            const p = await ApiService.getProyectoById(id);
            currentEditingProyectoId = p.proyectoId;
            modalTitle.textContent = 'Editar Proyecto';
            proyectoIdInput.value = p.proyectoId;
            inputNombreProyecto.value = p.nombreProyecto;
            inputDescripcionProyecto.value = p.descripcion || '';
            inputPresupuestoProyecto.value = p.presupuesto || '';
            selectEstadoProyecto.value = p.estado || 'PENDIENTE';

            proyectoModal.style.display = 'flex';
        } catch (err) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Error al cargar datos del proyecto: ' + err.message
            });
        }
    }

    proyectoForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const payload = {
            nombreProyecto: inputNombreProyecto.value,
            descripcion: inputDescripcionProyecto.value,
            presupuesto: parseFloat(inputPresupuestoProyecto.value),
            estado: selectEstadoProyecto.value
        };

        try {
            if (currentEditingProyectoId) {
                await ApiService.updateProyecto(currentEditingProyectoId, payload);
                Swal.fire({
                    icon: 'success',
                    title: '¡Actualizado!',
                    text: 'El proyecto se actualizó correctamente',
                    timer: 1500,
                    showConfirmButton: false
                });
            } else {
                await ApiService.createProyecto(payload);
                Swal.fire({
                    icon: 'success',
                    title: '¡Guardado!',
                    text: 'El proyecto fue creado correctamente',
                    timer: 1500,
                    showConfirmButton: false
                });
            }
            proyectoModal.style.display = 'none';
            loadProyectos();
        } catch (err) {
            Swal.fire({
                icon: 'error',
                title: 'Error al guardar',
                text: err.message
            });
        }
    });

    async function handleDeleteProyecto(id) {
        const result = await Swal.fire({
            title: '¿Está seguro?',
            text: 'No podrá revertir esta acción',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#dc2626',
            cancelButtonColor: '#64748b',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        });

        if (result.isConfirmed) {
            try {
                await ApiService.deleteProyecto(id);
                Swal.fire({
                    icon: 'success',
                    title: '¡Eliminado!',
                    text: 'El proyecto ha sido eliminado',
                    timer: 1500,
                    showConfirmButton: false
                });
                loadProyectos();
            } catch (err) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al eliminar',
                    text: err.message
                });
            }
        }
    }

    // --- USUARIOS LOGIC ---
    async function loadUsuarios() {
        try {
            const usuarios = await ApiService.getUsuarios();
            renderUsuarios(usuarios);
        } catch (err) {
            console.error('Error al cargar usuarios:', err);
        }
    }

    function renderUsuarios(usuarios) {
        usuariosTableBody.innerHTML = '';
        if (usuarios.length === 0) {
            usuariosTableBody.innerHTML = '<tr><td colspan="5" style="text-align:center;">No hay usuarios registrados</td></tr>';
            return;
        }

        usuarios.forEach(u => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${u.usuarioId}</td>
                <td>${escapeHtml(u.nombre)}</td>
                <td>${escapeHtml(u.email)}</td>
                <td><span class="user-badge">${escapeHtml(u.rol ? u.rol.nombreRol : '-')}</span></td>
                <td>${u.fechaRegistro || '-'}</td>
                <td>
                    <button class="btn btn-secondary btn-sm change-role-btn" data-id="${u.usuarioId}">Cambiar Rol</button>
                </td>
            `;
            usuariosTableBody.appendChild(tr);
        });

        document.querySelectorAll('.change-role-btn').forEach(btn => {
            btn.addEventListener('click', (e) => openRolModal(e.target.dataset.id));
        });
    }

    function openRolModal(usuarioId) {
        rolUserIdInput.value = usuarioId;
        rolModal.style.display = 'flex';
    }

    closeRolModalBtn.addEventListener('click', () => {
        rolModal.style.display = 'none';
    });

    rolForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const userId = rolUserIdInput.value;
        const nuevoRol = selectNuevoRol.value;

        try {
            await ApiService.cambiarRol(userId, nuevoRol);
            Swal.fire({
                icon: 'success',
                title: 'Rol Actualizado',
                text: 'El rol del usuario ha sido actualizado correctamente',
                timer: 1500,
                showConfirmButton: false
            });
            rolModal.style.display = 'none';
            loadUsuarios();
        } catch (err) {
            Swal.fire({
                icon: 'error',
                title: 'Error al cambiar rol',
                text: err.message
            });
        }
    });

    function escapeHtml(text) {
        if (!text) return '';
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
});
