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
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.publisher.IGetPublisherByNameUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPublisherByNameHandlerTest {

    @Mock
    private IGetPublisherByNameUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private GetPublisherByNameHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetPublisherByNameHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should return publishers by name paginated with status 200 OK")
    void listenGETPublisherByNameUseCase() {
        int page = 0;
        int size = 10;
        String nameFilter = "Books";

        when(serverRequest.queryParam("page")).thenReturn(Optional.of(String.valueOf(page)));
        when(serverRequest.queryParam("size")).thenReturn(Optional.of(String.valueOf(size)));
        when(serverRequest.pathVariable("name")).thenReturn(nameFilter);

        Publisher publisher = new Publisher();
        Pageable<Publisher> pageable = Pageable.<Publisher>builder()
                .content(List.of(publisher))
                .page(page)
                .size(size)
                .build();

        when(useCase.getPublishersByName(page, size, nameFilter)).thenReturn(Mono.just(pageable));
        when(mapper.map(any(), any())).thenReturn(new PublisherResponse());

        StepVerifier.create(handler.listenGETPublisherByNameUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}