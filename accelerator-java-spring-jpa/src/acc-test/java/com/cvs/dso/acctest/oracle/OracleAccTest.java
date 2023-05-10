package com.cvs.dso.acctest.oracle;

import org.junit.jupiter.api.Test;

import java.io.File;

import static com.cvs.dso.acctest.common.TestHarness.buildToolConfig;
import static com.cvs.dso.acctest.common.TestHarness.fileWithinSourceRoot;
import static org.assertj.core.api.Assertions.assertThat;

class OracleAccTest {

    @Test
    void hasOracleDependencies() {
        assertThat(buildToolConfig()).content().contains("jpa", "oracle", "flyway");
    }
    @Test
    void oracleSpecificDBPropertiesExistInApplicationYamls() {
        assertThat(new File("src/main/resources/application.yaml"))
                .exists()
                .content().contains("flyway", "oracle","Oracle12cDialect");

        assertThat(new File("src/test/resources/application-test.yaml"))
                .exists()
                .content().contains("oracle");
    }

    @Test
    void oracleSpecificTypeExistsInEntities() {
        assertThat(fileWithinSourceRoot("patient/data/PatientEntity.java"))
                .exists()
                .content().contains("uuid-char");
    }

    @Test
    void dockerComposeWithOracleExists() {
        assertThat(new File("docker-compose.yaml"))
                .exists()
                .content().contains("image: oracledb19c");
    }

    @Test
    void migrationHasProperIdType() {
        assertThat(new File("src/main/resources/db/migration/V1.0__create_patient_table.sql"))
                .exists()
                .content().contains("CHAR(36)");
    }

    @Test
    void migrationHasProperStringType() {
        assertThat(new File("src/main/resources/db/migration/V1.0__create_patient_table.sql"))
                .exists()
                .content().contains("VARCHAR2");
    }
}
