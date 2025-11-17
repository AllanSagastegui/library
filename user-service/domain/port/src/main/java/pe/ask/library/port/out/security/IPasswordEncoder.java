package pe.ask.library.port.out.security;

public interface IPasswordEncoder {
    String encode(String password);
    boolean matches(String rawPassword, String hashedPassword);
}
