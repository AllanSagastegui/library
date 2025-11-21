package pe.ask.library.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.ask.library.model.book.Format;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookResponse {
    private String title;
    private String gender;
    private String summary;
    private int numberOfPages;
    private String language;
    private Format format;
}
