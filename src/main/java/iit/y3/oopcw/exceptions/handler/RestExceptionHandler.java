package iit.y3.oopcw.exceptions.handler;

import iit.y3.oopcw.dto.response.ApiError_RSPNS;
import iit.y3.oopcw.dto.ApiError_DTO;
import iit.y3.oopcw.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        final List<ApiError_DTO> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> ApiError_DTO.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(error.getField() + " : " + error.getDefaultMessage())
                        .build()
                ).collect(Collectors.toList());
        exception.getBindingResult().getGlobalErrors()
                .stream()
                .map(error -> ApiError_DTO.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(error.getObjectName() + " : " + error.getDefaultMessage())
                        .build()
                ).forEach(errors::add);
        final ApiError_RSPNS response = ApiError_RSPNS.builder().errors(errors).build();
        return this.handleExceptionInternal(exception, response, headers, HttpStatus.BAD_REQUEST, request);
    }
    
    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(
            final ConstraintViolationException exception,
            final WebRequest request) {
        final List<ApiError_DTO> errors = exception.getConstraintViolations()
                .stream()
                .map(violation -> ApiError_DTO.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(violation.getRootBeanClass().getName() + ' ' +
                                violation.getPropertyPath() + ": " +
                                violation.getMessage())
                        .build()
                ).collect(Collectors.toList());
        final ApiError_RSPNS response = ApiError_RSPNS.builder().errors(errors).build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException exception,
            final WebRequest request) {
        final ApiError_RSPNS response = ApiError_RSPNS.builder()
                .errors(Collections.singletonList(ApiError_DTO.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(exception.getName() + " should be of type " + exception.getRequiredType().getName())
                        .build()
                )).build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentials(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.UNAUTHORIZED, exception.getLocalizedMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequest(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
    }

    @ExceptionHandler({LoginAttemptsExceedException.class})
    public ResponseEntity<Object> handleExceededLoginAttempts(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.NOT_ACCEPTABLE, exception.getLocalizedMessage());
    }

    @ExceptionHandler({PreConditionFailedException.class})
    public ResponseEntity<Object> handlePreconditionFailed(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.PRECONDITION_FAILED, exception.getLocalizedMessage());
    }

    @ExceptionHandler({NotAuthorizedException.class})
    public ResponseEntity<Object> handleNotAuthorized(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.UNAUTHORIZED, exception.getLocalizedMessage());
    }

    @ExceptionHandler({NotAuthenticatedException.class})
    public ResponseEntity<Object> handleNotAuthenticated(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.UNAUTHORIZED, exception.getLocalizedMessage());
    }

    @ExceptionHandler({SqlGrammarException.class})
    public ResponseEntity<Object> handleSqlGrammar(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.PRECONDITION_FAILED, exception.getLocalizedMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
    }

    @ExceptionHandler({FileNotFoundException.class})
    public ResponseEntity<Object> handleFileNotFound(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
    }


    private ResponseEntity<Object> handleGeneralException(final HttpStatus httpStatus, final String localizedMessage) {
        final ApiError_RSPNS response = ApiError_RSPNS.builder()
                .errors(Collections.singletonList(ApiError_DTO.builder()
                        .code(httpStatus.value())
                        .message(localizedMessage)
                        .build()
                )).build();
        return new ResponseEntity<>(response, new HttpHeaders(), httpStatus);
    }
}
