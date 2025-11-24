package pe.ask.library.kafkalistener.payload;

public record WelcomeEmail(
        String completeName,
        String email
){}