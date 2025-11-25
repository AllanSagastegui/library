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
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.category.IGetAllCategoriesUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllCategoriesHandlerTest {

    @Mock
    private IGetAllCategoriesUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private GetAllCategoriesHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetAllCategoriesHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should return all categories paginated with status 200 OK")
    void listenGETAllCategoriesUseCase() {
        int page = 0;
        int size = 10;

        when(serverRequest.queryParam("page")).thenReturn(Optional.of(String.valueOf(page)));
        when(serverRequest.queryParam("size")).thenReturn(Optional.of(String.valueOf(size)));

        Category category = new Category();
        Pageable<Category> pageable = Pageable.<Category>builder()
                .content(List.of(category))
                .page(page)
                .size(size)
                .build();

        when(useCase.getAllCategories(page, size)).thenReturn(Mono.just(pageable));

        when(mapper.map(any(), any())).thenReturn(new CategoryResponse());

        StepVerifier.create(handler.listenGETAllCategoriesUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}