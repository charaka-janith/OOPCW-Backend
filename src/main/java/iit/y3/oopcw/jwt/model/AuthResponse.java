package iit.y3.oopcw.jwt.model;

import java.io.Serializable;

public class AuthResponse implements Serializable {

    private final String jwt;
    private final String traceId;
    private final String role;

    public AuthResponse(String jwt, String sequence, String role) {
        this.jwt = jwt;
        this.traceId = sequence;
        this.role = role;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getJwt() {
        return jwt;
    }

    public String getRole() {
        return role;
    }
}
