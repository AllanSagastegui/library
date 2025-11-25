package pe.ask.library.api.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import pe.ask.library.api.exception.UnexpectedException;
import pe.ask.library.api.utils.validation.CustomValidator;
import pe.ask.library.model.exception.BaseException;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactiveHandlerOperationsTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private CustomValidator validator;

    @Mock
    private Object useCase;

    @Mock
    private ServerRequest serverRequest;

    private TestHandler handler;

    @BeforeEach
    void setUp() {
        handler = new TestHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should map source to target using mapper")
    void mapSuccess() {
        String source = "source";
        Integer target = 1;
        when(mapper.map(source, Integer.class)).thenReturn(target);

        Integer result = handler.map(source, Integer.class);

        assertThat(result).isEqualTo(target);
    }

    @Test
    @DisplayName("Should extract body and validate it")
    void extractBodySuccess() {
        TestDto dto = new TestDto("test");
        when(serverRequest.bodyToMono(TestDto.class)).thenReturn(Mono.just(dto));
        when(validator.validate(dto)).thenReturn(Mono.just(dto));

        StepVerifier.create(handler.extractBody(serverRequest, TestDto.class))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should extract body, validate and map to domain")
    void extractBodyToDomainSuccess() {
        TestDto dto = new TestDto("test");
        TestDomain domain = new TestDomain("test");

        when(serverRequest.bodyToMono(TestDto.class)).thenReturn(Mono.just(dto));
        when(validator.validate(dto)).thenReturn(Mono.just(dto));
        when(mapper.map(dto, TestDomain.class)).thenReturn(domain);

        StepVerifier.create(handler.extractBodyToDomain(serverRequest, TestDto.class, TestDomain.class))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should build OK response with body")
    void buildResponseSuccess() {
        Mono<String> pipeline = Mono.just("Success");

        StepVerifier.create(handler.buildResponse(pipeline))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should catch BaseException and propagate it as error in response build")
    void buildResponseBaseException() {
        BaseException ex = new TestException() {};
        Mono<String> pipeline = Mono.error(ex);

        StepVerifier.create(handler.buildResponse(pipeline))
                .expectErrorMatches(BaseException.class::isInstance)
                .verify();
    }

    @Test
    @DisplayName("Should catch RuntimeException and wrap in UnexpectedException")
    void buildResponseUnexpectedException() {
        Mono<String> pipeline = Mono.error(new RuntimeException("Boom"));

        StepVerifier.create(handler.buildResponse(pipeline))
                .expectError(UnexpectedException.class)
                .verify();
    }

    @Test
    @DisplayName("Should build Created response")
    void buildCreatedResponseSuccess() {
        String body = "Created";
        URI location = URI.create("/api/resource/1");

        StepVerifier.create(handler.buildCreatedResponse(body, location))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
                    assertThat(response.headers().getLocation()).isEqualTo(location);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should build No Content response")
    void buildNoContentResponseSuccess() {
        Mono<Void> pipeline = Mono.empty();

        StepVerifier.create(handler.buildNoContentResponse(pipeline))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should build Paged Response")
    void buildPagedResponseSuccess() {
        TestDomain domainItem = new TestDomain("item");
        TestDto responseItem = new TestDto("item");

        Pageable<TestDomain> pageable = Pageable.<TestDomain>builder()
                .page(1).size(10).totalElements(100).totalPages(10)
                .content(List.of(domainItem))
                .build();

        when(mapper.map(domainItem, TestDto.class)).thenReturn(responseItem);

        StepVerifier.create(handler.buildPagedResponse(Mono.just(pageable), TestDto.class))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get path variable")
    void getPathVarSuccess() {
        when(serverRequest.pathVariable("id")).thenReturn("123");
        assertThat(handler.getPathVar(serverRequest, "id")).isEqualTo("123");
    }

    @Test
    @DisplayName("Should get path UUID")
    void getPathUuidSuccess() {
        UUID uuid = UUID.randomUUID();
        when(serverRequest.pathVariable("id")).thenReturn(uuid.toString());
        assertThat(handler.getPathUuid(serverRequest, "id")).isEqualTo(uuid);
    }

    @Test
    @DisplayName("Should get query param or default")
    void getQueryParamSuccess() {
        when(serverRequest.queryParam("page")).thenReturn(Optional.empty());

        int page = handler.getPage(serverRequest);
        assertThat(page).isEqualTo(0);
    }

    @Test
    @DisplayName("Should get specific query param with conversion")
    void getQueryParamWithConversion() {
        when(serverRequest.queryParam("custom")).thenReturn(Optional.of("99"));

        int result = handler.getQueryParam(serverRequest, "custom", Integer::parseInt, 0);
        assertThat(result).isEqualTo(99);
    }

    @Test
    @DisplayName("Should get Principal Name")
    void getPrincipalNameSuccess() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user-test");

        doReturn(Mono.just(principal)).when(serverRequest).principal();

        StepVerifier.create(handler.getPrincipalName(serverRequest))
                .expectNext("user-test")
                .verifyComplete();
    }

    static class TestHandler extends ReactiveHandlerOperations<Object> {
        public TestHandler(Object useCase, ObjectMapper mapper, CustomValidator validator) {
            super(useCase, mapper, validator);
        }
    }

    static class TestDto {
        String name;
        public TestDto(String name) { this.name = name; }
        public TestDto() {}
    }
    static class TestDomain {
        String name;
        public TestDomain(String name) { this.name = name; }
    }

    static class TestException extends BaseException {
        public TestException() {
            super(
                    "TestException",
                    "TestException",
                    "TestException",
                    0,
                    null
            );
        }
    }
}