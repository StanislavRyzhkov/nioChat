package company.ryzhkov.server.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
public final class User {
    private ObjectId id;
    private String username;
    private String password;
}
