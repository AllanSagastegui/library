package pe.ask.library.usecase.kafka;

import pe.ask.library.model.book.Book;
import pe.ask.library.port.in.usecase.kafka.IUpdateBookStockUseCase;
import pe.ask.library.port.out.persistence.IBookRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@UseCase
public class UpdateBookStockUseCase implements IUpdateBookStockUseCase {
    private final IBookRepository repository;

    public UpdateBookStockUseCase(IBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Void> updateBookStock(UUID bookId, int quantity, boolean isIncrement) {
        return Mono.just(quantity)
                .filter(q -> q >= 0)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .flatMap(q -> processStockUpdate(bookId, q, isIncrement))
                .then();
    }

    private Mono<Book> processStockUpdate(UUID bookId, int quantity, boolean isIncrement) {
        return repository.getBookById(bookId)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .map(book -> {
                    int adjustment = isIncrement ? quantity : -quantity;
                    int newStock = book.getStock() + adjustment;
                    book.setStock(newStock);
                    return book;
                })
                .filter(book -> isIncrement || book.getStock() >= 0)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .flatMap(book -> repository.updateBook(bookId, book));
    }
}