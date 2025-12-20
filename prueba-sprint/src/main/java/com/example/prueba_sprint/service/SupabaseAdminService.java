package com.example.prueba_sprint.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@Service
public class SupabaseAdminService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${supabase.host:${SUPABASE_HOST:}}")
    private String supabaseHost;

    @Value("${supabase.serviceRoleKey:${SUPABASE_SERVICE_ROLE_KEY:}}")
    private String serviceRoleKey;

    private String baseUrl() {
        if (supabaseHost == null || supabaseHost.isBlank()) {
            throw new IllegalStateException("SUPABASE_HOST no está configurado");
        }
        if (supabaseHost.startsWith("http")) {
            return supabaseHost;
        }
        return "https://" + supabaseHost;
    }

    /**
     * Crea un usuario en Supabase Auth (Admin API)
     * Retorna el id (uid) del usuario creado.
     */
    public String createUser(String email, String password) {
        String url = baseUrl() + "/auth/v1/admin/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(serviceRoleKey);
        // Supabase Admin API requires the apikey header in addition to Authorization in some setups
        headers.add("apikey", serviceRoleKey);

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        body.put("email_confirm", true);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                Object id = resp.getBody().get("id");
                return id != null ? id.toString() : null;
            }
            return null;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error creando usuario en Supabase: " + e.getResponseBodyAsString(), e);
        } catch (Exception ex) {
            throw new RuntimeException("Error al conectar con Supabase Admin API: " + ex.getMessage(), ex);
        }
    }

    /**
     * Borra un usuario en Supabase Auth por su id (uid)
     */
    public void deleteUser(String supabaseUserId) {
        if (supabaseUserId == null) return;
        String url = baseUrl() + "/auth/v1/admin/users/" + supabaseUserId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(serviceRoleKey);
        headers.add("apikey", serviceRoleKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error eliminando usuario en Supabase: " + e.getResponseBodyAsString(), e);
        } catch (Exception ex) {
            throw new RuntimeException("Error al conectar con Supabase Admin API: " + ex.getMessage(), ex);
        }
    }
}
