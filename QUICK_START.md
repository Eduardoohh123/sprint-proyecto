# 🚀 Quick Start - Proyecto Sprint con Supabase

## ✅ Completado
- ✓ Supabase CLI instalado
- ✓ Proyecto vinculado (htdvrcajzddfjzpbfjhb)
- ✓ Migración inicial ejecutada
- ✓ Esquema creado (9 tablas)
- ✓ Datos de prueba insertados

## 📋 Siguiente Paso: Conectar Spring Boot

### 1. Obtener Password de DB
1. Ir a: https://supabase.com/dashboard/project/htdvrcajzddfjzpbfjhb/settings/database
2. Copiar el **Database Password**

### 2. Configurar Variables
```powershell
# Crear archivo .env desde ejemplo
Copy-Item .env.example .env

# Editar .env y reemplazar:
# SUPABASE_PASSWORD=<OBTENER_DESDE_SUPABASE_DASHBOARD>
# con tu password real
notepad .env
```

### 3. Cargar Variables y Ejecutar
```powershell
# Cargar variables de entorno
. .\load-env.ps1

# Ejecutar Spring Boot con perfil Supabase
cd prueba-sprint
mvn spring-boot:run -Dspring-boot.run.profiles=supabase
```

### 4. Verificar Conexión
Abre en navegador:
- http://localhost:8080/api/animes
- http://localhost:8080/api/users

## 📱 App Móvil Ionic

### Configuración Supabase
Ver: [app-movile/SUPABASE_INTEGRATION.md](../../mobile/app-movile/SUPABASE_INTEGRATION.md)

**Credenciales ya configuradas:**
- URL: `https://htdvrcajzddfjzpbfjhb.supabase.co`
- Key: `sb_publishable_cVnJBQyNeNyuIJIqJx6fsA_330rGqLn`

### Instalación
```bash
cd ../../mobile/app-movile
npm install @supabase/supabase-js
```

## 🗂️ Archivos Creados

### Backend (Spring Boot)
- `prueba-sprint/src/main/resources/application-supabase.properties` - Perfil Supabase
- `.env.example` - Template de variables
- `load-env.ps1` - Script para cargar .env
- `SUPABASE_SETUP.md` - Documentación completa

### Supabase
- `supabase/migrations/20251220122419_initial_schema.sql` - Migración inicial

### Frontend (Ionic)
- `SUPABASE_CONFIG.md` - Variables de entorno
- `SUPABASE_INTEGRATION.md` - Guía completa de integración

## 🎯 Arquitectura

```
┌─────────────────┐
│   App Ionic     │ ← Supabase Client (auth, realtime)
└────────┬────────┘
         │ HTTP
         ▼
┌─────────────────┐
│  Spring Boot    │ ← JDBC Connection
│   (REST API)    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Supabase     │
│   PostgreSQL    │
└─────────────────┘
```

## 📚 Comandos Útiles

### Supabase CLI
```powershell
# Ver migraciones
supabase migration list

# Nueva migración
supabase migration new nombre

# Aplicar migraciones
supabase db push

# Ver estado del proyecto
supabase projects list
```

### Spring Boot
```powershell
# Compilar
mvn clean package -DskipTests

# Ejecutar tests
mvn test

# Ejecutar JAR
java -jar target/prueba-sprint-0.0.1-SNAPSHOT.jar --spring.profiles.active=supabase
```

### Ionic
```powershell
# Servidor desarrollo
ionic serve

# Build producción
ionic build --prod

# Android
ionic capacitor run android

# iOS
ionic capacitor run ios
```

## ⚠️ Troubleshooting

### Error: "Connection refused"
- Verifica que `SUPABASE_PASSWORD` esté configurado
- Carga variables: `. .\load-env.ps1`

### Error: "SSL error"
- Ya configurado `sslmode=require` en application-supabase.properties

### Error: "Table not found"
- Ejecuta: `supabase db push`
- Verifica en Dashboard: https://supabase.com/dashboard/project/htdvrcajzddfjzpbfjhb/editor

## 🔗 Enlaces Útiles
- Dashboard: https://supabase.com/dashboard/project/htdvrcajzddfjzpbfjhb
- SQL Editor: https://supabase.com/dashboard/project/htdvrcajzddfjzpbfjhb/sql/new
- Database Settings: https://supabase.com/dashboard/project/htdvrcajzddfjzpbfjhb/settings/database
- API Docs: https://supabase.com/dashboard/project/htdvrcajzddfjzpbfjhb/api

## 💡 Próximos Pasos Recomendados

1. ✅ Obtener password DB y configurar `.env`
2. ✅ Ejecutar Spring Boot y verificar conexión
3. ⬜ Implementar servicio Supabase en Ionic
4. ⬜ Configurar autenticación en app móvil
5. ⬜ Probar flujo completo: Ionic → Spring Boot → Supabase
6. ⬜ Implementar Row Level Security (RLS) en Supabase
7. ⬜ Deploy Spring Boot (Render, Railway, etc.)
8. ⬜ Deploy Ionic (Netlify, Vercel, etc.)
