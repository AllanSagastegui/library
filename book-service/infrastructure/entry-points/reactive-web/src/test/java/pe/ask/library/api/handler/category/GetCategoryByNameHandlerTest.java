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
import pe.ask.library.port.in.usecase.category.IGetCategoryByNameUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCategoryByNameHandlerTest {

    @Mock
    private IGetCategoryByNameUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private GetCategoryByNameHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetCategoryByNameHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should return paged categories by name with status 200 OK")
    void listenGETCategoryByNameUseCase() {
        int page = 0;
        int size = 10;
        String nameFilter = "Fiction";

        when(serverRequest.queryParam("page")).thenReturn(Optional.of(String.valueOf(page)));
        when(serverRequest.queryParam("size")).thenReturn(Optional.of(String.valueOf(size)));
        when(serverRequest.pathVariable("name")).thenReturn(nameFilter);

        Category category = new Category();
        Pageable<Category> pageable = Pageable.<Category>builder()
                .content(List.of(category))
                .page(page)
                .size(size)
                .build();

        when(useCase.getCategoryByName(page, size, nameFilter)).thenReturn(Mono.just(pageable));

        when(mapper.map(any(), any())).thenReturn(new CategoryResponse());

        StepVerifier.create(handler.listenGETCategoryByNameUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}