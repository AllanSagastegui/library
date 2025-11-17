package pe.ask.library.persistence.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.persistence")
public record MongoDBSecret(
        String username,
        String password,
        String host,
        int port,
        String database,
        String authSource
){
    public String toUri() {
        return "mongodb://" + username + ":" + password + "@" + host + ":" + port +
                "/" + database + "?authSource=" + authSource;
    }
}
