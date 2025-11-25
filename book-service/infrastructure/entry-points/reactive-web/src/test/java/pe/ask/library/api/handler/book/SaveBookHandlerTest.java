package pe.ask.library.api.handler.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.util.UriBuilder;
import pe.ask.library.api.dto.request.BookRequest;
import pe.ask.library.api.dto.response.BookResponse;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.book.Book;
import pe.ask.library.port.in.usecase.book.ISaveBookUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveBookHandlerTest {

    @Mock
    private ISaveBookUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private SaveBookHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SaveBookHandler(useCase, mapper, validator);
    }

    @Test
    void listenPOSTSaveBookUseCase() {
        BookRequest requestDto = new BookRequest();
        Book bookDomain = new Book();
        Book savedBook = new Book();
        savedBook.setId(UUID.randomUUID());
        BookResponse responseDto = new BookResponse();
        URI createdUri = URI.create("/book/" + savedBook.getId());

        when(serverRequest.bodyToMono(BookRequest.class)).thenReturn(Mono.just(requestDto));
        when(validator.validate(requestDto)).thenReturn(Mono.just(requestDto));
        when(mapper.map(requestDto, Book.class)).thenReturn(bookDomain);

        when(useCase.saveBook(bookDomain)).thenReturn(Mono.just(savedBook));
        when(mapper.map(savedBook, BookResponse.class)).thenReturn(responseDto);

        UriBuilder uriBuilder = mock(UriBuilder.class);
        when(serverRequest.uriBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build(any(Object[].class))).thenReturn(createdUri);

        StepVerifier.create(handler.listenPOSTSaveBookUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
                    assertThat(response.headers().getLocation()).isEqualTo(createdUri);
                })
                .verifyComplete();
    }
}