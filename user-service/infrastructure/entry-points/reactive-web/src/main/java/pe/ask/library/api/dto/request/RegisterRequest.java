package pe.ask.library.api.dto.request;

public record RegisterRequest(
        String name,
        String lastName,
        String email,
        String password
) { }
