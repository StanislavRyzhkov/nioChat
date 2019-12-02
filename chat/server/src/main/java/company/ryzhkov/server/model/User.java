package company.ryzhkov.server.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class User {
    private final String id;
    private final String username;
    private final String password;
}
