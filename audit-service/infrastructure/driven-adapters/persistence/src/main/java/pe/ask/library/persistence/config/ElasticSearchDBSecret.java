package pe.ask.library.persistence.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.persistence")
public record ElasticSearchDBSecret(
        String host,
        int port
) {
    public String toUri() {
        return host + ":" + port;
    }
}
