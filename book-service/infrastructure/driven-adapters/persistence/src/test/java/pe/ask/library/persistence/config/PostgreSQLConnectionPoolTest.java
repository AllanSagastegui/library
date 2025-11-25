package pe.ask.library.persistence.config;

import io.r2dbc.pool.ConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class PostgreSQLConnectionPoolTest {

    @InjectMocks
    private PostgreSQLConnectionPool connectionPoolFactory;

    @Mock
    private PostgresqlConnectionProperties properties;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        when(properties.host()).thenReturn("localhost");
        when(properties.port()).thenReturn(5432);
        when(properties.database()).thenReturn("testdb");
        when(properties.schema()).thenReturn("public");
        when(properties.username()).thenReturn("user");
        when(properties.password()).thenReturn("pass");
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Should create R2DBC ConnectionPool bean successfully")
    void getConnectionConfigSuccess() {
        ConnectionPool pool = connectionPoolFactory.getConnectionConfig(properties);
        assertNotNull(pool);
    }
}