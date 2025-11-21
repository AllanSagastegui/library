package pe.ask.library.api.handler;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.request.RegisterRequest;
import pe.ask.library.api.dto.response.RegisterResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validation.CustomValidator;
import pe.ask.library.model.user.User;
import pe.ask.library.port.in.usecase.user.IRegisterUserUseCase;
import reactor.core.publisher.Mono;

@Component
public class RegisterUserHandler extends ReactiveHandlerOperations<
        IRegisterUserUseCase
        > {


    protected RegisterUserHandler(IRegisterUserUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenPOSTRegisterUserUseCase(ServerRequest serverRequest) {
        return extractBodyToDomain(serverRequest, RegisterRequest.class, User.class)
                .flatMap(useCase::registerUser)
                .flatMap(response -> buildCreatedResponse(
                        map(response, RegisterResponse.class),
                        createUri(serverRequest, "/{id}", response.getId())
                ));
    }
}
