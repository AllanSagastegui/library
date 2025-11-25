package pe.ask.library.persistence.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class PostgresqlConnectionPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class)
            .withPropertyValues(
                    "adapters.persistence.host=localhost",
                    "adapters.persistence.port=5432",
                    "adapters.persistence.database=library_db",
                    "adapters.persistence.schema=public",
                    "adapters.persistence.username=postgres",
                    "adapters.persistence.password=secret"
            );

    @Test
    @DisplayName("Should bind PostgreSQL properties correctly from configuration")
    void testPropertiesBinding() {
        contextRunner.run(context -> {
            PostgresqlConnectionProperties props = context.getBean(PostgresqlConnectionProperties.class);

            assertThat(props.host()).isEqualTo("localhost");
            assertThat(props.port()).isEqualTo(5432);
            assertThat(props.database()).isEqualTo("library_db");
            assertThat(props.schema()).isEqualTo("public");
            assertThat(props.username()).isEqualTo("postgres");
            assertThat(props.password()).isEqualTo("secret");
        });
    }

    @EnableConfigurationProperties(PostgresqlConnectionProperties.class)
    static class TestConfig {
    }
}