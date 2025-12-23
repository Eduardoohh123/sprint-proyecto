# Contribuciones y política de commits 📁

Por favor, sigue estas reglas al hacer cambios en el repositorio:

- **Haz los commits desde la raíz del repositorio** (`sprint-proyecto`), no desde subcarpetas (p.ej. `prueba-sprint`). Esto ayuda a mantener un historial consistente y evita confusiones con rutas relativas.
- Incluye un mensaje claro y conciso, formato sugerido: `tipo(scope): breve-descripción` (ej. `feat(api): add health endpoint`).
- Para cambios que afecten a despliegues o variables de entorno, actualiza también la documentación y avisa en el PR.
- Si necesitas ejecutar scripts de mantenimiento (migraciones, limpieza), añade un archivo `scripts/` con instrucciones y marca claramente si es `dev-only` o `prod`.

Gracias — esto mantiene el historial limpio y facilita despliegues y revisiones. ✅
