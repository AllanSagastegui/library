package pe.ask.library.api.utils.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.ask.library.api.exception.ValidationException;
import reactor.test.StepVerifier;

class CustomValidatorTest {

    private CustomValidator customValidator;

    @BeforeEach
    void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            customValidator = new CustomValidator(validator);
        }
    }

    static class TestDTO {
        @NotNull
        String name;
        TestDTO(String name) { this.name = name; }
    }

    @Test
    @DisplayName("Should pass validation when object is valid")
    void validateValidObject() {
        TestDTO dto = new TestDTO("Allan");

        StepVerifier.create(customValidator.validate(dto))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ValidationException when object is invalid")
    void validateInvalidObject() {
        TestDTO dto = new TestDTO(null);

        StepVerifier.create(customValidator.validate(dto))
                .expectError(ValidationException.class)
                .verify();
    }
}
