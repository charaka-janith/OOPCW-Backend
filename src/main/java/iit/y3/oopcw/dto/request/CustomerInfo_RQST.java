package iit.y3.oopcw.dto.request;

import iit.y3.oopcw.dto.CustomerInfo_DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerInfo_RQST extends Abstract_RQST {

    @Serial
    private static final long serialVersionUID = 4537706006565731855L;
    private CustomerInfo_DTO cusInfo;
}