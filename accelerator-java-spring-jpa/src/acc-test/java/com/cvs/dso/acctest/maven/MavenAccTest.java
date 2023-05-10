package com.cvs.dso.acctest.maven;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class MavenAccTest {

    @Test
    void mavenFilesExist() {
        assertThat(new File("pom.xml")).exists().isFile();
        assertThat(new File("mvnw")).exists().isFile();
        assertThat(new File("mvnw.cmd")).exists().isFile();
        assertThat(new File(".mvn")).exists().isDirectory();
    }

    @Test
    void gradleFilesDoNotExist() {
        assertThat(new File("build.gradle")).doesNotExist();
        assertThat(new File("settings.gradle")).doesNotExist();
        assertThat(new File("gradlew")).doesNotExist();
        assertThat(new File("gradlew.bat")).doesNotExist();
        assertThat(new File("gradle")).doesNotExist();
    }

    @Test
    void hasProjectDetails() {
        assertThat(new File("pom.xml")).content().contains(
                "<groupId>com.cvs</groupId>",
                "<artifactId>acc-test</artifactId>",
                "<name>Test-Project-Name</name>",
                "<description>Test-Description</description>"
        );
    }

    @Test
    void binTestUsesMaven() {
        assertThat(new File("bin/test")).content().contains("mvnw");
    }

    @Test
    void binBuildUsesMaven() {
        assertThat(new File("bin/build")).content().contains("mvnw");
    }

    @Test
    void readmeHasMavenCommands() {
        assertThat(new File("README.md")).content()
                .contains("mvnw verify", "mvnw spring-boot:run", "mvnw sonar:sonar");
    }

    @Test
    void readmeDoesNotHaveGradleCommands() {
        assertThat(new File("README.md")).content().doesNotContain("gradlew");
    }

    @Test
    void readmeHasLinkToCodeCoverage() {
        assertThat(new File("README.md")).content().contains("target/site/jacoco/index.html");
    }

    @Test
    void readmeHasMavenPreReq() {
        assertThat(new File("README.md")).content().contains("Maven wrapper");
    }

    @Test
    void readmeDoesNotWarnAgainstJUnit4() {
        assertThat(new File("README.md")).content().doesNotContain("JUnit version support");
    }
}
