package pe.ask.library.model.loan;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoanBuilder {
    private UUID id;
    private UUID userId;
    private UUID bookId;
    private LocalDateTime loanDate;
    private LocalDateTime estimatedReturnDate;
    private LocalDateTime dateOfRealReturn;
    private Status status;

    public LoanBuilder() {
    }

    public LoanBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public LoanBuilder userId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public LoanBuilder bookId(UUID bookId) {
        this.bookId = bookId;
        return this;
    }

    public LoanBuilder loanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
        return this;
    }

    public LoanBuilder estimatedReturnDate(LocalDateTime estimatedReturnDate) {
        this.estimatedReturnDate = estimatedReturnDate;
        return this;
    }

    public LoanBuilder dateOfRealReturn(LocalDateTime dateOfRealReturn) {
        this.dateOfRealReturn = dateOfRealReturn;
        return this;
    }

    public LoanBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public Loan build() {
        return new Loan(id, userId, bookId, loanDate, estimatedReturnDate, dateOfRealReturn, status);
    }
}
