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
import pe.ask.library.port.in.usecase.book.IGetAllBooksByLanguageUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllBooksByLanguageHandlerTest {

    @Mock
    private IGetAllBooksByLanguageUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private MapBookComplete mapBookComplete;
    @Mock
    private ServerRequest serverRequest;

    private GetAllBooksByLanguageHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetAllBooksByLanguageHandler(useCase, mapper, validator, mapBookComplete);
    }

    @Test
    void listenGETAllBooksByLanguageUseCase() {
        int page = 0;
        int size = 10;
        String language = "ES";

        when(serverRequest.queryParam("page")).thenReturn(Optional.of(String.valueOf(page)));
        when(serverRequest.queryParam("size")).thenReturn(Optional.of(String.valueOf(size)));
        when(serverRequest.pathVariable("language")).thenReturn(language);

        BookComplete bookDomain = mock(BookComplete.class);
        Pageable<BookComplete> pageableDomain = Pageable.<BookComplete>builder()
                .content(List.of(bookDomain))
                .page(page)
                .size(size)
                .build();

        BookCompleteResponse responseItem = mock(BookCompleteResponse.class);
        Pageable<BookCompleteResponse> pageableResponse = Pageable.<BookCompleteResponse>builder()
                .content(List.of(responseItem))
                .page(page)
                .size(size)
                .build();

        when(useCase.getAllBooksByLanguage(page, size, language)).thenReturn(Mono.just(pageableDomain));
        when(mapBookComplete.toResponse(any())).thenReturn(pageableResponse);

        StepVerifier.create(handler.listenGETAllBooksByLanguageUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}