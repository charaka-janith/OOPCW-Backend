package iit.y3.oopcw.springsecurity;

import iit.y3.oopcw.model.User;
import iit.y3.oopcw.service.User_SRVC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SpringSecurityUserService implements UserDetailsService {

    private final User_SRVC userSRVC;

    @Autowired
    public SpringSecurityUserService(@Lazy User_SRVC userSRVC) {
        this.userSRVC = userSRVC;
    }

    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        final Optional<User> user = this.userSRVC.findByUsername(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("Username Not Found");
        return new SpringSecurityUserDetails(user.get());
    }

    public void onAuthenticationSuccess(UserDetails userDetails) {
        final Optional<User> user = this.userSRVC.findByUsername(userDetails.getUsername());
        if (user.isPresent() && user.get().getFailedAttempt() > 0) {
            userSRVC.resetFailedAttempts(user.get().getUserName());
        }
    }

    public void onAuthenticationFailure(String userName) {
        Optional<User> hacker = this.userSRVC.findByUsername(userName);
        if (hacker.isPresent()) {
            if (hacker.get().isAccountNonLocked()) {
                if (hacker.get().getFailedAttempt() < User_SRVC.MAX_FAILED_ATTEMPTS - 1) {
                    userSRVC.increaseFailedAttempts(hacker.orElse(null));
                    throw new BadCredentialsException("Your username and password do not match. Please try again !");
                } else {
                    userSRVC.lock(hacker.orElse(null));
                    throw new LockedException("Your account has been locked due to 5 failed attempts."
                            + " It will be unlocked after 24 hours.");
                }
            } else {
                if (userSRVC.unlockWhenTimeExpired(hacker.orElse(null))) {
                    throw new LockedException("Your account has been unlocked. Please try to login again.");
                }
            }
        }
    }
}
