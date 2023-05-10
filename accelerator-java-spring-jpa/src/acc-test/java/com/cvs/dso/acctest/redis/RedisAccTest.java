package com.cvs.dso.acctest.redis;

import org.junit.jupiter.api.Test;

import java.io.File;

import static com.cvs.dso.acctest.common.TestHarness.buildToolConfig;
import static com.cvs.dso.acctest.common.TestHarness.fileWithinSourceRoot;
import static com.cvs.dso.acctest.common.TestHarness.fileWithinTestRoot;
import static org.assertj.core.api.Assertions.assertThat;

class RedisAccTest {

    @Test
    void redisCacheConfigExists() {
        assertThat(fileWithinSourceRoot("patient/data/RedisCacheConfig.java")).exists().isFile();
        assertThat(fileWithinTestRoot("patient/data/RedisTestContainerConfig.java")).exists().isFile();
    }

    @Test
    void redisCachePropertiesExistInApplicationYamls() {
        assertThat(new File("src/main/resources/application.yaml"))
                .exists()
                .content().contains("redis", "cache");
    }

    @Test
    void hasRedisDependencies() {
        assertThat(buildToolConfig()).content().contains("jedis", "redis", "cache");
    }

    @Test
    void dockerComposeWithRedisExists() {
        assertThat(new File("docker-compose.yaml"))
                .exists()
                .content().contains("image: redis:7");
    }

    @Test
    void readmeContainsRedisInfo() {
        assertThat(new File("README.md"))
                .exists()
                .content().contains("Redis");

        assertThat(new File("ADR.md"))
                .exists()
                .content().contains("Redis");
    }
}
