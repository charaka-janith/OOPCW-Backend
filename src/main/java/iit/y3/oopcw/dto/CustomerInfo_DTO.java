package iit.y3.oopcw.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerInfo_DTO extends AbstractBase_DTO {

    @Serial
    private static final long serialVersionUID = 8483242022959618436L;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String contactNo;
    private String email;
}