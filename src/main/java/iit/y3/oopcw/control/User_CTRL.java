package iit.y3.oopcw.control;

import iit.y3.oopcw.dto.request.CustomerInfo_RQST;
import iit.y3.oopcw.dto.request.ForgetPassword_RQST;
import iit.y3.oopcw.dto.request.PasswordUpdate_RQST;
import iit.y3.oopcw.dto.response.Api_RSPNS;
import iit.y3.oopcw.dto.response.User_RSPNS;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/oop-cw")
public interface User_CTRL {
	
    @PostMapping("/public/user/register")
    ResponseEntity<Api_RSPNS> registerCustomer(@Valid @RequestBody CustomerInfo_RQST customerInfoRequest);
    
    @GetMapping("/public/user/{userName}/validate")	
    ResponseEntity<Api_RSPNS> validateUserName(@PathVariable("userName") @NotBlank String userName);
	
    @GetMapping("/users/me")
    User_RSPNS findCurrent();
    
    @PostMapping("/users/reset-password")
    ResponseEntity<Api_RSPNS> resetPassword(@Valid @RequestBody PasswordUpdate_RQST passwordUpdateRequest);
    
    @GetMapping("/users/password-grace-period")
    ResponseEntity<Api_RSPNS> checkPasswordExpiration();

    @PostMapping("/public/users/forget-password")
    ResponseEntity<Api_RSPNS> forgetPassword(
            @Valid @RequestBody ForgetPassword_RQST request
    );
}
