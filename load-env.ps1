# Script para cargar variables de entorno desde archivo .env
# Uso: . .\load-env.ps1

$envFile = ".env"

if (Test-Path $envFile) {
    Write-Host "Cargando variables de entorno desde $envFile..." -ForegroundColor Green
    
    Get-Content $envFile | ForEach-Object {
        if ($_ -match '^\s*([^#][^=]*)\s*=\s*(.*)$') {
            $name = $matches[1].Trim()
            $value = $matches[2].Trim()
            
            # Remover comillas si existen
            $value = $value -replace '^["'']|["'']$', ''
            
            # No cargar si es un placeholder
            if ($value -notmatch '^<.*>$') {
                Set-Item -Path "env:$name" -Value $value
                Write-Host "  ✓ $name" -ForegroundColor Cyan
            } else {
                Write-Host "  ⚠ $name (placeholder - no cargado)" -ForegroundColor Yellow
            }
        }
    }
    
    Write-Host "`nVariables cargadas. Ahora puedes ejecutar:" -ForegroundColor Green
    Write-Host "  cd prueba-sprint" -ForegroundColor White
    Write-Host "  mvn spring-boot:run -Dspring-boot.run.profiles=supabase" -ForegroundColor White
} else {
    Write-Host "Error: Archivo .env no encontrado." -ForegroundColor Red
    Write-Host "Crea uno basado en .env.example:" -ForegroundColor Yellow
    Write-Host "  Copy-Item .env.example .env" -ForegroundColor White
    Write-Host "  # Luego edita .env y añade SUPABASE_PASSWORD" -ForegroundColor White
}
