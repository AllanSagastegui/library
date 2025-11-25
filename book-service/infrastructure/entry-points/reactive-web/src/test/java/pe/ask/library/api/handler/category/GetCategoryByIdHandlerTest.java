package pe.ask.library.api.handler.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import pe.ask.library.api.dto.response.CategoryResponse;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.category.Category;
import pe.ask.library.port.in.usecase.category.IGetCategoryByIdUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCategoryByIdHandlerTest {

    @Mock
    private IGetCategoryByIdUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private GetCategoryByIdHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetCategoryByIdHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should return category by ID with status 200 OK")
    void listenGETCategoryByIdUseCase() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        CategoryResponse responseDto = new CategoryResponse();

        when(serverRequest.pathVariable("id")).thenReturn(id.toString());

        when(useCase.getCategoryById(id)).thenReturn(Mono.just(category));

        when(mapper.map(category, CategoryResponse.class)).thenReturn(responseDto);

        StepVerifier.create(handler.listenGETCategoryByIdUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}
