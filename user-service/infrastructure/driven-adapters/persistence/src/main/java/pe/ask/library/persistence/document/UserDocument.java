package pe.ask.library.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "user")
public class UserDocument {
    @Id
    @Field("id")
    private String id;

    @Field("name")
    private String name;

    @Field("last_name")
    private String lastName;

    @Field("email")
    private String email;

    @Field("password")
    private String password;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("role_id")
    private String roleId;
}
