package iit.y3.oopcw.service;

import iit.y3.oopcw.dto.response.Api_RSPNS;
import iit.y3.oopcw.model.User;
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;
import java.util.Optional;

public interface User_SRVC {
    /* limit login attempts */
    public static final int MAX_FAILED_ATTEMPTS = 5;
    public static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours
    User registerCustomer(@Valid User user);
    ResponseEntity<Api_RSPNS> validateUserName(String userName);
    Boolean resetPassword(Long id, String newPassword, String oldPassword);
    User getCurrentUser();
    String getUserRole_byUserName(String username);
    Optional<User> findByUsername(String parameter);
//    BankUserDto loginValidate(BankUserRequest bankUserRequest);
//    BankUserDto loginBankValidate(@Valid BankUsersRequest bankUsersRequest);
    /* limit login attempts */
    public void increaseFailedAttempts(User user);
    public void resetFailedAttempts(String userName);
    public void lock(User user);
    public boolean unlockWhenTimeExpired(User user);
    /* sequence */
    String getNextSequenceValue();
    public void forgetPassword (String userName);
}