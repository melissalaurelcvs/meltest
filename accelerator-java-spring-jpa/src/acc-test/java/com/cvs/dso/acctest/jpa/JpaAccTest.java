package com.cvs.dso.acctest.jpa;

import org.junit.jupiter.api.Test;

import java.io.File;

import static com.cvs.dso.acctest.common.TestHarness.fileWithinSourceRoot;
import static org.assertj.core.api.Assertions.assertThat;

class JpaAccTest {

    @Test
    void persistencePortExists() {
        assertThat(fileWithinSourceRoot("patient/data/")).exists().isNotEmptyDirectory();
        assertThat(fileWithinSourceRoot("patient/data/")).exists().isNotEmptyDirectory();
        assertThat(fileWithinSourceRoot("patient/domain/PatientService.java"))
                .content().contains("com.cvs.acctest.patient.data");
    }

    @Test
    void dbMigrationsExist() {
        assertThat(new File("src/main/resources/db/migration/"))
                .isNotEmptyDirectory()
                .isDirectoryContaining("glob:**.sql");
    }

    @Test
    void dbPropertiesExistInApplicationYamls() {
        assertThat(new File("src/main/resources/application.yaml"))
                .exists()
                .content().contains("datasource", "jpa");

        assertThat(new File("src/test/resources/application-test.yaml"))
                .exists()
                .content().contains("datasource", "database");
    }

    @Test
    void readmeContainsJpaInfo() {
        assertThat(new File("README.md"))
                .exists()
                .content().contains("JPA", "SQL", "Database", "migration", "Flyway");

        assertThat(new File("ADR.md"))
                .exists()
                .content().contains("migration", "Flyway", "Why UUIDs as primary keys");
    }
}
