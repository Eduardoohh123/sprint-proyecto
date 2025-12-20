# 🗄️ Guía: Base de Datos Externa PostgreSQL

## ¿Por qué usar PostgreSQL?

- ✅ Los datos **persisten** (no se pierden al reiniciar)
- ✅ Ambas aplicaciones (Spring Boot e Ionic) comparten la **misma base de datos**
- ✅ Base de datos **profesional** usada en producción
- ✅ Fácil de administrar con pgAdmin

## 📥 Paso 1: Instalar PostgreSQL

### Opción A: Instalar PostgreSQL (Recomendado)
1. Descarga PostgreSQL: https://www.postgresql.org/download/windows/
2. Ejecuta el instalador
3. Durante la instalación:
   - **Puerto:** 5432 (dejar por defecto)
   - **Usuario:** postgres
   - **Contraseña:** Elige una (ejemplo: `postgres` o `admin123`)
   - Instala también **pgAdmin** (herramienta visual)

### Opción B: Usar Docker (Más rápido)
```powershell
docker run --name postgres-prueba -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
```

## 🔧 Paso 2: Crear la Base de Datos

### Usando pgAdmin (GUI):
1. Abre **pgAdmin**
2. Conecta al servidor (localhost, usuario: postgres, contraseña: la que elegiste)
3. Click derecho en "Databases" → "Create" → "Database..."
4. Nombre: `prueba_sprint_db`
5. Click "Save"

### Usando línea de comandos:
```powershell
# Conectarse a PostgreSQL
psql -U postgres

# Crear la base de datos
CREATE DATABASE prueba_sprint_db;

# Salir
\q
```

### Usando Docker:
```powershell
docker exec -it postgres-prueba psql -U postgres -c "CREATE DATABASE prueba_sprint_db;"
```

## ⚙️ Paso 3: Configurar Spring Boot

Ya está configurado! Solo necesitas **actualizar la contraseña** si usaste una diferente:

Edita: `application-postgres.properties`
```properties
spring.datasource.password=TU_CONTRASEÑA_AQUI
```

## 🚀 Paso 4: Iniciar con PostgreSQL

Ejecuta Spring Boot con el perfil de PostgreSQL:

```powershell
cd "c:\Users\Eduardo\OneDrive\Desktop\Estudios\Desarrollo de software 2\sprint-proyecto\prueba-sprint"

# Opción 1: Usando variable de entorno
$env:SPRING_PROFILES_ACTIVE="postgres"; .\mvnw.cmd spring-boot:run

# Opción 2: Usando parámetro
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
```

## 📱 Paso 5: Ionic (Sin cambios necesarios)

**¡No necesitas cambiar nada en Ionic!** Seguirá usando la misma API REST:
- `http://localhost:8080/api/users/register`
- `http://localhost:8080/api/users/login`
- etc.

Los datos se guardarán automáticamente en PostgreSQL.

## ✅ Verificar que Funciona

1. **Inicia Spring Boot** con PostgreSQL (Paso 4)
2. **Verifica los logs**, deberías ver:
   ```
   HHH000412: Hibernate ORM core version ...
   PostgreSQLDialect
   Tomcat started on port 8080
   ```
3. **Registra un usuario** desde Ionic o test-conexion.html
4. **Verifica en pgAdmin:**
   - Abre pgAdmin
   - Ve a: `prueba_sprint_db` → `Schemas` → `public` → `Tables` → `users`
   - Click derecho → "View/Edit Data" → "All Rows"

## 🔄 Volver a H2 (desarrollo rápido)

Si quieres volver a H2 temporalmente:
```powershell
.\mvnw.cmd spring-boot:run
# (sin especificar perfil, usa application.properties por defecto)
```

## 🆘 Solución de Problemas

### Error: "Connection refused"
- PostgreSQL no está corriendo
- Solución: Inicia el servicio de PostgreSQL o el contenedor Docker

### Error: "database prueba_sprint_db does not exist"
- Falta crear la base de datos
- Solución: Ejecuta los comandos del Paso 2

### Error: "password authentication failed"
- Contraseña incorrecta
- Solución: Actualiza `application-postgres.properties` con la contraseña correcta

### Verificar si PostgreSQL está corriendo:
```powershell
# Windows (PowerShell)
Get-Service -Name postgresql*

# O usando psql
psql -U postgres -c "SELECT version();"
```

## 📊 Diferencias H2 vs PostgreSQL

| Característica | H2 (En memoria) | PostgreSQL (Externa) |
|---------------|-----------------|---------------------|
| **Persistencia** | ❌ Se pierde al reiniciar | ✅ Datos permanentes |
| **Velocidad** | ⚡ Más rápida | 🚶 Ligeramente más lenta |
| **Uso** | 🔧 Desarrollo/Testing | 🏢 Producción |
| **Configuración** | ✅ Cero configuración | ⚙️ Requiere instalación |
| **Compartir datos** | ❌ Solo la app actual | ✅ Múltiples apps |
| **Herramientas** | H2 Console básica | pgAdmin avanzado |

## 🎯 Recomendación

Para tu proyecto con Ionic + Spring Boot:
- **Desarrollo:** Usa PostgreSQL (para ver datos persistentes)
- **Producción:** Usa PostgreSQL con variables de entorno (application-prod.properties)

## 📝 Conexión desde Ionic

Ionic no necesita cambios porque se conecta a través de la API REST:

```typescript
// Ya configurado en api.service.ts
apiUrl = 'http://localhost:8080/api'

// Spring Boot maneja internamente H2 o PostgreSQL
// Ionic solo hace peticiones HTTP, no le importa la DB
```

## 🔐 Buenas Prácticas

1. **No subas contraseñas a Git:**
   ```properties
   # Usa variables de entorno
   spring.datasource.password=${DB_PASSWORD:postgres}
   ```

2. **Diferentes configuraciones por ambiente:**
   - `application.properties` → H2 (desarrollo rápido)
   - `application-postgres.properties` → PostgreSQL local
   - `application-prod.properties` → PostgreSQL producción

3. **Backups:** Con PostgreSQL puedes hacer backups:
   ```powershell
   pg_dump -U postgres prueba_sprint_db > backup.sql
   ```

¡Listo! Ahora tienes una base de datos externa compartida entre Spring Boot e Ionic. 🎉
