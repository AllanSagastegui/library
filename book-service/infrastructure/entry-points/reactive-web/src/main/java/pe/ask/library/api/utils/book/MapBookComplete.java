package pe.ask.library.api.utils.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.ask.library.api.dto.response.*;
import pe.ask.library.model.author.Author;
import pe.ask.library.model.book.Book;
import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.category.Category;
import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MapBookComplete {

    private final IPaginationUtils utils;

    public Pageable<BookCompleteResponse> toResponse(Pageable<BookComplete> domain) {
        List<BookCompleteResponse> contentResponse = domain.getContent().stream()
                .map(this::mapSingleBook)
                .toList();

        return Pageable.<BookCompleteResponse>builder()
                .content(contentResponse)
                .page(domain.getPage())
                .size(domain.getSize())
                .totalElements(domain.getTotalElements())
                .totalPages(domain.getTotalPages())
                .build();
    }

    private BookCompleteResponse mapSingleBook(BookComplete domain) {
        if (domain == null) return null;

        return new BookCompleteResponse(
                toBookResponse(domain.getBook()),
                toAuthorResponse(domain.getAuthor()),
                toCategoryResponse(domain.getCategory()),
                toPublisherResponse(domain.getPublisher())
        );
    }

    private BookResponse toBookResponse(Book book) {
        if (book == null) return null;
        return new BookResponse(
                book.getTitle(),
                book.getGender(),
                book.getSummary(),
                book.getNumberOfPages(),
                book.getLanguage(),
                book.getFormat()
        );
    }

    private AuthorResponse toAuthorResponse(Author author) {
        if (author == null) return null;
        return new AuthorResponse(
                author.getFirstName(),
                author.getLastName(),
                author.getPseudonym(),
                author.getNationality()
        );
    }

    private CategoryResponse toCategoryResponse(Category category) {
        if (category == null) return null;
        return new CategoryResponse(
                category.getName(),
                category.getDescription()
        );
    }

    private PublisherResponse toPublisherResponse(Publisher publisher) {
        if (publisher == null) return null;
        return new PublisherResponse(
                publisher.getName(),
                publisher.getAddress(),
                publisher.getCountry()
        );
    }
}
