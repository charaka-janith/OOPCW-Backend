package iit.y3.oopcw.jwt.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class AuthResponse implements Serializable {

    private final String jwt;
    private final String traceId;
    private final String role;
    private final String firstName;
    private final String lastName;

    public AuthResponse(String jwt, String traceId, String role, String firstName, String lastName) {
        this.jwt = jwt;
        this.traceId = traceId;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}