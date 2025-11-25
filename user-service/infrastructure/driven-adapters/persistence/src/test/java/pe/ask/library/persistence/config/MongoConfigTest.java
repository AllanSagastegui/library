package pe.ask.library.persistence.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.mongo.MongoConnectionDetails;
import org.springframework.boot.ssl.SslBundles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class MongoConfigTest {

    @InjectMocks
    private MongoConfig mongoConfig;

    @Mock
    private MongoDBSecret secret;

    @Mock
    private SslBundles sslBundles;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        when(secret.toUri()).thenReturn("mongodb://user:pass@localhost:27017/db?authSource=admin");
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Should create MongoConnectionDetails bean successfully")
    void mongoPropertiesSuccess() {
        MongoConnectionDetails connectionDetails = mongoConfig.mongoProperties(secret, sslBundles);
        assertNotNull(connectionDetails);
    }
}