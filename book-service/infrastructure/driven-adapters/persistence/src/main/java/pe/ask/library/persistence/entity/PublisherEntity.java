package pe.ask.library.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table("publisher")
@NoArgsConstructor
@AllArgsConstructor
public class PublisherEntity {
    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("address")
    private String address;

    @Column("country")
    private String country;
}
