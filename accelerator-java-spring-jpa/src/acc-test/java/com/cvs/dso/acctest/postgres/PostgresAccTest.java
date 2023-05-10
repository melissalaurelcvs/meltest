package com.cvs.dso.acctest.postgres;

import org.junit.jupiter.api.Test;

import java.io.File;

import static com.cvs.dso.acctest.common.TestHarness.buildToolConfig;
import static com.cvs.dso.acctest.common.TestHarness.fileWithinSourceRoot;
import static org.assertj.core.api.Assertions.assertThat;

class PostgresAccTest {

    @Test
    void hasPostgresDbDependencies() {
        assertThat(buildToolConfig()).content().contains("jpa", "postgresql", "flyway");
    }
    @Test
    void postgresSpecificDBPropertiesExistInApplicationYamls() {
        assertThat(new File("src/main/resources/application.yaml"))
                .exists()
                .content().contains("postgresql", "PostgreSQLDialect");

        assertThat(new File("src/test/resources/application-test.yaml"))
                .exists()
                .content().contains("postgresql");
    }

    @Test
    void postgresSpecificTypeExistsInEntities() {
        assertThat(fileWithinSourceRoot("patient/data/PatientEntity.java"))
                .exists()
                .content().contains("pg-uuid");
    }

    @Test
    void dockerComposeWithPostgresExists() {
        assertThat(new File("docker-compose.yaml"))
                .exists()
                .content().contains("image: postgres:14");
    }

}
