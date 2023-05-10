package com.cvs.dso.acctest.java8;

import org.junit.jupiter.api.Test;

import static com.cvs.dso.acctest.common.TestHarness.buildToolConfig;
import static com.cvs.dso.acctest.common.TestHarness.isGradleProject;
import static com.cvs.dso.acctest.common.TestHarness.isMavenProject;
import static org.assertj.core.api.Assertions.assertThat;

class Java8AccTest {

    @Test
    void buildToolHasJavaVersion() {
        if (isMavenProject()) {
            assertThat(buildToolConfig())
                    .content().contains("<java.version>8</java.version>");
        }

        if (isGradleProject()) {
            assertThat(buildToolConfig()).content().contains(
                    "sourceCompatibility = '8'",
                    "targetCompatibility = '8'"
            );
        }
    }
}
