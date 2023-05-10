package com.cvs.dso.acctest.noredis;

import org.junit.jupiter.api.Test;

import java.io.File;

import static com.cvs.dso.acctest.common.TestHarness.buildToolConfig;
import static com.cvs.dso.acctest.common.TestHarness.fileWithinSourceRoot;
import static com.cvs.dso.acctest.common.TestHarness.fileWithinTestRoot;
import static org.assertj.core.api.Assertions.assertThat;

class NoRedisAccTest {

    @Test
    void redisCacheConfigsDoNotExist() {
        assertThat(fileWithinSourceRoot("patient/data/RedisCacheConfig.java")).doesNotExist();
        assertThat(fileWithinTestRoot("patient/data/RedisTestContainerConfig.java")).doesNotExist();
    }

    @Test
    void redisCachePropertiesDoNotExistInApplicationYamls() {
        assertThat(new File("src/main/resources/application.yaml"))
                .exists()
                .content().doesNotContain("redis", "cache");
    }

    @Test
    void buildToolConfigHasNoRedisDependencies() {
        assertThat(buildToolConfig())
                .exists()
                .content().doesNotContain("jedis", "redis", "cache");
    }

    @Test
    void readmeContainsRedisInfo() {
        assertThat(new File("README.md"))
                .exists()
                .content().doesNotContain("Redis");
    }

    @Test
    void dockerComposeWithOutRedis() {
        assertThat(new File("docker-compose.yaml")).satisfiesAnyOf(
                file -> assertThat(file).doesNotExist(),
                file -> assertThat(file).content().doesNotContain("image: redis:7")
        );
    }
}
