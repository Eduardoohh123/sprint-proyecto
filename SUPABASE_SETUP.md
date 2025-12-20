# Configuración Supabase - Proyecto Sprint

## ✅ Migración Completada

Se ha ejecutado exitosamente la migración inicial `20251220122419_initial_schema.sql` que incluye:

### Tablas Creadas
- `animes` - Catálogo de anime
- `categories` - Categorías de juegos
- `games` - Catálogo de juegos
- `downloads` - Enlaces de descarga para juegos
- `episodes` - Episodios de anime
- `guides` - Guías y tutoriales
- `news` - Noticias
- `users` - Usuarios del sistema
- `user_anime_lists` - Listas personales de anime

### Datos Iniciales Insertados
- 1 anime (One Piece)
- 1 categoría (Acción)
- 3 usuarios de prueba

## 🔗 Conexión Spring Boot

### Perfil Supabase
El archivo `application-supabase.properties` está configurado en:
```
prueba-sprint/src/main/resources/application-supabase.properties
```

### Variables de Entorno Requeridas
Antes de ejecutar Spring Boot, configura estas variables:

```powershell
# En PowerShell (Windows)
$env:SUPABASE_HOST="db.htdvrcajzddfjzpbfjhb.supabase.co"
$env:SUPABASE_DB="postgres"
$env:SUPABASE_USER="postgres"
$env:SUPABASE_PASSWORD="<TU_PASSWORD_DB_SUPABASE>"

# Para usar la Admin API (crear/eliminar usuarios desde el backend)
# Exporta la Service Role Key como variable de entorno:
# $env:SUPABASE_SERVICE_ROLE_KEY="eyJhbGci..."
```

### Ejecución con Maven
```powershell
# Desde el directorio prueba-sprint
mvn spring-boot:run -Dspring-boot.run.profiles=supabase
```

### Ejecución con JAR
```powershell
# Compilar
mvn clean package -DskipTests

# Ejecutar con perfil supabase
java -jar target/prueba-sprint-0.0.1-SNAPSHOT.jar --spring.profiles.active=supabase
```

## 📊 Comandos Supabase CLI

### Verificar Estado
```powershell
# Ver proyecto vinculado
supabase projects list

# Ver migraciones aplicadas
supabase migration list
```

### Crear Nueva Migración
```powershell
# Crear migración
supabase migration new nombre_migracion

# Editar archivo generado en:
# supabase/migrations/<timestamp>_nombre_migracion.sql

# Aplicar migración
supabase db push
```

### Rollback (si necesario)
```powershell
# Ver historial
supabase db remote commit

# Revertir última migración (usar con cuidado)
supabase db reset --db-url postgresql://postgres:<PASSWORD>@db.htdvrcajzddfjzpbfjhb.supabase.co:5432/postgres
```

## 🌐 Acceso al Dashboard
- **URL Proyecto:** https://htdvrcajzddfjzpbfjhb.supabase.co
- **Dashboard:** https://supabase.com/dashboard/project/htdvrcajzddfjzpbfjhb
- **SQL Editor:** https://supabase.com/dashboard/project/htdvrcajzddfjzpbfjhb/sql/new

## 🔐 Credenciales Supabase

### API Keys (para frontend/Ionic)
- **Project URL:** `https://htdvrcajzddfjzpbfjhb.supabase.co`
- **Anon/Public Key:** `sb_publishable_cVnJBQyNeNyuIJIqJx6fsA_330rGqLn`

### Database Password (para Spring Boot)
En el Dashboard de Supabase:
1. Ve a **Settings** → **Database**
2. Busca la sección **Connection String**
3. Copia el password de la base de datos
4. Úsalo como `SUPABASE_PASSWORD`

### Archivo .env
Crea un archivo `.env` basado en `.env.example`:
```bash
cp .env.example .env
# Edita .env y añade el password de la DB
```

## 📝 Notas Importantes
- **JPA DDL:** Configurado como `none` en perfil supabase (las migraciones manejan el esquema)
- **SSL:** Requerido (`sslmode=require`)
- **Puerto:** 5432 (PostgreSQL estándar)
- **Base de datos:** `postgres` (default de Supabase)

## 🚀 Próximos Pasos
1. Obtener password de DB desde Supabase Dashboard (Settings → Database)
2. Copiar `.env.example` a `.env` y completar `SUPABASE_PASSWORD`
3. Ejecutar Spring Boot con perfil `supabase`:
   ```powershell
   # Cargar variables desde .env (manual en PowerShell)
   $env:SUPABASE_PASSWORD="TU_PASSWORD_AQUI"
   
   # Ejecutar Spring Boot
   cd prueba-sprint
   mvn spring-boot:run -Dspring-boot.run.profiles=supabase
   ```
4. Verificar conexión probando endpoints (ej: `http://localhost:8080/api/animes`)
5. Configurar app móvil Ionic con las credenciales de [SUPABASE_CONFIG.md](../../../mobile/app-movile/SUPABASE_CONFIG.md)

## 🔧 Troubleshooting

### Error de Conexión
```
Connection refused
```
**Solución:** Verifica que SUPABASE_PASSWORD esté configurado correctamente.

### Error de SSL
```
SSL error: certificate verify failed
```
**Solución:** Asegúrate de usar `sslmode=require` en la URL JDBC.

### Tablas No Encontradas
```
relation "animes" does not exist
```
**Solución:** Ejecuta `supabase db push` nuevamente o verifica en SQL Editor.
