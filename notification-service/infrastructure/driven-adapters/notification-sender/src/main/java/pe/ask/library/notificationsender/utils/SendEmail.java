package pe.ask.library.notificationsender.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class SendEmail {

    private final JavaMailSender mailSender;

    public Mono<Void> send(String to, String subject, String htmlContent) {
        return Mono.fromRunnable(() -> {
                    MimeMessage message = mailSender.createMimeMessage();
                    try {
                        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                        helper.setTo(to);
                        helper.setSubject(subject);
                        helper.setText(htmlContent, true);

                        mailSender.send(message);

                    } catch (MessagingException e) {
                        throw new RuntimeException("Error al enviar el correo a " + to, e);
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}