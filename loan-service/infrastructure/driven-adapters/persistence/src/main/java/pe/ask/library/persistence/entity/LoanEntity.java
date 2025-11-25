package pe.ask.library.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pe.ask.library.model.loan.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("loans")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanEntity {
    @Id
    @Column("id")
    private UUID id;

    @Column("user_id")
    private String userId;

    @Column("book_id")
    private UUID bookId;

    @Column("loan_date")
    private LocalDateTime loanDate;

    @Column("estimated_return_date")
    private LocalDateTime estimatedReturnDate;

    @Column("date_of_real_return")
    private LocalDateTime dateOfRealReturn;

    @Column("status")
    private Status status;
}
