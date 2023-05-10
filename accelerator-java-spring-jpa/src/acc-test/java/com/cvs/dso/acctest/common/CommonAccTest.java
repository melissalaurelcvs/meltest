package com.cvs.dso.acctest.common;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.cvs.dso.acctest.common.TestHarness.fileWithinSourceRoot;
import static com.cvs.dso.acctest.common.TestHarness.fileWithinTestRoot;
import static org.assertj.core.api.Assertions.assertThat;

class CommonAccTest {

    @Test
    void commonProjectFilesExist() {
        assertThat(new File(".gitignore")).exists().isFile();
    }

    @Test
    void commonMarkdownFilesExist() {
        assertThat(new File("README.md")).exists().isFile();
        assertThat(new File("ADR.md")).exists().isFile();
        assertThat(new File("CHANGELOG.md")).exists().isFile();
        assertThat(new File("CONTRIBUTING.md")).exists().isFile();
        assertThat(new File("readme-template.md")).exists().isFile();
    }

    @Test
    void acceleratorYamlDoesNotExist() {
        assertThat(new File("accelerator.yaml")).doesNotExist();
    }

    @Test
    void sonarPropertiesFileIsExcluded() {
        assertThat(new File("sonar-project.properties")).doesNotExist();
    }

    @Test
    void configFolderIsExcluded() {
        assertThat(new File("config")).doesNotExist();
    }

    @Test
    void hasBddTests() {
        assertThat(new File("src/test/resources/bdd/"))
                .isNotEmptyDirectory()
                .isDirectoryContaining("glob:**.feature");
        assertThat(fileWithinTestRoot("bdd/BddTest.java")).exists().isFile();
        assertThat(fileWithinTestRoot("bdd/CucumberBootstrap.java")).exists().isFile();
    }

    @Test
    void serviceClassRenamed() {
        assertThat(fileWithinSourceRoot("patient/domain/PatientService.java")).exists().isFile();
        assertThat(fileWithinTestRoot("patient/domain/PatientServiceTest.java")).exists().isFile();
    }

    @Test
    void packagesRenamed() {
        assertThat(new File("src/main/java/com/example")).doesNotExist();
        assertThat(new File("src/main/java/com/cvs/acctest")).exists().isDirectory();

        assertThat(new File("src/test/java/com/example")).doesNotExist();
        assertThat(new File("src/test/java/com/cvs/acctest")).exists().isDirectory();

        assertThat(new File("README.md")).content().doesNotContain("com/example");
        assertThat(new File("README.md")).content().contains("com/cvs/acctest");
    }

    @Test
    void readmeDoesNotHavePlaceholders() {
        assertThat(new File("README.md")).content()
                .doesNotContain("---Start", "---End");
        assertThat(new File("ADR.md")).content()
                .doesNotContain("---Start", "---End");
    }

    @Test
    void adrHasFragmentMerged(){
        assertThat(new File("ADR.md")).content()
                .contains("Architecture Decision Record");
         assertThat(new File("ADR.md")).content()
                .contains("Hexagonal Architecture");
    }

    @Test
    void adrHasFragmentMergedInRightOrder() throws IOException {
        String fileContent = Files.readString(Path.of("ADR.md"));
        assertThat(fileContent.indexOf("Why RESTful API"))
                .isGreaterThan(fileContent.indexOf("Architecture Decision Record"));
        assertThat(fileContent.indexOf("Why RESTful API"))
                .isLessThan(fileContent.indexOf("Why Project Lombok"));
    }
}
