package pe.ask.library.notificationsender.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import pe.ask.library.notificationsender.utils.SendEmail;
import pe.ask.library.port.in.notification.ISendLoanEmail;
import pe.ask.library.port.utils.Status;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendLoanEmail implements ISendLoanEmail {

    private final SendEmail sendEmail;
    private final SpringTemplateEngine customTemplateEngine;

    private static final String BASE_URL = "https://github.com/AllanSagastegui/library";

    @Override
    public Mono<Void> sendLoanEmail(
            UUID loanId,
            String email,
            String completeName,
            LocalDateTime loanDate,
            LocalDateTime estimatedReturnDate,
            Status status
    ) {
        return Mono.fromCallable(() -> {
                    log.info("Preparando plantilla de correo para préstamo [{}]", loanId);

                    Context context = new Context();
                    Map<String, Object> variables = new HashMap<>();

                    variables.put("title", "Actualización de Préstamo");
                    variables.put("userName", completeName);
                    variables.put("newStatus", translateStatus(status));
                    variables.put("statusColor", determineColor(status));
                    variables.put("loanId", loanId.toString());
                    variables.put("loanDate", loanDate);
                    variables.put("estimatedReturnDate", estimatedReturnDate);
                    variables.put("repoLink", BASE_URL);

                    context.setVariables(variables);

                    return customTemplateEngine.process("send-loan-email", context);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(htmlContent -> {
                    String subject = String.format("Ask-Library: Tu préstamo está %s", translateStatus(status));
                    return sendEmail.send(email, subject, htmlContent);
                });
    }

    private String determineColor(Status status) {
        return switch (status) {
            case LOANED -> "#7482ff";
            case RETURNED -> "#28a745";
            case PENDING -> "#ffc107";
            case OVERDUE -> "#dc3545";
            case FAILED -> "#6c757d";
        };
    }

    private String translateStatus(Status status) {
        return switch (status) {
            case PENDING -> "PENDIENTE";
            case LOANED -> "PRESTADO";
            case RETURNED -> "DEVUELTO";
            case OVERDUE -> "VENCIDO";
            case FAILED -> "FALLIDO";
        };
    }
}