package pe.ask.library.notificationsender.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import pe.ask.library.notificationsender.utils.SendEmail;
import pe.ask.library.port.in.notification.ISendWelcomeEmail;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendWelcomeEmail implements ISendWelcomeEmail {

    private final SendEmail sendEmail;
    private final SpringTemplateEngine templateEngine;

    private static final String BASE_URL = "https://github.com/AllanSagastegui/library";

    @Override
    public Mono<Void> sendWelcomeEmail(String to, String completeName) {
        return Mono.fromCallable(() -> {
                    log.info("Generando correo de bienvenida para: {}", to);

                    Context context = new Context();
                    context.setVariable("title", "¡Bienvenido a Ask-Library!");
                    context.setVariable("userName", completeName);
                    context.setVariable("portalUrl", BASE_URL);
                    return templateEngine.process("welcome-email", context);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(htmlContent -> {
                    String subject = "Bienvenido a la comunidad de Ask-Library 📚";
                    return sendEmail.send(to, subject, htmlContent);
                });
    }
}