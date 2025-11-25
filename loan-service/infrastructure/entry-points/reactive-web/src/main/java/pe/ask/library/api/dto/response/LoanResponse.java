package pe.ask.library.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.ask.library.model.loan.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanResponse {
    private String userId;
    private UUID bookId;
    private LocalDateTime loanDate;
    private LocalDateTime estimatedReturnDate;
    private LocalDateTime dateOfRealReturn;
    private Status status;
}
