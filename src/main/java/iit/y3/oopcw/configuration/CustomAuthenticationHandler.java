package iit.y3.oopcw.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import iit.y3.oopcw.dto.ApiError_DTO;
import iit.y3.oopcw.model.User;
import iit.y3.oopcw.service.User_SRVC;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import iit.y3.oopcw.dto.response.ApiError_RSPNS;

@Component
public class CustomAuthenticationHandler extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final String HTTP_RESPONSE_CONTENT_TYPE = "application/json";
    private final PasswordEncoder passwordEncoder;
    private final User_SRVC userSRVC;
    private final ObjectMapper objectMapper;

    @Autowired
    public CustomAuthenticationHandler(ObjectMapper objectMapper
            , @Lazy PasswordEncoder passwordEncoder
            , @Lazy User_SRVC userSRVC) {
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.userSRVC = userSRVC;
    }

    public void onAuthenticationFailure(HttpServletRequest request
            , HttpServletResponse response
            , AuthenticationException exception
    ) throws IOException, ServletException {

        if (exception.getClass().equals(CredentialsExpiredException.class)) {

            response.setStatus(HttpStatus.LOCKED.value());
            response.setContentType(CustomAuthenticationHandler.HTTP_RESPONSE_CONTENT_TYPE);
            response.getOutputStream()
                    .println(objectMapper.writeValueAsString(ApiError_RSPNS.builder()
                            .errors(Collections.singletonList(ApiError_DTO.builder()
                                            .code(HttpStatus.LOCKED.value())
                                            .message("Password is expired. Please reset your Password ")
                                            .build()
                                    )
                            ).build()));

        } else if (exception.getClass().equals(BadCredentialsException.class)) {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CustomAuthenticationHandler.HTTP_RESPONSE_CONTENT_TYPE);
            response.getOutputStream()
                    .println(objectMapper.writeValueAsString(ApiError_RSPNS.builder()
                            .errors(Collections.singletonList(ApiError_DTO.builder()
                                            .code(HttpStatus.UNAUTHORIZED.value())
                                            .message("Authentication Failed")
                                            .build()
                                    )
                            ).build()));

        } else if (exception.getClass().equals(DisabledException.class)) {
            final Optional<User> user = this.userSRVC.findByUsername(request.getParameter("username"));
            if (this.passwordEncoder.matches(request.getParameter("password"), user.map(User::getPassword).orElse(null))) {
                response.setStatus(HttpStatus.LOCKED.value());
                response.setContentType(CustomAuthenticationHandler.HTTP_RESPONSE_CONTENT_TYPE);
                response.getOutputStream()
                        .println(objectMapper.writeValueAsString(ApiError_RSPNS.builder()
                                .errors(Collections.singletonList(ApiError_DTO.builder()
                                                .code(HttpStatus.LOCKED.value())
                                                .message("Reset your Password !!")
                                                .build()
                                        )
                                ).build()));

            } else {

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(CustomAuthenticationHandler.HTTP_RESPONSE_CONTENT_TYPE);
                response.getOutputStream()
                        .println(objectMapper.writeValueAsString(ApiError_RSPNS.builder()
                                .errors(Collections.singletonList(ApiError_DTO.builder()
                                                .code(HttpStatus.UNAUTHORIZED.value())
                                                .message("Authentication Failed")
                                                .build()
                                        )
                                ).build()));
            }
        } else {
            setDefaultFailureUrl("/re");
        }
    }
}