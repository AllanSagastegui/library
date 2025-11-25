package pe.ask.library.persistence.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class MongoDBSecretTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class)
            .withPropertyValues(
                    "adapters.persistence.username=mongouser",
                    "adapters.persistence.password=mongopass",
                    "adapters.persistence.host=localhost",
                    "adapters.persistence.port=27017",
                    "adapters.persistence.database=library_db",
                    "adapters.persistence.authSource=admin"
            );

    @Test
    @DisplayName("Should bind MongoDB properties correctly from configuration and generate valid URI")
    void testPropertiesBindingAndUriGeneration() {
        contextRunner.run(context -> {
            MongoDBSecret secret = context.getBean(MongoDBSecret.class);

            assertThat(secret.username()).isEqualTo("mongouser");
            assertThat(secret.password()).isEqualTo("mongopass");
            assertThat(secret.host()).isEqualTo("localhost");
            assertThat(secret.port()).isEqualTo(27017);
            assertThat(secret.database()).isEqualTo("library_db");
            assertThat(secret.authSource()).isEqualTo("admin");

            String expectedUri = "mongodb://mongouser:mongopass@localhost:27017/library_db?authSource=admin";
            assertThat(secret.toUri()).isEqualTo(expectedUri);
        });
    }

    @EnableConfigurationProperties(MongoDBSecret.class)
    static class TestConfig {
    }
}