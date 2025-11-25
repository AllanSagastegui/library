package pe.ask.library.api.handler.publisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import pe.ask.library.api.dto.response.PublisherResponse;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.port.in.usecase.publisher.IGetPublisherByIdUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPublisherByIdHandlerTest {

    @Mock
    private IGetPublisherByIdUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private GetPublisherByIdHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetPublisherByIdHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should return publisher by ID with status 200 OK")
    void listenGETPublisherByIdUseCase() {
        UUID id = UUID.randomUUID();
        Publisher publisher = new Publisher();
        PublisherResponse responseDto = new PublisherResponse();

        when(serverRequest.pathVariable("id")).thenReturn(id.toString());

        when(useCase.getPublisherById(id)).thenReturn(Mono.just(publisher));
        when(mapper.map(publisher, PublisherResponse.class)).thenReturn(responseDto);

        StepVerifier.create(handler.listenGETPublisherByIdUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}