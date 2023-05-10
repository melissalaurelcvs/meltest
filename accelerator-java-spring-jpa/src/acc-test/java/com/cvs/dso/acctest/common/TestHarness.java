package com.cvs.dso.acctest.common;

import java.io.File;
import java.util.Arrays;

public class TestHarness {

    public static File buildToolConfig() {
        if (isMavenProject()) {
            return new File("pom.xml");
        } else {
            return new File("build.gradle");
        }
    }

    public static boolean isMavenProject() {
        return new File("pom.xml").exists();
    }

    public static boolean isGradleProject() {
        return new File("build.gradle").exists();
    }

    public static boolean isDockerProject() {
        return new File("docker-compose.yaml").exists();
    }

    public static File fileWithinSourceRoot(String path) {
        File sourceRootDirectory = findDirectoryWithFile(new File("src/main/java"), "Application.java");
        return new File(sourceRootDirectory, path);
    }

    public static File fileWithinTestRoot(String path) {
        File testRootDirectory = findDirectoryWithFile(new File("src/test/java"), "ApplicationTest.java");
        return new File(testRootDirectory, path);
    }

    private static File findDirectoryWithFile(File parent, String fileName) {
        String[] list = parent.list();
        if (Arrays.asList(list).contains(fileName)) {
            return parent;
        } else {
            return findDirectoryWithFile(new File(parent, list[0]), fileName);
        }
    }

}
