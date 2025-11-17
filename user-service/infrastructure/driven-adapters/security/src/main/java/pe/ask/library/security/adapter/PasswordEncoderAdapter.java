package pe.ask.library.security.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.ask.library.port.out.security.IPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements IPasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<String> encode(String raw) {
        return Mono.fromCallable(() -> passwordEncoder.encode(raw))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Boolean> matches(String raw, String encoded) {
        return Mono.fromCallable(() -> passwordEncoder.matches(raw, encoded))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
