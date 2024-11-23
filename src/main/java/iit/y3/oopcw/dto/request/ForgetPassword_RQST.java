package iit.y3.oopcw.dto.request;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class ForgetPassword_RQST extends Abstract_RQST {

    @Serial
    private static final long serialVersionUID = -7672384290863180056L;
    private String userName;
    private String password;
    private String retypePassword;
}