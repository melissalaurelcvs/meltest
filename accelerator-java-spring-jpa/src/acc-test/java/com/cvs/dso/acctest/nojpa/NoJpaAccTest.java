package com.cvs.dso.acctest.nojpa;

import org.junit.jupiter.api.Test;

import java.io.File;

import static com.cvs.dso.acctest.common.TestHarness.buildToolConfig;
import static com.cvs.dso.acctest.common.TestHarness.fileWithinSourceRoot;
import static com.cvs.dso.acctest.common.TestHarness.fileWithinTestRoot;
import static org.assertj.core.api.Assertions.assertThat;

class NoJpaAccTest {

    @Test
    void persistencePortDoesNotExist() {
        assertThat(fileWithinSourceRoot("patient/data/")).doesNotExist();
        assertThat(fileWithinTestRoot("patient/data/")).doesNotExist();
        assertThat(fileWithinSourceRoot("patient/domain/PatientService.java"))
                .content().doesNotContain("com.cvs.acctest.patient.data");
    }

    @Test
    void dbMigrationsDoNotExist() {
        assertThat(new File("src/main/resources/db/")).doesNotExist();
    }

    @Test
    void dbPropertiesDoNotExistInApplicationYamls() {
        assertThat(new File("src/main/resources/application.yaml"))
                .exists()
                .content().doesNotContain("datasource", "jpa");

        assertThat(new File("src/test/resources/application-test.yaml"))
                .exists()
                .content().doesNotContain("datasource", "database");
    }

    @Test
    void buildToolConfigDoesNotHaveDbDependencies() {
        assertThat(buildToolConfig())
                .exists()
                .content().doesNotContain("jpa", "postgresql", "flyway");
    }

    @Test
    void readmeDoesNotContainDbInfo() {
        assertThat(new File("README.md"))
                .exists()
                .content().doesNotContain("JPA", "SQL", "Database", "migration", "Flyway");

        assertThat(new File("ADR.md"))
                .exists()
                .content().doesNotContain("migration", "Flyway", "Why UUIDs are primary keys");
    }

    @Test
    void dockerComposeWithOutPostgres() {
        assertThat(new File("docker-compose.yaml")).satisfiesAnyOf(
                file -> assertThat(file).doesNotExist(),
                file -> assertThat(file).content().doesNotContain("image: postgres:14")
        );
    }
}
