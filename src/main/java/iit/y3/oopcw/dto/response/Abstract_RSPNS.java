package iit.y3.oopcw.dto.response;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class Abstract_RSPNS implements Serializable {

    @Serial
    private static final long serialVersionUID = -6512711126659630457L;
    private final LocalDateTime timestamp = LocalDateTime.now();
}