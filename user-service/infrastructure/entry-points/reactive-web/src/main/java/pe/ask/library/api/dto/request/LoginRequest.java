package pe.ask.library.api.dto.request;

public record LoginRequest(
        String email,
        String password
) { }
