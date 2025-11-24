package pe.ask.library.api.handler;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.request.LoginRequest;
import pe.ask.library.api.dto.response.LoginResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validation.CustomValidator;
import pe.ask.library.port.in.usecase.user.ILoginUserUseCase;
import reactor.core.publisher.Mono;

@Component
public class LoginUserHandler extends ReactiveHandlerOperations<
        ILoginUserUseCase
        > {

    protected LoginUserHandler(ILoginUserUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenPOSTLoginUserUseCase(ServerRequest serverRequest) {
        return extractBody(serverRequest, LoginRequest.class)
                .flatMap(dto -> useCase.loginUser(dto.getEmail(), dto.getPassword()))
                .map(domainObject -> map(domainObject, LoginResponse.class))
                .as(this::buildResponse);
    }
}
