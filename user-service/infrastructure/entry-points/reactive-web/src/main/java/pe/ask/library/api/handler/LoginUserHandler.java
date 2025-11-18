package pe.ask.library.api.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.request.LoginRequest;
import pe.ask.library.api.exception.UnexpectedException;
import pe.ask.library.api.mapper.TokenMapper;
import pe.ask.library.api.utils.validation.CustomValidator;
import pe.ask.library.model.exception.BaseException;
import pe.ask.library.port.in.usecase.user.ILoginUserUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoginUserHandler {

    private final ILoginUserUseCase loginUserUseCase;
    private final CustomValidator validator;
    private final TokenMapper mapper;

    public Mono<ServerResponse> listenPOSTLoginUserUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequest.class)
                .flatMap(validator::validate)
                .flatMap(dto -> loginUserUseCase.execute(dto.email(), dto.password()))
                .map(mapper::toLoginResponse)
                .flatMap(response -> ServerResponse
                        .ok()
                        .bodyValue(response)
                )
                .onErrorResume(ex ->
                        Mono.error(ex instanceof BaseException ? ex : new UnexpectedException(ex))
                );
    }
}
