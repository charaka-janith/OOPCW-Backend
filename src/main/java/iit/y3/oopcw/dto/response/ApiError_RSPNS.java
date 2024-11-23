package iit.y3.oopcw.dto.response;

import iit.y3.oopcw.dto.ApiError_DTO;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class ApiError_RSPNS {
    private static final long serialVersionUID = -1975367487737075997L;
    private List<ApiError_DTO> errors;
}
