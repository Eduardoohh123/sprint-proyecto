# Conexión Ionic - Spring Boot

## 🎯 Configuración Completada

Se ha configurado exitosamente la conexión entre la aplicación Ionic y el backend Spring Boot.

## 📋 Archivos Modificados/Creados

### Backend (Spring Boot)
1. **SecurityConfig.java** - Configurado para permitir acceso público a `/api/**` y deshabilitar CSRF para endpoints REST
2. **WebConfig.java** - Ya estaba configurado con CORS para permitir conexiones desde Ionic

### Frontend (Ionic)
1. **api.service.ts** - Agregados métodos para:
   - `registerUser()` - Registrar usuario en Spring Boot
   - `loginUser()` - Login de usuario
   - `getUserById()` - Obtener usuario por ID
   - `updateUser()` - Actualizar usuario
   - Métodos para animes y games

2. **spring-register/** - Nueva página de registro que se conecta directamente con Spring Boot
   - `spring-register.page.ts`
   - `spring-register.page.html`
   - `spring-register.page.scss`

3. **app.routes.ts** - Agregada ruta `/spring-register`

4. **login.page.ts** - Agregado método `registerInSpringBoot()` para registros desde la página de login

## 🚀 Cómo Probar la Conexión

### Paso 1: Iniciar el Backend Spring Boot

```bash
cd "c:\Users\Eduardo\OneDrive\Desktop\Estudios\Desarrollo de software 2\sprint-proyecto\prueba-sprint"
mvn spring-boot:run
```

El backend estará disponible en: **http://localhost:8080**

### Paso 2: Iniciar la Aplicación Ionic

```bash
cd "C:\Users\Eduardo\OneDrive\Desktop\Estudios\mobile\app-movile"
ionic serve
```

La aplicación estará disponible en: **http://localhost:8100**

### Paso 3: Registrar un Usuario

Opción 1: **Usar la página dedicada**
1. Navega a: http://localhost:8100/spring-register
2. Completa el formulario con:
   - Nombre completo
   - Nombre de usuario
   - Email
   - Contraseña
   - Confirmar contraseña
3. Acepta los términos y haz clic en "Registrarse"

Opción 2: **Desde el login**
1. Navega a: http://localhost:8100/login
2. Busca el formulario de registro integrado
3. Llama al método `registerInSpringBoot()` en lugar de `onRegisterInline()`

### Paso 4: Verificar en la Base de Datos H2

1. Abre el navegador y ve a: **http://localhost:8080/h2-console**
2. Configuración de conexión:
   - **JDBC URL:** `jdbc:h2:mem:testdb`
   - **Usuario:** `sa`
   - **Contraseña:** (dejar vacío)
3. Haz clic en "Connect"
4. Ejecuta la consulta:
   ```sql
   SELECT * FROM USERS;
   ```
5. Deberías ver tu usuario registrado

## 📡 Endpoints API Disponibles

### Usuarios
- **POST** `/api/users/register` - Registrar nuevo usuario
  ```json
  {
    "username": "johndoe",
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }
  ```

- **POST** `/api/users/login` - Iniciar sesión
  ```json
  {
    "email": "john@example.com",
    "password": "password123"
  }
  ```

- **GET** `/api/users/{id}` - Obtener usuario por ID

- **PUT** `/api/users/{id}` - Actualizar usuario

### Animes
- **GET** `/api/anime` - Obtener todos los animes
- **GET** `/api/anime/{id}` - Obtener anime por ID
- **GET** `/api/anime/search?q=naruto` - Buscar animes

### Games
- **GET** `/api/games` - Obtener todos los juegos
- **GET** `/api/games/{id}` - Obtener juego por ID

## 🔧 Configuración

### Environment (Ionic)
La URL del backend está configurada en:
```typescript
// src/environments/environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

### CORS (Spring Boot)
Configurado en `WebConfig.java` para permitir:
- Orígenes: `localhost:8100`, `localhost:4200`
- Métodos: GET, POST, PUT, DELETE, OPTIONS
- Headers: Todos (*)
- Credentials: Habilitado

### Security (Spring Boot)
Configurado en `SecurityConfig.java`:
- Rutas `/api/**` son públicas (no requieren autenticación)
- CSRF deshabilitado para `/api/**`

## 🐛 Troubleshooting

### Error de CORS
Si ves errores de CORS en la consola del navegador:
1. Verifica que el backend esté corriendo
2. Confirma que `WebConfig.java` permite el origen correcto
3. Asegúrate de que la URL en `environment.ts` sea correcta

### Error 401 Unauthorized
Si obtienes 401 al hacer peticiones:
1. Verifica que las rutas `/api/**` estén configuradas como públicas en `SecurityConfig.java`
2. Revisa los logs del backend para ver qué está bloqueando la petición

### Backend no responde
1. Verifica que Spring Boot esté corriendo: `http://localhost:8080`
2. Revisa los logs en la terminal donde ejecutaste `mvn spring-boot:run`
3. Verifica que el puerto 8080 no esté ocupado

### Ionic no se conecta
1. Verifica la URL en `environment.ts`
2. Abre las DevTools del navegador (F12) y revisa la consola
3. Verifica la pestaña Network para ver las peticiones HTTP

## ✅ Verificación Rápida

### Test con cURL (Backend)
```bash
# Registrar usuario
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Test desde el Navegador (Ionic)
1. Abre: http://localhost:8100/spring-register
2. Abre DevTools (F12)
3. Ve a la pestaña "Network"
4. Registra un usuario
5. Verifica que veas una petición POST a `http://localhost:8080/api/users/register` con respuesta 201

## 📝 Notas Importantes

1. **Base de datos H2**: Es en memoria, los datos se pierden al reiniciar el backend
2. **Contraseñas**: Se encriptan con BCrypt antes de guardar
3. **CORS**: Solo configurado para desarrollo local
4. **Producción**: Necesitarás cambiar las URLs y configuraciones de CORS para producción

## 🎉 ¡Listo!

Tu aplicación Ionic ahora está completamente conectada con el backend Spring Boot. Los usuarios que registres desde Ionic se guardarán en la misma base de datos H2 que usa tu aplicación Spring Boot.
