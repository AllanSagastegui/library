package pe.ask.library.api.handler.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import pe.ask.library.api.dto.request.BookRequest;
import pe.ask.library.api.dto.response.BookResponse;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.book.Book;
import pe.ask.library.port.in.usecase.book.IUpdateBookUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateBookHandlerTest {

    @Mock
    private IUpdateBookUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private UpdateBookHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateBookHandler(useCase, mapper, validator);
    }

    @Test
    void listenPUTUpdateBookUseCase() {
        UUID id = UUID.randomUUID();
        BookRequest requestDto = new BookRequest();
        Book bookDomain = new Book();
        Book updatedBook = new Book();
        BookResponse responseDto = new BookResponse();

        when(serverRequest.pathVariable("id")).thenReturn(id.toString());
        when(serverRequest.bodyToMono(BookRequest.class)).thenReturn(Mono.just(requestDto));
        when(validator.validate(requestDto)).thenReturn(Mono.just(requestDto));
        when(mapper.map(requestDto, Book.class)).thenReturn(bookDomain);

        when(useCase.updateBook(id, bookDomain)).thenReturn(Mono.just(updatedBook));
        when(mapper.map(updatedBook, BookResponse.class)).thenReturn(responseDto);

        StepVerifier.create(handler.listenPUTUpdateBookUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}