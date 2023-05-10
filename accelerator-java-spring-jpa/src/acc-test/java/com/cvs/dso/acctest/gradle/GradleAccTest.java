package com.cvs.dso.acctest.gradle;

import org.junit.jupiter.api.Test;

import java.io.File;

import static com.cvs.dso.acctest.common.TestHarness.isDockerProject;
import static org.assertj.core.api.Assertions.assertThat;

class GradleAccTest {

    @Test
    void gradleFilesExist() {
        assertThat(new File("build.gradle")).exists().isFile();
        assertThat(new File("settings.gradle")).exists().isFile();
        assertThat(new File("gradlew")).exists().isFile();
        assertThat(new File("gradlew.bat")).exists().isFile();
        assertThat(new File("gradle")).exists().isDirectory();
    }

    @Test
    void mavenFilesDoNotExist() {
        assertThat(new File("pom.xml")).doesNotExist();
        assertThat(new File("mvnw")).doesNotExist();
        assertThat(new File("mvnw.cmd")).doesNotExist();
        assertThat(new File(".mvnw")).doesNotExist();
    }

    @Test
    void hasProjectDetails() {
        assertThat(new File("build.gradle")).content().contains(
                "group = 'com.cvs'"
        );
        assertThat(new File("settings.gradle")).content().contains(
                "rootProject.name = 'acc-test'"
        );
    }

    @Test
    void binTestUsesGradle() {
        assertThat(new File("bin/test")).content().contains("gradlew");
    }

    @Test
    void binBuildUsesGradle() {
        assertThat(new File("bin/build")).content().contains("gradlew");
    }

    @Test
    void readmeHasGradleCommands() {
        assertThat(new File("README.md")).content()
                .contains("gradlew test", "gradlew bootRun", "gradlew sonarqube");
    }

    @Test
    void readmeDoesNotHaveMavenCommands() {
        assertThat(new File("README.md")).content().doesNotContain("mvnw");
    }

    @Test
    void readmeHasLinkToCodeCoverage() {
        assertThat(new File("README.md")).content().contains("build/reports/jacoco/test/html/index.html");
    }

    @Test
    void readmeHasGradlePreReq() {
        assertThat(new File("README.md")).content().contains("Gradle wrapper");
    }

    @Test
    void readmeWarnsAgainstJUnit4() {
        if (isDockerProject()) {
            assertThat(new File("README.md")).content().contains("JUnit version support");
        } else {
            assertThat(new File("README.md")).content().doesNotContain("JUnit version support");
        }
    }
}
