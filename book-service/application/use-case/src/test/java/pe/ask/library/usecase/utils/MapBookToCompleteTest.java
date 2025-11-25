package pe.ask.library.usecase.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.author.Author;
import pe.ask.library.model.book.Book;
import pe.ask.library.model.category.Category;
import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import pe.ask.library.port.out.persistence.ICategoryRepository;
import pe.ask.library.port.out.persistence.IPublisherRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MapBookToCompleteTest {

    @Mock
    private IAuthorRepository authorRepository;
    @Mock
    private ICategoryRepository categoryRepository;
    @Mock
    private IPublisherRepository publisherRepository;

    private MapBookToComplete mapper;

    @BeforeEach
    void setUp() {
        mapper = new MapBookToComplete(authorRepository, categoryRepository, publisherRepository);
    }

    @Test
    @DisplayName("Should assemble BookComplete with all related entities")
    void map() {
        UUID authorId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID publisherId = UUID.randomUUID();

        Book book = Book.builder()
                .withAuthorId(authorId)
                .withCategoryId(categoryId)
                .withPublisherId(publisherId)
                .withTitle("Test Book")
                .build();

        Author author = Author.builder().withId(authorId).withFirstName("Author").build();
        Category category = Category.builder().withId(categoryId).withName("Cat").build();
        Publisher publisher = Publisher.builder().withId(publisherId).withName("Pub").build();

        when(authorRepository.getAuthorById(authorId)).thenReturn(Mono.just(author));
        when(categoryRepository.getCategoryById(categoryId)).thenReturn(Mono.just(category));
        when(publisherRepository.getPublisherById(publisherId)).thenReturn(Mono.just(publisher));

        StepVerifier.create(mapper.map(book))
                .assertNext(bookComplete -> {
                    assertThat(bookComplete.getBook()).isEqualTo(book);
                    assertThat(bookComplete.getAuthor()).isEqualTo(author);
                    assertThat(bookComplete.getCategory()).isEqualTo(category);
                    assertThat(bookComplete.getPublisher()).isEqualTo(publisher);
                })
                .verifyComplete();
    }
}