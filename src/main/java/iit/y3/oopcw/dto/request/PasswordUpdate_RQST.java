package iit.y3.oopcw.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordUpdate_RQST extends Abstract_RQST {

    @Serial
    private static final long serialVersionUID = -7672384290863180056L;
    private Long id;
    private String newPassword;
    private String oldPassword;
}