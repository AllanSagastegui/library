package pe.ask.library.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JacksonConfigTest {

    @Test
    @DisplayName("Should provide configured ObjectMapper with JavaTimeModule and disabled timestamps")
    void testJacksonMapperBean() throws JsonProcessingException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JacksonConfig.class);

        ObjectMapper mapper = context.getBean(ObjectMapper.class);
        assertNotNull(mapper);

        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        String json = mapper.writeValueAsString(now);

        assertTrue(json.contains("2025-01-01T12:00:00"), "Date should be serialized as ISO String, but was: " + json);

        assertFalse(json.startsWith("["), "Date should not be serialized as an array/timestamp");

        context.close();
    }
}