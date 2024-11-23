package iit.y3.oopcw.dto;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public abstract class AbstractBase_DTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -962657520659248485L;
    private Long id;
    private Date creationTimestamp;
}