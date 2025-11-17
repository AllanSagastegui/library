package pe.ask.library.security.utils;

import lombok.Getter;

@Getter
public enum TokenExpirationTime {
    ACCESS(3600),
    REFRESH(604800);

    private final long seconds;

    TokenExpirationTime(long seconds){
        this.seconds = seconds;
    }
}
