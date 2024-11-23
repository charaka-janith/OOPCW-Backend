package iit.y3.oopcw.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PasswordUpdate_RSPNS {
    private static final long serialVersionUID = -3298529066121658581L;
    private Boolean passwordMatch;
}