package pe.ask.library.api.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.request.RegisterRequest;
import pe.ask.library.api.exception.UnexpectedException;
import pe.ask.library.api.mapper.UserMapper;
import pe.ask.library.api.utils.routes.UserRoutes;
import pe.ask.library.api.utils.validation.CustomValidator;
import pe.ask.library.model.exception.BaseException;
import pe.ask.library.port.in.usecase.user.IRegisterUserUseCase;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class RegisterUserHandler {

    private final IRegisterUserUseCase registerUserUseCase;
    private final CustomValidator validator;
    private final UserMapper mapper;

    public Mono<ServerResponse> listenPOSTRegisterUserUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RegisterRequest.class)
                .flatMap(validator::validate)
                .map(mapper::registerRequestToEntity)
                .flatMap(registerUserUseCase::execute)
                .map(mapper::toRegisterResponse)
                .flatMap(response -> ServerResponse
                        .created(URI.create(UserRoutes.REGISTER))
                        .bodyValue(response)
                )
                .onErrorResume(ex ->
                        Mono.error(ex instanceof BaseException ? ex : new UnexpectedException(ex))
                );
    }
}
