package pe.ask.library.api.utils.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.ask.library.api.utils.exception.ValidationException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class CustomValidator {

    private final Validator validator;

    public <T> Mono<T> validate(T obj) {
        return Mono.fromCallable(() -> validator.validate(obj))
                .flatMap(violations -> violations.isEmpty()
                        ? Mono.just(obj)
                        : Mono.error(new ValidationException(
                                violations.stream()
                                        .collect(Collectors.toMap(
                                                v -> v.getPropertyPath().toString(),
                                                ConstraintViolation::getMessage
                                        ))
                        ))
                );
    }
}
