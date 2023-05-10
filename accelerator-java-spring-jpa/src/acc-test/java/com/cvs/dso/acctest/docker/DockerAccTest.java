package com.cvs.dso.acctest.docker;

import org.junit.jupiter.api.Test;

import java.io.File;

import static com.cvs.dso.acctest.common.TestHarness.buildToolConfig;
import static org.assertj.core.api.Assertions.assertThat;

class DockerAccTest {

    @Test
    void hasTestcontainersDependencies() {
        assertThat(buildToolConfig()).content().contains("testcontainers");
    }

    @Test
    void readmeContainsDockerInfo() {
        assertThat(new File("README.md"))
                .exists()
                .content().contains("Testcontainers", "Docker", "docker");

        assertThat(new File("ADR.md"))
                .exists()
                .content().contains("Testcontainers", "Docker");
    }
}
