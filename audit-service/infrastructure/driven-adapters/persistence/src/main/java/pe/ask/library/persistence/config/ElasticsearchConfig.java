package pe.ask.library.persistence.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ElasticSearchDBSecret.class)
public class ElasticsearchConfig extends ReactiveElasticsearchConfiguration {

    private final ElasticSearchDBSecret secret;

    @NonNull
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(secret.toUri())
                .withConnectTimeout(5000)
                .withSocketTimeout(5000)
                .build();
    }
}
