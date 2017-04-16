package xyz.luan.acervus.model;

import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Endpoint;
import io.yawp.repository.annotations.Id;
import lombok.Getter;

@Endpoint(path = "/users")
public class User {

    @Id
    private IdRef<User> id;

    @Getter
    private String email;

    @Getter
    private String password;
}
