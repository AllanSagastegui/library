package pe.ask.library.persistence.config;

import org.springframework.boot.autoconfigure.mongo.MongoConnectionDetails;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.PropertiesMongoConnectionDetails;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MongoDBSecret.class)
public class MongoConfig {

    @Bean
    MongoConnectionDetails mongoProperties(MongoDBSecret secret, SslBundles sslBundles) {
        MongoProperties properties = new MongoProperties();
        properties.setUri(secret.toUri());
        return new PropertiesMongoConnectionDetails(properties, sslBundles);
    }
}
