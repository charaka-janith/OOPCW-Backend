package iit.y3.oopcw.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import iit.y3.oopcw.common.Constants.Status;

public class Api_RSPNS<T> {

    @JsonIgnore
    private int httpCode;

    private String executionCode;
    private String executionDescription;

    @JsonInclude(Include.NON_NULL)
    private T data;

    private Api_RSPNS(ApiResponseBuilder builder) {
        this.httpCode = builder.httpCode;
        this.executionCode = builder.apiStatusCode;
        this.executionDescription = builder.message;
        this.data = (T) builder.data;
    }

    protected Api_RSPNS() {}

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getExecutionCode() {
        return executionCode;
    }

    public void setExecutionCode(String executionCode) {
        this.executionCode = executionCode;
    }

    public String getExecutionDescription() {
        return executionDescription;
    }

    public void setExecutionDescription(String executionDescription) {
        this.executionDescription = executionDescription;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class ApiResponseBuilder<T> {

        private int httpCode = HttpStatus.OK.value();
        private String apiStatusCode = "00";
        private String message = "";
        private T data;

        public ApiResponseBuilder<T> withData(T data) {
            this.data = data;
            return this;
        }

        public ApiResponseBuilder<T> withStatusCode(int httpCode) {
            this.httpCode = httpCode;
            return this;
        }

        public ApiResponseBuilder<T> withApiStatusCode(Status status) {
            this.apiStatusCode = status.val();
            return this;
        }

        public ApiResponseBuilder<T> withApiStatusCode(String status) {
            this.apiStatusCode = status;
            return this;
        }

        public ApiResponseBuilder<T> withMessage(String message) {
            this.message = message;
            return this;
        }

        public ResponseEntity<Api_RSPNS> build() {
            Api_RSPNS<T> apiRSPNS = new Api_RSPNS<>(this);
            return ResponseEntity.status(apiRSPNS.httpCode).body(apiRSPNS);
        }
    }
}
