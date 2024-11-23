package iit.y3.oopcw.dto;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

@Builder
@Data
public class ApiError_DTO implements Serializable {
    private static final long serialVersionUID = 757808049146522333L;
    private Integer code;
    private String message;
}