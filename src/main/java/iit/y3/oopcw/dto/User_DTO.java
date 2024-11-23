package iit.y3.oopcw.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class User_DTO extends AbstractBase_DTO {

    @Serial
    private static final long serialVersionUID = 8483242022959618436L;
    private String firstName;
    private String lastName;
    private String username;
    private String contactNo;
    private String email;
    private String status;
    private LocalDate expiryDate;
    private boolean isFirstAttempt;
}