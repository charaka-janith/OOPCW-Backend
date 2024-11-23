package iit.y3.oopcw.service.impl;

import iit.y3.oopcw.dto.response.Api_RSPNS;
import iit.y3.oopcw.exceptions.PreConditionFailedException;
import iit.y3.oopcw.model.PasswordHistory;
import iit.y3.oopcw.model.User;
import iit.y3.oopcw.repository.PasswordHistory_REPO;
import iit.y3.oopcw.repository.User_REPO;
import iit.y3.oopcw.service.User_SRVC;
import iit.y3.oopcw.common.Constants.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Service
public class User_SRVC_IMPL implements User_SRVC {
    private final ModelMapper modelMapper;
    private final User_REPO userREPO;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistory_REPO passwordHistory_REPO;

    @Autowired
    public User_SRVC_IMPL(final ModelMapper modelMapper
            , final User_REPO userREPO
            , final PasswordEncoder passwordEncoder
            , final PasswordHistory_REPO passwordHistory_REPO) {
        this.modelMapper = modelMapper;
        this.userREPO = userREPO;
        this.passwordEncoder = passwordEncoder;
        this.passwordHistory_REPO = passwordHistory_REPO;
    }

    @Transactional
    @Override
    public User registerCustomer(@Valid User user) {
        if (user.getFirstName() != null && !user.getFirstName().chars().allMatch(Character::isLetter)
                || user.getLastName() != null && !user.getLastName().chars().allMatch(Character::isLetter)) {
            throw new PreConditionFailedException("Names should contain string characters only");
        }
        if (this.findByUsername(user.getUserName()).isPresent()) {
            throw new PreConditionFailedException("Username already exists");
        }
        PasswordHistory passwordHistory = new PasswordHistory();
        LocalDate expiryDate = LocalDate.now().plusDays(30);
        user.setExpiryDate(expiryDate);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        user.setRole("CUSTOMER");
        user.setStatus("CREATED");
        user = this.userREPO.save(user);
        // save password history
        passwordHistory.setUser(user);
        passwordHistory.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.passwordHistory_REPO.save(passwordHistory);
        return user;
    }

    @Override
    public ResponseEntity<Api_RSPNS> validateUserName(String userName) {
        Optional<User> user = this.userREPO.findByUserNameAndDeletedFalse(userName);
        if (user.isEmpty()) {
            return new Api_RSPNS.ApiResponseBuilder<>().withApiStatusCode(Status.SUCCESS)
                    .withMessage(Status.SUCCESS.description()).withStatusCode(HttpStatus.OK.value())
                    .build();
        } else {
            return new Api_RSPNS.ApiResponseBuilder<>().withApiStatusCode(Status.SUCCESS)
                    .withMessage("Sorry the username is already reserved").withStatusCode(HttpStatus.OK.value())
                    .build();
        }
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return this.userREPO.findByUserNameAndDeletedFalse(username);
    }

    @Override
    public Boolean resetPassword(final Long id, final String newPassword, final String oldPassword) {
        return true;
    }

    @Override
    public User getCurrentUser() {
        return null;
    }

    @Override
    public String getUserRole_byUserName(String username) {
        Optional<User> user = userREPO.findByUserNameAndDeletedFalse(username);
        return user.map(User::getRole).orElse(null);
    }

    @Override
    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userREPO.updateFailedAttempts(newFailAttempts, user.getUserName());
    }

    @Override
    public void resetFailedAttempts(String userName) {
        userREPO.updateFailedAttempts(0, userName);
    }

    @Override
    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userREPO.save(user);
    }

    @Override
    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();
        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userREPO.save(user);
            return true;
        }
        return false;
    }

    @Override
    public String getNextSequenceValue() {
//        return userREPO.getNextValue_sessionSequence().toString();
        return "1999";
    }

    @Override
    public void forgetPassword(String userName) {
        Optional<User> user = userREPO.findByUserNameAndDeletedFalse(userName);
        if (user.isEmpty() || null == user.get().getEmail()) throw new BadCredentialsException("username not found");
//        user.setPassword_token();
        // TODO create a cron job for token
        // TODO sent email for forget password
    }
}