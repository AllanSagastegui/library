package pe.ask.library.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthorResponse {
    private String firstName;
    private String lastName;
    private String pseudonym;
    private String nationality;
}
