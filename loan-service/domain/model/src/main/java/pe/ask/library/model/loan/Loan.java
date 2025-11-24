package pe.ask.library.model.loan;

import java.time.LocalDateTime;
import java.util.UUID;

public class Loan {
    private UUID id;
    private UUID userId;
    private UUID bookId;
    private LocalDateTime loanDate;
    private LocalDateTime estimatedReturnDate;
    private LocalDateTime dateOfRealReturn;
    private Status status;

    public Loan() {}

    public Loan(UUID id, UUID userId, UUID bookId, LocalDateTime loanDate, LocalDateTime estimatedReturnDate, LocalDateTime dateOfRealReturn, Status status) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.estimatedReturnDate = estimatedReturnDate;
        this.dateOfRealReturn = dateOfRealReturn;
        this.status = status;
    }

    public static LoanBuilder builder() { return new LoanBuilder(); }

    public LoanBuilder toBuilder() {
        return  new LoanBuilder()
                .id(this.id)
                .userId(this.userId)
                .bookId(this.bookId)
                .loanDate(this.loanDate)
                .estimatedReturnDate(this.estimatedReturnDate)
                .dateOfRealReturn(this.dateOfRealReturn)
                .status(this.status);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDateTime getEstimatedReturnDate() {
        return estimatedReturnDate;
    }

    public void setEstimatedReturnDate(LocalDateTime estimatedReturnDate) {
        this.estimatedReturnDate = estimatedReturnDate;
    }

    public LocalDateTime getDateOfRealReturn() {
        return dateOfRealReturn;
    }

    public void setDateOfRealReturn(LocalDateTime dateOfRealReturn) {
        this.dateOfRealReturn = dateOfRealReturn;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
