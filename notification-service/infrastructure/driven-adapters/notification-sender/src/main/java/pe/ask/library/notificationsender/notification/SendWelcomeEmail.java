package pe.ask.library.notificationsender.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;
import pe.ask.library.notificationsender.utils.SendEmail;
import pe.ask.library.port.in.notification.ISendWelcomeEmail;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SendWelcomeEmail implements ISendWelcomeEmail {

    private final SendEmail sendEmail;
    private final SpringTemplateEngine templateEngine;

    public Mono<Void> sendWelcomeEmail(String to, String completeName){
        return null;
    }
}
