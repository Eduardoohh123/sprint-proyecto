package com.example.prueba_sprint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    @Autowired
    private DataSource dataSource;

    /**
     * Endpoint para verificar el estado de la aplicación
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("application", "Spring Boot API");
        status.put("status", "running");
        status.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(status);
    }

    /**
     * Endpoint para verificar la conexión con PostgreSQL
     */
    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> checkDatabaseConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            response.put("database", "PostgreSQL");
            response.put("status", "connected");
            response.put("url", connection.getMetaData().getURL());
            response.put("username", connection.getMetaData().getUserName());
            response.put("databaseProductName", connection.getMetaData().getDatabaseProductName());
            response.put("databaseProductVersion", connection.getMetaData().getDatabaseProductVersion());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Endpoint de prueba simple
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    /**
     * Endpoint para listar interfaces de red y direcciones IPv4/IPv6
     */
    @GetMapping("/network")
    public ResponseEntity<Map<String, Object>> getNetworkInfo() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> interfaces = new HashMap<>();
            java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
            while (nets.hasMoreElements()) {
                java.net.NetworkInterface netint = nets.nextElement();
                java.util.List<Map<String, Object>> addrs = new java.util.ArrayList<>();
                java.util.Enumeration<java.net.InetAddress> inetAddresses = netint.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    java.net.InetAddress addr = inetAddresses.nextElement();
                    Map<String, Object> info = new HashMap<>();
                    info.put("address", addr.getHostAddress());
                    info.put("isLoopback", addr.isLoopbackAddress());
                    info.put("isLinkLocal", addr.isLinkLocalAddress());
                    info.put("isSiteLocal", addr.isSiteLocalAddress());
                    info.put("isIPv4", addr instanceof java.net.Inet4Address);
                    info.put("isIPv6", addr instanceof java.net.Inet6Address);
                    addrs.add(info);
                }
                interfaces.put(netint.getName(), addrs);
            }
            response.put("interfaces", interfaces);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
