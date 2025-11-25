package pe.ask.library.api.handler.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import pe.ask.library.api.dto.response.BookCompleteResponse;
import pe.ask.library.api.utils.book.MapBookComplete;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.book.IGetAllBooksByAuthorIdUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllBooksByAuthorIdHandlerTest {

    @Mock
    private IGetAllBooksByAuthorIdUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private MapBookComplete mapBookComplete;
    @Mock
    private ServerRequest serverRequest;

    private GetAllBooksByAuthorIdHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetAllBooksByAuthorIdHandler(useCase, mapper, validator, mapBookComplete);
    }

    @Test
    void listenGETAllBooksByAuthorIdUseCase() {
        int page = 0;
        int size = 10;
        UUID authorId = UUID.randomUUID();

        when(serverRequest.queryParam("page")).thenReturn(Optional.of(String.valueOf(page)));
        when(serverRequest.queryParam("size")).thenReturn(Optional.of(String.valueOf(size)));
        when(serverRequest.pathVariable("authorId")).thenReturn(authorId.toString());

        BookComplete bookDomain = mock(BookComplete.class);
        Pageable<BookComplete> pageableDomain = Pageable.<BookComplete>builder()
                .content(List.of(bookDomain))
                .page(page)
                .size(size)
                .build();

        BookCompleteResponse bookResponseDto = mock(BookCompleteResponse.class);
        Pageable<BookCompleteResponse> pageableResponse = Pageable.<BookCompleteResponse>builder()
                .content(List.of(bookResponseDto))
                .page(page)
                .size(size)
                .build();

        when(useCase.getAllBooksByAuthorId(page, size, authorId)).thenReturn(Mono.just(pageableDomain));

        when(mapBookComplete.toResponse(any())).thenReturn(pageableResponse);

        StepVerifier.create(handler.listenGETAllBooksByAuthorIdUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}