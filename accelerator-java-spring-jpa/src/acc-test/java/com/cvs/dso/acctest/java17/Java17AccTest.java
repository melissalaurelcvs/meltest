package com.cvs.dso.acctest.java17;

import org.junit.jupiter.api.Test;

import static com.cvs.dso.acctest.common.TestHarness.buildToolConfig;
import static com.cvs.dso.acctest.common.TestHarness.isGradleProject;
import static com.cvs.dso.acctest.common.TestHarness.isMavenProject;
import static org.assertj.core.api.Assertions.assertThat;

class Java17AccTest {

    @Test
    void buildToolHasJavaVersion() {
        if (isMavenProject()) {
            assertThat(buildToolConfig())
                    .content().contains("<java.version>17</java.version>");
        }

        if (isGradleProject()) {
            assertThat(buildToolConfig()).content().contains(
                    "sourceCompatibility = '17'",
                    "targetCompatibility = '17'"
            );
        }
    }
}
