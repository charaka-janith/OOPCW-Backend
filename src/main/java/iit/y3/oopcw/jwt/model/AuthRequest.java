package iit.y3.oopcw.jwt.model;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Setter
@Getter
public class AuthRequest implements Serializable {

    private String username;
    private String password;

    // need default constructor for JSON Parsing
    public AuthRequest() {}

    public AuthRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }
}