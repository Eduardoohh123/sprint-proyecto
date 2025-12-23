package com.example.prueba_sprint.startup;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SchemaRunner {

    private static final Logger log = LoggerFactory.getLogger(SchemaRunner.class);

    private final DataSource dataSource;

    public SchemaRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void runIfRequested() {
        try {
            log.info("Ejecutando schema.sql en la base de datos (temporal)");
            ResourceDatabasePopulator pop = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
            pop.execute(dataSource);
            log.info("Ejecución de schema.sql completada correctamente");
        } catch (Exception e) {
            log.error("Fallo al ejecutar schema.sql en startup: {}", e.getMessage(), e);
            // No rethrow: queremos que la app continúe y que los logs registren el fallo para diagnóstico
        }
    }
}
