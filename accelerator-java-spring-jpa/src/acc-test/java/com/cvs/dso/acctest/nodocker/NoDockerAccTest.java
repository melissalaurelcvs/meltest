package com.cvs.dso.acctest.nodocker;

import org.junit.jupiter.api.Test;

import java.io.File;

import static com.cvs.dso.acctest.common.TestHarness.buildToolConfig;
import static org.assertj.core.api.Assertions.assertThat;

class NoDockerAccTest {

    @Test
    void dockerComposeDoesNotExist() {
        assertThat(new File("docker-compose.yaml")).doesNotExist();
    }

    @Test
    void buildToolConfigDoesNotHaveTestcontainersDependencies() {
        assertThat(buildToolConfig())
                .exists()
                .content().doesNotContain("testcontainers");
    }

    @Test
    void readmeDoesNotContainDockerInfo() {
        assertThat(new File("README.md"))
                .exists()
                .content().doesNotContain("Testcontainers", "Docker", "docker");

        assertThat(new File("ADR.md"))
                .exists()
                .content().doesNotContain("Testcontainers", "Docker");
    }
}
