package iit.y3.oopcw.dto.response;

import iit.y3.oopcw.dto.User_DTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.util.List;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class User_RSPNS extends Abstract_RSPNS {

    @Serial
    private static final long serialVersionUID = 2209027780545705212L;
    private List<User_DTO> users;
}