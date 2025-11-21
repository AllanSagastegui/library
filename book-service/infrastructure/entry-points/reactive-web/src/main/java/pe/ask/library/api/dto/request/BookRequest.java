package pe.ask.library.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.ask.library.model.book.Format;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookRequest {
    private String title;
    private String gender;
    private String summary;
    private int numberOfPages;
    private String language;
    private Format format;
    private UUID publisherId;
    private UUID categoryId;
    private UUID authorId;
}
