package pe.ask.library.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pe.ask.library.model.book.Format;

import java.util.UUID;

@Getter
@Setter
@Table("book")
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {
    @Id
    @Column("id")
    private UUID id;

    @Column("title")
    private String title;

    @Column("gender")
    private String gender;

    @Column("summary")
    private String summary;

    @Column("number_of_pages")
    private int numberOfPages;

    @Column("language")
    private String language;

    @Column("format")
    private Format format;

    @Column("publisher_id")
    private UUID publisherId;

    @Column("category_id")
    private UUID categoryId;

    @Column("author_id")
    private UUID authorId;
}
