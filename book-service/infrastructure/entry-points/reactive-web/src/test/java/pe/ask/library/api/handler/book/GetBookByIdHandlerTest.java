package pe.ask.library.api.handler.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import pe.ask.library.api.dto.response.BookResponse;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.book.Book;
import pe.ask.library.port.in.usecase.book.IGetBookByIdUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBookByIdHandlerTest {

    @Mock
    private IGetBookByIdUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private GetBookByIdHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetBookByIdHandler(useCase, mapper, validator);
    }

    @Test
    void listenGETBookByIdUseCase() {
        UUID id = UUID.randomUUID();
        Book book = new Book();
        BookResponse responseDto = new BookResponse();

        when(serverRequest.pathVariable("id")).thenReturn(id.toString());
        when(useCase.getBookById(id)).thenReturn(Mono.just(book));
        when(mapper.map(book, BookResponse.class)).thenReturn(responseDto);

        StepVerifier.create(handler.listenGETBookByIdUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}