package pe.ask.library.api.utils.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.api.dto.response.BookCompleteResponse;
import pe.ask.library.model.author.Author;
import pe.ask.library.model.book.Book;
import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.book.Format;
import pe.ask.library.model.category.Category;
import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MapBookCompleteTest {

    @Mock
    private IPaginationUtils paginationUtils;

    private MapBookComplete mapBookComplete;

    @BeforeEach
    void setUp() {
        mapBookComplete = new MapBookComplete(paginationUtils);
    }

    @Test
    @DisplayName("Should map Pageable<BookComplete> to response correctly")
    void toResponseSuccess() {
        Book book = mock(Book.class);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getGender()).thenReturn("Drama");
        when(book.getSummary()).thenReturn("Summary");
        when(book.getNumberOfPages()).thenReturn(200);
        when(book.getLanguage()).thenReturn("ES");
        when(book.getFormat()).thenReturn(Format.DIGITAL);

        Author author = mock(Author.class);
        when(author.getFirstName()).thenReturn("John");
        when(author.getLastName()).thenReturn("Doe");
        when(author.getPseudonym()).thenReturn("JD");
        when(author.getNationality()).thenReturn("USA");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Fiction");
        when(category.getDescription()).thenReturn("Fiction books");

        Publisher publisher = mock(Publisher.class);
        when(publisher.getName()).thenReturn("Books Pub");
        when(publisher.getAddress()).thenReturn("123 St");
        when(publisher.getCountry()).thenReturn("UK");

        BookComplete bookComplete = mock(BookComplete.class);
        when(bookComplete.getBook()).thenReturn(book);
        when(bookComplete.getAuthor()).thenReturn(author);
        when(bookComplete.getCategory()).thenReturn(category);
        when(bookComplete.getPublisher()).thenReturn(publisher);

        Pageable<BookComplete> domainPage = Pageable.<BookComplete>builder()
                .content(List.of(bookComplete))
                .page(1)
                .size(10)
                .totalElements(50L)
                .totalPages(5)
                .build();

        var responsePage = mapBookComplete.toResponse(domainPage);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getPage());
        assertEquals(10, responsePage.getSize());
        assertEquals(50L, responsePage.getTotalElements());
        assertEquals(5, responsePage.getTotalPages());
        assertEquals(1, responsePage.getContent().size());

        BookCompleteResponse item = responsePage.getContent().get(0);

        assertNotNull(item.getBook());
        assertEquals("Test Title", item.getBook().getTitle());
        assertEquals("DIGITAL", item.getBook().getFormat().name());

        assertNotNull(item.getAuthor());
        assertEquals("John", item.getAuthor().getFirstName());

        assertNotNull(item.getCategory());
        assertEquals("Fiction", item.getCategory().getName());

        assertNotNull(item.getPublisher());
        assertEquals("Books Pub", item.getPublisher().getName());
    }

    @Test
    @DisplayName("Should handle null nested objects gracefully")
    void toResponseWithNulls() {
        BookComplete bookComplete = mock(BookComplete.class);
        when(bookComplete.getBook()).thenReturn(null);
        when(bookComplete.getAuthor()).thenReturn(null);
        when(bookComplete.getCategory()).thenReturn(null);
        when(bookComplete.getPublisher()).thenReturn(null);

        Pageable<BookComplete> domainPage = Pageable.<BookComplete>builder()
                .content(List.of(bookComplete))
                .page(0)
                .size(10)
                .build();

        var responsePage = mapBookComplete.toResponse(domainPage);

        assertNotNull(responsePage);
        assertFalse(responsePage.getContent().isEmpty());

        BookCompleteResponse item = responsePage.getContent().get(0);

        assertNull(item.getBook());
        assertNull(item.getAuthor());
        assertNull(item.getCategory());
        assertNull(item.getPublisher());
    }

    @Test
    @DisplayName("Should handle null content item in list")
    void toResponseWithNullItem() {
        Pageable<BookComplete> domainPage = Pageable.<BookComplete>builder()
                .content(java.util.Arrays.asList((BookComplete) null))
                .build();

        var responsePage = mapBookComplete.toResponse(domainPage);

        assertNotNull(responsePage);
        assertNull(responsePage.getContent().get(0));
    }
}